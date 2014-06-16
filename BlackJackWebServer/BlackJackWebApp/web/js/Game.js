/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var IsResigned = false;

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

function DisableResign(){
    document.getElementById("btnResign").setAttribute("disabled", "disabled");
}

function refreshPlayers(events) {
    $("#players").empty();
    $.each(events|| [], function(index,val){
    
        switch(val.type){
            case CARDS_DEALT:
                break;
            case GAME_OVER:
                break;
            case GAME_START:
                break;
            case GAME_WINNER:
                break;
            case NEW_ROUND:
                break;
            case PLAYER_RESIGNED:
                var Name = event.PlayerName;
                RemovePlayer(Name);
                
                //todo: handle
                if (Name.equals(PlayerName)){
                        IsResigned = true;
                        DisableResign();                       
                        GameOver();
                 }
                break;
            case PLAYER_TURN:
                break;
            case PROMPT_PLAYER_TO_TAKE_ACTION:
                break;
            case USER_ACTION:
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

$(function() 
    { 
        ajaxShowPlayers();
        ajaxHandleEvents();
      
      
    }
);