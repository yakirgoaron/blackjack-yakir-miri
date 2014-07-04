<%-- 
    Document   : LoginPage
    Created on : 13/06/2014, 16:44:56
    Author     : miri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="script/jquery-2.0.3.min.js"></script>
    </head>
    <body>
        <div class="container">
            <form class="form-signin-heading" action="Login" > 
                <h2 class="form-signin-heading">Please login</h2>
                Host: <input type="text" class="form-control" name="Host" value="localhost" required autofocus>
                Port: <input type="number" class="form-control" name="Port" value="8080" required>
                <button class="btn btn-lg btn-primary btn-block" type="submit" >Login</button>
            </form>
            <% Object errorMessage = request.getAttribute("LoginError");%>
            <% if (errorMessage != null) {%>
            <div class="alert alert-danger alert-dismissable">
                <strong>Error!</strong> <%=errorMessage%>
            </div>
            <% } %>

        </div>
    </body>
</html>
