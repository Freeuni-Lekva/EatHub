<%@ page import="ge.eathub.servlets.ConfirmServlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <title>Confirmation</title>
    <style>
        body {
            background-color: darksalmon;
        }

        #confirm_style {
            position: absolute;
            left: 35%;
            top: 43%;
            width: 30%;
            height: 14%;
            padding: 1vw;
            border: 2px dashed lightgreen;
            border-radius: 10px;
        }

        #text_style {
            text-align: center;
            font-size: 1.2vw;
            vertical-align: middle;
            margin-bottom: 0.7vw;
        }

        #input_style {
            width: 75%;
            padding: 1% 2%;
            margin: 1.5%;
            box-sizing: border-box;
        }

        #error_text {
            text-align: center;
            font-size: 1.1vw;
            color: #f02323;
        }

        #button_style {
            width: 17%;
            padding: 1% 2%;
            margin: 1.5%;
        }

        input:hover {
            background-color: lightgreen;
        }
    </style>
</head>
<body>
<div id="confirm_style">
    <form action="<c:url value="/confirm"/>" method="post">
        <div id="text_style">
            please confirm registration, token is sent on your email
        </div>
        <div>
            <input id="input_style" name="confirm-token" placeholder="confirmation token "/>
            <input id="button_style" type='submit' value='confirm'/>
        </div>
    </form>
    <%--    <% request.getSession().get  %>--%>
    <% String error = (String) request.getAttribute(ConfirmServlet.ERROR_ATTR);
        if (error != null) {%>
    <div id="error_text">
        <%=error%>
    </div>
    <% }%>
</div>
</body>
</html>
