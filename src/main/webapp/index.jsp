<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<html lang="en">
<head>
    <title>EatHub</title>
    <style>

        body {
            background-image: url("https://wallpapercave.com/wp/wp1929358.jpg");
            margin: 0;
        }

        #welcome_text {
            position: absolute;
            top: 10%;
            left: 20%;
            width: 60%;
            height: 20%;
            color: yellow;
            font-size: 4vw;
            text-align: center;
        }

        #buttons {
            position: absolute;
            left: 35%;
            top: 40%;
            width: 30%;
            height: 40%;
        }

        .button_class {
            border-radius: 10px;
            background-color: yellowgreen;
            width: 100%;
            height: 30%;
            font-size: 2vw;
        }

        #register_button {
            position: relative;
            height: 40%;
            float: bottom;
        }

        #login_button {
            margin-top: 10%;
            height: 40%;
            float: top;
        }

        .button_class:hover {
            background-color: chartreuse;
            font-size: 2.1vw;
        }


    </style>
</head>
<body>

<h1 id="welcome_text"> WELCOME TO EATHUB </h1>
<div id="buttons">
    <a href="/login">
        <button class="button_class" id="register_button"> Login</button>
    </a>
    <a href="/register">
        <button class="button_class" id="login_button"> Registration</button>
    </a>
</div>

</body>
</html>
