<!doctype html>
<html lang="en">
<head>
    <link type="text/css" rel="stylesheet" href="styles/common.css">
    <link type="text/css" rel="stylesheet" href="styles/chat.css">
    <link  type="text/css" rel="stylesheet" href="styles/auth.css">
    <script type="application/javascript" src="scripts/auth.js"></script>
    <script type="application/javascript" src="scripts/chat.js"></script>
    <meta charset="utf-8">
    <title>Chat</title>

</head>
<body>
<div id="title">Chat</div>
<form id="authentication" onsubmit="return false;">
    <input type="text" id="username" placeholder="Username" autofocus required/>
    <input type="password" id="password" placeholder="Password" required="required"/>
    <input type="number" id="roomID" placeholder="room ID"/>
    <button class="button" onclick="signIn(<%= request.getSession().getAttribute("roomID") %>)">Sign In</button>
    <span class="error" id="authentication-error"></span>
</form>
<div style="display: block" id="contacts"></div>
<div style="display: block" id="chat">
    <div id="messages" autofocus></div>
    <form id="chat-controls" onsubmit="return false;">
        <input type="text" id="message" placeholder="Enter a message" />
        <button class="button" onclick="sendMessage()">Send</button>
    </form>
</div>
</body>
</html>