/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var CurrPlayer;
var IsResigned = false;
var CallResignFlag = true;
var IsSplitChosen = false;
var HandToTake = 1;
var FirstDealCards = true;

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
var Delta = 10;
var timers = new Array();

function refreshPlayers(users) {
    RemovePlayers();  
    
    $.each(users || [], function(index, val) 
    {
        var image;
        
       if ((val.Name !== "Dealer") && (val.Status !== "RETIRED")){
        if(val.Type === 'COMPUTER')
           image = 'CompPlayer.png';
       else
           image = 'HumanPlayer.png';
        $('<div class="row spacer" id="'+val.Name+'">' + 
                '<div class="col-md-8 col-xs-8"><div class="col-md-3 col-xs-3"><div class ="row"><img class="img-responsive" id="img'+val.Name+'" src="images/players/'+image+'"/></div><div class="row"><label>Name:'+val.Name+'</label><label>Money:'+val.Money+'</label></div></div>'+

                  '<div class="col-md-7 col-xs-7"><div class="row Bet1"><div class="col-md-3 col-xs-3 TotalBet"></div><div class="Cards"></div></div>'+
                  '<div class="row Bet2"><div class="col-md-3 col-xs-3 TotalBet"></div><div class="Cards">'+
                  '</div></div></div></div>').appendTo($("#players")).fadeIn(4000);
        
     //   $('<div class="row Bet1"><div class="Cards"></div></div>').appendTo($('#'+val.Name));
      //  $('<div class="row Bet2"><div class="Cards"></div></div>').appendTo($('#'+val.Name));
    }
    });
}

function RemovePlayer(name){
    $('#'+name).empty();
}

function AddEffect(playerName){
 
    
    $("img").removeClass("Effect");
    $('#img'+playerName).addClass("Effect");
    
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
    $('<div class="PlayerAction"> action:'+Action+'</div>').appendTo('#'+Name);
}

function RemoveUserAction(){
    $(".PlayerAction").empty();
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

function UpdateBets(PlayerData){
    var Bet1 = $("#"+PlayerData.name).children().children().children(".Bet1"); 
    $(Bet1).children(".TotalBet").children().remove();
    $(Bet1).children(".TotalBet").append('<label>Bet:'+PlayerData.Bets[0].BetWage+'</label>');
    
    if (PlayerData.Bets[1].BetWage !== 0)
    {
        var Bet2 = $("#"+PlayerData.name).children().children().children(".Bet2");
        $(Bet2).children(".TotalBet").children().remove();
        $(Bet2).children(".TotalBet").append('<label>Bet:'+PlayerData.Bets[1].BetWage+'</label>');
    }
}
function CardsDealt(event)
{
    var CardsTag;
    var arraycards;
    var PlayerData = ajaxPlayerDetails(event.playerName);
    if (event.playerName === "Dealer")   
        CardsTag = $("#Dealer").children(".Cards");
    else{
        UpdateBets(PlayerData);
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
    timers.push(setTimeout(ProgressBarUpdate, 1000));
}

function ProgressBarToNormal()
{
    $('#ValuePrg').css('width', '100%').attr('aria-valuenow', 100);
    valuepg = 100;
    $('#ValuePrg').attr('class', 'rogress-bar progress-bar-success');
}


function ProgressBarForAction()
{
    $('#ValuePrg').css('width', '180%').attr('aria-valuenow', 110);
    valuepg = 110;
    $('#ValuePrg').attr('class', 'rogress-bar progress-bar-success');
    ChangeProgressDown();
}

function ProgressBarUpdate()
{
       valuepg -= Delta;
       for (var i = 0; i < timers.length; i++)
       {
           clearTimeout(timers[i]);
       }
       if(valuepg === 100-Delta)
       {
           
           $('#ValuePrg').attr('class', 'progress-bar progress-bar-success');
       }
       $('#ValuePrg').css('width', valuepg+'%').attr('aria-valuenow', valuepg);
       if(valuepg < 40)
       {
           $('#ValuePrg').attr('class', 'progress-bar progress-bar-danger');
       }
       else if(valuepg < 80)
       {
           $('#ValuePrg').attr('class', 'progress-bar progress-bar-warning');
       }
      // console.log(valuepg);
       if(valuepg > 0)
           ChangeProgressDown();
       else
       {
           for (var i = 0; i < timers.length; i++)
           {
                clearTimeout(timers[i]);
           }
           ProgressBarToNormal();
           GameOver();
           CallResignFlag = false;
       }
}

function HideResign(){
    $("#Resign").hide();
}

function ShowResign(){
   $("#Resign").show(); 
}

function DealWithEvents(events) {
    
    
    var IsTrriger = true;
    $.each(events|| [], function(index,val){
    console.log(val.type+" "+val.playerName);
        switch(val.type){
            case "CARDS_DEALT":                
                CardsDealt(val);
                break;
            case "GAME_OVER":
                GameOver();
                CallResignFlag = false;
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
                RemoveUserAction();
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
                
                if(val.playerName === CurrPlayer.name)
                {
                    // TODO TIMER
                    console.log(CurrPlayer.Bets[0].BetWage);
                    HideResign();
                    Delta = 100/(val.timeout/1000);
                    $('#ValuePrg').attr('class', 'rogress-bar progress-bar-success');
                    if(CurrPlayer.Bets[0].BetWage === 0)
                    {
                        $("#PlaceBetfrm").show(ChangeProgressDown()); 
                        $("#PlaceBet").attr('max', CurrPlayer.money);
                        $("#BetLabel").show();
                        $("#BetLabel").text($("#PlaceBet").val());
                        
                    }
                    else
                    {
                        $("div").removeClass("Effect");
                        console.log(HandToTake);
                        var playerName = val.playerName;
                        var Hand  = $('#'+playerName).children().children().children(".Bet"+HandToTake);
    
                        Hand.addClass("Effect");
                        
                        $("#DoAction").show(ProgressBarForAction());
                        
                    }
                    IsTrriger = false;
                }
                break;
            case "USER_ACTION":       
                if(val.playerName === CurrPlayer.name)
                {
                    ShowResign();
                    if (val.playerAction === "SPLIT"){
                        IsSplitChosen = true;
                        UpdateBets(ajaxPlayerDetails(val.playerName));
                    }
                    else if ((IsSplitChosen === true) && 
                             (val.playerAction !== "HIT") && (HandToTake < 2))
                        HandToTake++;
                    $('#Error').fadeOut(function(){$('#Error').hide()});
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
        $("#PlaceBet").change(function(){
            $("#BetLabel").text($("#PlaceBet").val());
        });
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
                    ShowResign();
                    triggerAjaxHandleEvents();
                }
            });

            $("#PlaceBetfrm").hide();
            $("#BetLabel").hide();
            // by default - we'll always return false so it doesn't redirect the user.
            ProgressBarToNormal();
            return false;
        });
        window.onunload = Resign;
      //  $("#Resign").submit(Resign);
        $("#ActionHit").submit(DoPlayerAction);
        $("#ActionDouble").submit(DoPlayerAction);
        $("#ActionSplit").submit(DoPlayerAction);
        $("#ActionStand").submit(DoPlayerAction);
         
    }
);
function Resign(){
    if (CallResignFlag === true){
        jQuery.ajax({
            data: $(this).serialize(),
            async: false,
            url: "PlayerResign?CloseApp=true",
            timeout: 2000,
            error: function() {
                console.log("Failed to submit");
            },
            success: function(r) {
                //do not add the user string to the chat area
                //since it's going to be retrieved from the server
                //$("#result h1").text(r);
                HideResign();
            }
        });

        return false;
    }
}
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
             
            if(r !== undefined)
            {
                $('#Error').show().fadeIn();
                $('#errormessage').text(r);
            }
            triggerAjaxHandleEvents();
        }

    });

    $("#DoAction").hide();
    ProgressBarToNormal();
    // by default - we'll always return false so it doesn't redirect the user.

    return false;
}