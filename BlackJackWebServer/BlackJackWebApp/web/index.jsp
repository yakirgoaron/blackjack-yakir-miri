<%-- 
    Document   : index
    Created on : 20/05/2014, 08:43:01
    Author     : Yakir
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="/bjwebapi/CreateGame">
             Game Name: <input type="text" name="GameName"><br>
             Comp Players: <input type="text" name="CompPlayersNumber"><br>
             Human Players: <input type="text" name="HumanPlayers"><br>
            <input type="submit" value="Submit">
        </form>
        <form method="post" action="/bjwebapi/CreateGame" enctype="multipart/form-data">
            <input type="file" name="XMLFile"><br /><br />
            <input type="submit" name="submit" value="Submit">
       </form>
    </body>
</html>
