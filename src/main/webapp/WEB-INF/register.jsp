<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ge.eathub.servlets.RegistrationServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<head>
    <title>Register User</title>
    <style>
        body {
            background-color: darksalmon;
        }

        #form_style {
            position: absolute;
            left: 40%;
            top: 30%;
            width: 20%;
            height: 40%;
            padding: 1vw;
            border: 2px dashed lightgreen;
            border-radius: 10px;
        }

        #reg_text {
            text-align: center;
            font-size: 2vw;
            height: 10%;
        }

        #error_text {
            text-align: center;
            color: #f02323;
            margin-top: 2.5%;
            font-size: 1.2vw;
            height: 5%;
            vertical-align: middle;
        }

        .text_style {
            text-align: left;
            margin-top: 2%;
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
            background-color: lightgreen;
        }

        #log_in_text {
            text-align: center;
            font-size: 1vw;
        }

    </style>
</head>
<body>
<div id="form_style">
    <div id="reg_text"> Registration</div>
    <% String regError = (String) request.getAttribute(RegistrationServlet.ERROR_ATTR);
        if (regError != null) {%>
    <div id="error_text"><%=regError%>
    </div>
    <% }%>
    <form action="<c:url value="/register"/>" method="post">
        <div class="text_style">
            Username:
        </div>
        <input type='text' placeholder='Username:' name='username' autocomplete='username' required/>

        <div class="text_style">
            Password:
        </div>
        <input type='password' placeholder='Password:' name='password' autocomplete='new-password' required/>

        <div class="text_style">
            Email:
        </div>
        <input type='text' placeholder='email:' name='email' autocomplete='email' required/>
        <br>
        <input type='submit' value='register'/>
    </form>
    <div id="log_in_text">
        already have an account? <a href="<c:url value="/login"/>"> log in </a>
    </div>
</div>
</body>
</html>
