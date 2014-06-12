/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function refreshWaitingGames(users) {
    //clear all current users
    $("#Gamelist").empty();
    $.each(users || [], function(index, val) {
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<tr><td></td><td>' + val.Name + '</td><td>' +  val.HumanPlayers + '</td><td>' +  val.JoinedPlayers + '</td></tr>').appendTo($("#Gamelist"));
    });
}

$(function() 
    { 
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
            }
        });
    }
)