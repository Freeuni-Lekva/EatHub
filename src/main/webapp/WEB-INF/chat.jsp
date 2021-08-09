<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <link type="text/css" rel="stylesheet" href="../styles/common.css">
    <link type="text/css" rel="stylesheet" href="../styles/chat.css">
    <script type="application/javascript" src="../scripts/chat.js"></script>
<div style="display: block" id="contacts"></div>
<div style="display: block" id="chat">
    <div id="messages" autofocus></div>
    <form id="chat-controls" onsubmit="return false;">
        <input type="text" id="message" placeholder="Enter a message"/>
        <button class="button" onclick="sendMessage()">Send</button>
    </form>
</div>
