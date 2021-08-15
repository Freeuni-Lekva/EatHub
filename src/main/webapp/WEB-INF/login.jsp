<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.servlets.LoginServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <title>Login</title>
    <style>

        body {
            background-color: darksalmon;
        }

        .log_in_form {
            position: absolute;
            left: 40%;
            top: 33%;
            width: 20%;
            height: 34%;
            padding: 1vw;
            margin-bottom: 2%;
            border: 2px dashed lightgreen;
            border-radius: 10px;
        }

        #login_text {
            text-align: center;
            font-size: 1.6vw;
            height: 10%;
            vertical-align: middle;
        }

        #error_text {
            text-align: center;
            color: #f02323;
            margin-top: 2.5%;
            font-size: 1.3vw;
            height: 5%;
            vertical-align: middle;
        }

        #register_text {
            height: 10%;
            width: 100%;
            text-align: center;
        }

        .text {
            text-align: left;
            margin-top: 3%;
            margin-bottom: 1%;
            font-size: 1vw;
            height: 10%;
        }

        input {
            width: 100%;
            height: 5%;
            padding: 1.5% 3%;
            margin: 1.5% 0;
            box-sizing: border-box;
        }

        input:hover {
            background-color: beige;
        }


    </style>
</head>
<body>
<div class="log_in_form">
    <div id="login_text"> please log in</div>
    <% String error = (String) request.getAttribute(LoginServlet.ERROR_ATTR);
        if (error != null) {%>
    <div id="error_text"><%=error%>
    </div>
    <% }%>
    <form action="<c:url value="/login"/>" method="post">
        <div class="text">
            Username:
        </div>
        <div>
            <input type='text' placeholder='Enter Username:' name='username' autocomplete='username' required/>
        </div>
        <div class="text">
            Password:
        </div>
        <div>
            <input type='password' placeholder='Enter Password:' name='password' autocomplete='current-password'
                   required/>
        </div>
        <input type='submit' value='Login'/>
    </form>
    <div id="register_text">
        Not a member? <a href="<c:url value="/register"/>"> Register </a>
    </div>
</div>
</body>
</html>
