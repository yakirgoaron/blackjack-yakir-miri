/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var CurrPlayer;
var IsResigned = false;
var IsSplitChosen = false;
var HandToTake = 1;

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
var valuepg = 100;

function refreshPlayers(users) {
    RemovePlayers();  
    
    $.each(users || [], function(index, val) 
    {
        var image;
        
       if (val.Name !== "Dealer"){
        if(val.Type === 'COMPUTER')
           image = 'CompPlayer.png';
       else
           image = 'HumanPlayer.png';
        $('<div class="row" id="'+val.Name+'">' + 
                '<div class="col-md-8 col-xs-8"><div class="col-md-1 col-xs-1"><img id="img'+val.Name+'" src="images/players/'+image+'"/></div>'+
                  '<div class="col-md-7 col-xs-7"><div class="row Bet1"><div class="Cards"></div></div><div class="row Bet2"><div class="Cards"></div></div></div></div>').appendTo($("#players")).fadeIn(4000);
        
     //   $('<div class="row Bet1"><div class="Cards"></div></div>').appendTo($('#'+val.Name));
      //  $('<div class="row Bet2"><div class="Cards"></div></div>').appendTo($('#'+val.Name));
    }
    });
}

function RemovePlayer(name){
    $('"#"'+name);
}

function AddEffect(playerName){
 
    $('#img'+playerName).addClass("Effect");
    var player  = $('#'+playerName).children(".Bet"+HandToTake);
    console.log("player");
    console.log(player);
    console.log(player.offset());
    
    $('#'+playerName).children(".Bet"+HandToTake).addClass("Effect");
}
function RemoveDealerCards(){
    $("#DealerCards").empty();
}
function GameOver(){
    window.location = "GameOver.html";
}

function GameWinner(Name){
    $('<div> winner is: '+ Name+'</div>').appendTo('#'+Name);
}
function RemovePlayers(){
    $("#players").empty();
}
function DisableResign(){
    document.getElementById("btnResign").setAttribute("disabled", "disabled");
}

function ShowUserAction(Name, Action){
    $('<div> action:'+Action+'</div>').appendTo('#'+Name);
}

function ShowDealtCards(playerName, CardsTag, arraycards, BetNum){

    var PlayerCards;
    if($('#Deck'+playerName+BetNum).length > 0)
    {
       PlayerCards = $('#Deck'+playerName+BetNum);
       PlayerCards.empty();
    }
    else
    {
        PlayerCards = $('<div id="Deck'+playerName+BetNum+'" class="col-md-1 col-xs-1"> </div>').appendTo($("#Deck"));
    }
    CardsTag.empty();
    var card = $("#"+playerName);
    $.each(arraycards || [], function(index, val)
    {   
        $('<div class="col-md-1 col-xs-1"><img class="'+val.rank+val.suit+'" /></div>').appendTo(PlayerCards);        
        
    });
    PlayerCards.show(
                function(){
                  $(this).animate({right: $(this).offset().left - card.offset().left ,bot:$(this).offset().top - card.offset().top }, 3000,function(){
                           
                            CardsTag.empty();
                            $(this).children().appendTo(CardsTag);
                            $(this).remove();
                             
                        });
                    
                    });    
}

function CardsDealt(event)
{
    var CardsTag;
    var arraycards;
    var PlayerData = ajaxPlayerDetails(event.playerName);
    if (event.playerName === "Dealer")   
        CardsTag = $("#Dealer").children(".Cards");
    else{
        console.log($("#"+event.playerName).children());
        CardsTag = $("#"+event.playerName).children().children().children(".Bet1").children(".Cards");
        var CardsTag2 = $("#"+event.playerName).children().children().children(".Bet2").children(".Cards");
        arraycards = PlayerData.Bets[1].BetCards;
        ShowDealtCards(event.playerName, CardsTag2, arraycards, "2");
    }
    arraycards = PlayerData.Bets[0].BetCards;
    ShowDealtCards(event.playerName, CardsTag, arraycards, "1");
    
    /*
    var PlayerCards;
    if($('Deck'+event.playerName).length > 0)
    {
       PlayerCards = $('Deck'+event.playerName);
       PlayerCards.empty();
    }
    else
    {
        PlayerCards = $('<div id="Deck'+event.playerName+'" class="col-md-1 col-xs-1"> </div>').appendTo($("#Deck"));
    }
    CardsTag.empty();
    var card = $("#"+event.playerName);
    $.each(arraycards || [], function(index, val)
    {   
        $('<div class="col-md-1 col-xs-1"><img class="'+val.rank+val.suit+'" /></div>').appendTo(PlayerCards);        
        
    });
    PlayerCards.show(
                function(){
                    $(this).animate({right: $(this).offset().left - card.offset().left ,bot:$(this).offset().top - card.offset().top }, 3000,function(){
                           
                            CardsTag.empty();
                            $(this).children().appendTo(CardsTag);
                            $(this).remove();
                             
                        });
                    
                    });*/
    
}


function ChangeProgressDown()
{
    setTimeout(ProgressBarUpdate, 1000);
}

function ProgressBarUpdate()
{
       valuepg -= 10;
       $('#ValuePrg').css('width', valuepg+'%').attr('aria-valuenow', valuepg);
       if(valuepg < 40)
       {
           $('#ValuePrg').attr('class', 'progress-bar progress-bar-danger');
       }
       else if(valuepg < 80)
       {
           $('#ValuePrg').attr('class', 'progress-bar progress-bar-warning');
       }
       ChangeProgressDown();
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
                GameWinner(val.playerName);
                break;
            case "NEW_ROUND":
                RemovePlayers();
                RemoveDealerCards();
                ajaxShowPlayers();
                HandToTake = 1;
                IsSplitChosen = false;
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
                AddEffect(val.playerName);
                break;
            case "PROMPT_PLAYER_TO_TAKE_ACTION":                
                ajaxCurrPlayer();
                ChangeProgressDown();
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
                if(val.playerName === CurrPlayer.name)
                {
                    if (val.playerAction === "SPLIT")
                        IsSplitChosen = true;
                    else if ((IsSplitChosen === true) && 
                             (val.playerAction !== "HIT") && (HandToTake < 2))
                        HandToTake++;
                }
                
                ShowUserAction(val.playerName, val.playerAction);
                break;       
                    
        }
    });
    if(IsTrriger)
    {
        triggerAjaxHandleEvents();
    }
}
function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}
function ajaxShowPlayers() {
    jQuery.ajax({
            dataType: 'json',
            url: "GamePlayers",
            async: false,
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
            async:false,
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

function ajaxPlayerDetails(PlayerName) 
{
var Temp; 
 jQuery.ajax({
            type:'post',
            dataType: 'json',
            url: "PlayerDetails?PlayerName="+PlayerName,
            async: false,
            timeout: 2000,
            error: function() {
                console.log("Failed to submit");
            },
            success: function(Player) 
            {
                Temp = Player;                              
            }
        });   
        return Temp;
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
            $('#ValuePrg').css('width', 100+'%').attr('aria-valuenow', 100);
            return false;
        });
        
        $("#ActionHit").submit(DoPlayerAction);
        $("#ActionDouble").submit(DoPlayerAction);
        $("#ActionSplit").submit(DoPlayerAction);
        $("#ActionStand").submit(DoPlayerAction);
    }
);
function DoPlayerAction() {
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
             console.log($(this).serialize());
            triggerAjaxHandleEvents();
        }

    });

    $("#DoAction").hide();
    $('#ValuePrg').css('width', 100+'%').attr('aria-valuenow', 100);
    // by default - we'll always return false so it doesn't redirect the user.

    return false;
}