/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var CurrPlayerName;
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
        $( $('<div class="row" id="'+val.Name+'>' + 
                '<div class="col-md-1 col-xs-1"></div></div>').append($('<img src="images/players/'+image+'"/>'))).
                appendTo($("#players"));
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

function DealWithEvents(events) {
    $("#players").empty();
    $.each(events|| [], function(index,val){
    
        switch(val.type){
            case "CARDS_DEALT":
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
                var Name = event.PlayerName;
                RemovePlayer(Name);
                              
                if (Name === CurrPlayerName){
                        IsResigned = true;
                        DisableResign(); 
                        //todo: handle
                        GameOver();
                 }
                break;
            case "PLAYER_TURN":
                break;
            case "PROMPT_PLAYER_TO_TAKE_ACTION":
                break;
            case "USER_ACTION":
                break;       
                    
        }
    });
    
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

function ajaxCurrPlayer() {
    jQuery.ajax({
            dataType: 'json',
            url: "PlayerDetails",
            timeout: 2000,
            error: function() {
                console.log("Failed to submit");
            },
            success: function(PlayerName) 
            {
                   CurrPlayerName = PlayerName;
            }
        });
}

$(function() 
    {        
        ajaxCurrPlayer();
        ajaxHandleEvents();
    }
);