/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var CurrPlayer;
var IsResigned = false;

var GAME_START = "GameStart";
var GAME_OVER = "GameOver";
var GAME_WINNER = "GameWinner";
var PLAYER_RESIGNED = "PlayerResigned";
var NEW_ROUND = "NewRound";
var PLAYER_TURN = "PlayerTurn";
var CARDS_DEALT = "CardsDealt";
var PROMPT_PLAYER_TO_TAKE_ACTION = "PromptPlayerToTakeAction";
var USER_ACTION = "UserAction";
var PLACE_BET = "PlaceBet";
var DOUBLE = "Double";
var HIT = "Hit";
var SPLIT = "Split";
var STAND = "Stand";
var refreshRate = 2000;

function refreshPlayers(users) {
    $("#players").empty();
  
    
    $.each(users || [], function(index, val) 
    {
        var image;
        
       if (val.Name !== "Dealer"){
        if(val.Type === 'COMPUTER')
           image = 'CompPlayer.png';
       else
           image = 'HumanPlayer.png';
        $('<div class="row" id="'+val.Name+'">' + 
                '<div class="col-md-1 col-xs-1"><img src="images/players/'+image+'"/></div></div>').appendTo($("#players"));
        $('<div class="Bet1"><div class="Cards"></div></div>').appendTo($('#'+val.Name));
    }
    });
}

function RemovePlayer(name){
    $('"#"'+name);
}

function GameOver(){
    window.location = "GameOver.html";
}

function DisableResign(){
    document.getElementById("btnResign").setAttribute("disabled", "disabled");
}
function CardsDealt(event)
{
    var CardsTag = $("#"+event.playerName).children(".Bet1").children(".Cards");
    var arraycards = event.cards;
    CardsTag.empty();
    $.each(arraycards, function(index, val)
    {
        $('<div class="col-md-1 col-xs-1"><img class="'+val.rank+val.suit+'" /></div>').appendTo(CardsTag);
    });
}
function DealWithEvents(events) {
    
    
    var IsTrriger = true;
    $.each(events|| [], function(index,val){
    console.log(val.type);
        switch(val.type){
            case "CARDS_DEALT":
                CardsDealt(val);
                break;
            case "GAME_OVER":
                GameOver();
                break;
            case "GAME_START":
                ajaxShowPlayers();
                break;
            case "GAME_WINNER":
                break;
            case "NEW_ROUND":
                break;
            case "PLAYER_RESIGNED":
                var Name = val.playerName;
                RemovePlayer(Name);
                              
                if (Name === CurrPlayer.name){
                        IsResigned = true;
                        DisableResign(); 
                        //todo: handle
                        GameOver();
                 }
                break;
            case "PLAYER_TURN":
                break;
            case "PROMPT_PLAYER_TO_TAKE_ACTION":                
                ajaxCurrPlayer();
                if(val.playerName === CurrPlayer.name)
                {
                    // TODO TIMER
                    console.log(CurrPlayer.Bets[0].BetWage);
                    if(CurrPlayer.Bets[0].BetWage === 0)
                    {
                        $("#PlaceBetfrm").show();
                        
                    }
                    else
                    {
                        $("#DoAction").show();
                        
                    }
                    IsTrriger = false;
                }
                break;
            case "USER_ACTION":
                break;       
                    
        }
    });
    if(IsTrriger)
    {
        triggerAjaxHandleEvents();
    }
    
}
function ajaxShowPlayers() {
    jQuery.ajax({
            dataType: 'json',
            url: "GamePlayers",
            timeout: 2000,
            error: function() {
                console.log("Failed to submit");
            },
            success: function(data) 
            {
                   refreshPlayers(data);
            }
        });
}

function ajaxHandleEvents() {
    jQuery.ajax({
            dataType: 'json',
            url: "EventsHappened",
            timeout: 2000,
            error: function() {
                console.log("Failed to submit");
            },
            success: function(events) 
            {
                   DealWithEvents(events);
            }
        });
        
}

function triggerAjaxHandleEvents() {
    setTimeout(ajaxHandleEvents, refreshRate);
}

function ajaxCurrPlayer() {
    jQuery.ajax({
            dataType: 'json',
            url: "PlayerDetails",
            async: false,
            timeout: 2000,
            error: function() {
                console.log("Failed to submit");
            },
            success: function(PlayerName) 
            {
                   CurrPlayer = PlayerName;
            }
        });
}
$(function() 
    {        
        ajaxCurrPlayer();
        triggerAjaxHandleEvents();
        $("#PlaceBetfrm").submit(function() {
            jQuery.ajax({
                data: $(this).serialize(),
                url: this.action,
                timeout: 2000,
                error: function() {
                    console.log("Failed to submit");
                },
                success: function(r) {
                    //do not add the user string to the chat area
                    //since it's going to be retrieved from the server
                    //$("#result h1").text(r);
                    triggerAjaxHandleEvents();
                }
            });

            $("#PlaceBetfrm").hide();
            // by default - we'll always return false so it doesn't redirect the user.
            
            return false;
        });
        
        $("#DoAction").submit(function() {
            jQuery.ajax({
                data: $(this).serialize(),
                url: this.action,
                timeout: 2000,
                error: function() {
                    console.log("Failed to submit");
                     console.log($(this).serialize());
                },
                success: function(r) {
                    //do not add the user string to the chat area
                    //since it's going to be retrieved from the server
                    //$("#result h1").text(r);
                     console.log($(this).serialize());
                    triggerAjaxHandleEvents();
                }
               
            });

            $("#DoAction").hide();
            // by default - we'll always return false so it doesn't redirect the user.
            
            return false;
        });
    }
);