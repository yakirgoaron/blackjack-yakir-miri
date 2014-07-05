/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var refreshRate = 7000;

function refreshWaitingGames(users) {
    $("#Gamelist").empty();
    $.each(users || [], function(index, val) 
    {
        $('<tr><td><input id="'+index+'" type="radio" name="1" ></td><td>' + 
                val.Name + '</td><td>' +  val.HumanPlayers + '</td><td>' +  val.JoinedPlayers + '</td>'+
                '<input type="hidden" value="'+val.Loaded+'" /></tr>').
                appendTo($("#Gamelist"));
    });
}


function AddUsersToList(users)
{
    $("#dataCombo").empty();
    $.each(users || [], function(index, val) 
    {
        console.log(val);
        if(val.Status === 'ACTIVE' && val.Type !== 'COMPUTER')
        $('<option value="'+val.Name+'">'+val.Name+'</option>').appendTo($("#dataCombo"));
    });
    
}

function ChangeTextToCombo(e)
{
    
    if($("#"+e.target.id).parent().parent().children(":hidden").val() === 'true')
    {
        jQuery.ajax({
            dataType: 'json',
            url: "GamePlayers?GameName="+$("#"+e.target.id).parent().next().text(),
            timeout: 2000,
            error: function() {
                console.log("Failed to submit");
            },
            success: function(data) 
            {
                   AddUsersToList(data);
            }
        });
        $("#PlayerName").removeAttr("required");
        $("#PlayerName").hide();
        $("#dataCombo").show();
    }
    else
    {
        $("#dataCombo").hide();
        $("#PlayerName").show();
        $("#PlayerName").prop('required',true);
    }
    $("#GameName").prop("value",$("#"+e.target.id).parent().next().text());
    
}
function triggerAjaxTableContent() {
    setTimeout(ajaxTableContent, refreshRate);
}

function ajaxTableContent() {
    jQuery.ajax({
            dataType: 'json',
            url: "GamesStatus",
            timeout: 2000,
            error: function() {
                console.log("Failed to submit");
            },
            success: function(data) 
            {
                   refreshWaitingGames(data);
                   $(":radio").change(ChangeTextToCombo);
            }
        });
        $("#dataCombo").hide();
        triggerAjaxTableContent();
}

$(function() 
    { 
        ajaxTableContent();
      
      
    }
);

