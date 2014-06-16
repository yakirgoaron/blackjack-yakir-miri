<%-- 
    Document   : CreateGame
    Created on : 13/06/2014, 18:39:39
    Author     : miri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Game</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery-1.11.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="bs-example bs-example-tabs">
            <ul id="CreateGameTabs" class="nav nav-tabs">
                <li class>
                    <a href="#CreateGame" data-toggle="tab">Create Game</a>
                </li>
                <li class>
                    <a href="#LoadGame" data-toggle="tab">Load Game</a>
                </li>
            </ul>
            <% Object errorMessage = request.getAttribute("Error");%>                                     
            <% if (errorMessage != null) {%>
            <span class="alert alert-danger"><%=errorMessage%></span>
            <% } %>
            <div id="CreateGameTabsContent" class="tab-content">
                <div class="tab-pane fade container" id="CreateGame">
                    <form class="form-signin-heading" action="CreateGame">
                        <input type="hidden" name="tab" value="1" />
                        Game Name: <input class="form-control" type="text" name="GameName" autofocus required><br>
                        Comp Players: <input class="form-control" type="number" name="CompPlayersNumber"  max="5" value = "1" min="0" required><br>
                        Human Players: <input class="form-control" type="number" name="HumanPlayers" max="6" value = "1" min="1" required><br>
                        <button class="btn btn-lg btn-primary btn-block" type="submit" >Create</button>
                        <!-- input type="submit" value="Submit" -->                                    
                    </form>
                </div>
                <div class="tab-pane fade container" id="LoadGame">
                    <form method="post" action="CreateGame" enctype="multipart/form-data">
                        <input type="hidden" name="tab" value="0" />
                        <input type="file" class="form-control" name="XMLFile" required><br /><br />
                        <button class="btn btn-lg btn-primary btn-block" type="submit" >Create</button>
                        <!-- input type="submit" name="submit" value="Submit" -->
                   </form>
                </div>
            </div>
       </div>
    </body>
</html>
