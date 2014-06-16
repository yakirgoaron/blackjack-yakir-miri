/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function() { // onload...do
    var Success = false;
    //add a function to the submit event
    $("#CreateGame").submit(function() {
        jQuery.ajax({
            dataType:'plain',
            timeout: 2000,
            error: function() {
                Success = false;
                console.log("Failed to submit");
            },
            success: function(data) {
                console.log("succes to submit");
                
                if (data.equals("SUCCESS")){
                    Success = true;
                }
                else
                {
                    Success = false;
                }
                
            }
        });

                // $("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return Success;
    });
});


