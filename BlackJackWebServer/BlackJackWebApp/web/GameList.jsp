<%-- 
    Document   : GameList
    Created on : 13/06/2014, 19:11:46
    Author     : miri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Games Created</title>
        <meta name="viewport" content="width=device-width">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery-1.11.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/GameList.js"></script>
    </head>
    <body>
        <div class="container">
           <div class="panel panel-default">
                <table class="table">
                    <thead>
                         <th>#</th>
                         <th>Game Name</th>
                         <th>Human Players</th>
                         <th>Joined Players</th>
                    </thead>
                    <tbody id="Gamelist">
                    </tbody>
                </table>
           </div>
           <div class="container">
               <form class="form-signin-heading" >
                    Player Name: <input class="form-control" id="PlayerName" type="text" name="PlayerNameTxT" autofocus required><br>
                    <select id="dataCombo" class="selectpicker">
                     </select>
                    <input type="hidden" name="GameName" id="GameName" >
                   <button class="btn btn-lg btn-primary btn-block" type="submit" >Join</button>
               </form>
           </div>
        </div>
        <% Object errorMessage = request.getAttribute("Error");%>
        <% if (errorMessage != null) {%>

        <div class="alert alert-danger alert-dismissable">
            <strong>Error!</strong> <%=errorMessage%>
        </div>
        <% } %>
    </body>
</html>
