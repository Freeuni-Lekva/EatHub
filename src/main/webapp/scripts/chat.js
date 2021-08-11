const host = document.location.host;
let chatSocket;

function joinRoom(currentUser, roomID) {
    const request = new XMLHttpRequest();
    console.log(currentUser, roomID)
    //todo change with ajax
    request.open("POST", "http://" + host + "/join-room?room-id=" + roomID);
    // request.setRequestHeader("Content-Type", "application/json");
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            switch (request.status) {
                case 200:

                    let html = request.response;
                    console.log(request.getAllResponseHeaders());
                    let auth = request.getResponseHeader("Authorization")
                    console.log(auth);
                    if (!auth) {
                        document.getElementById("join-error").innerHTML =
                            "no auth";
                        break;
                    }
                    let token = auth.split("Basic ")[1];
                    console.log(token);
                    let dom = new DOMParser().parseFromString(html, 'text/html');
                    document.getElementsByTagName("html")[0].innerHTML =
                        dom.getElementsByTagName("html")[0].innerHTML;
                    joinChat(token, currentUser, roomID);
                    break;
                case 401:
                    document.getElementById("join-error").innerHTML =
                        "Oops... UNAUTHORIZED .";
                    break;
                case 403:
                    document.getElementById("join-error").innerHTML =
                        "Oops... These credentials are invalid.";
                    break;

                default:
                    document.getElementById("join-error").innerHTML =
                        "Oops... Looks like something is broken.";
            }
        }
    };
    request.send();
}

function joinChat(token, currentUser, roomID) {
    // const roomID = document.getElementById("roomID").value;
    openSocket(token, roomID, currentUser);
}

function openSocket(accessToken, roomID, currentUser) {
    if (chatSocket) {
        chatSocket.close();
    }
    chatSocket = new WebSocket("ws://" + host + "/chat?access-token=" + accessToken + "&room-id=" + roomID);
    chatSocket.onerror = function (ev) {
        console.log(ev.type);
    }
    chatSocket.onopen = function (event) {
        // window.open("http://" + host + "/chat.jsp")
        document.getElementById("title").innerHTML = "Room - " + roomID;
        // document.getElementById("join-room").style.display = "none";
        document.getElementById("contacts").style.display = "block";
        document.getElementById("chat").style.display = "block";
        document.getElementById("message").focus();
    };

    chatSocket.onmessage = function (event) {
        if (typeof event.data === "string") {
            const webSocketMessage = JSON.parse(event.data);
            console.log(webSocketMessage);
            switch (webSocketMessage.type) {
                case "welcome":
                    displayConnectedUserMessage(webSocketMessage.username, currentUser);
                    break;
                case "text":
                    displayMessage(webSocketMessage.username, webSocketMessage.content, currentUser,
                        webSocketMessage.sendTime);
                    break;
                case "image":
                    displayMessage(webSocketMessage.username, webSocketMessage.content, currentUser,
                        webSocketMessage.sendTime, true);
                    break;
                case "goodbye":
                    displayDisconnectedUserMessage(webSocketMessage.username);
                    break;
                case "active-users":
                    cleanAvailableUsers();
                    for (let i = 0; i < webSocketMessage.usernames.length; i++) {
                        addAvailableUsers(webSocketMessage.usernames[i]);
                    }
                    break;
            }
        }
    };
}

function sendMessage() {
    const text = document.getElementById("message").value.trim();
    if (text !== '') {
        document.getElementById("message").value = "";
        sendText(text)
    }
    const input = document.querySelector('input');
    if (input.files == null) {
        return;
    }
    let file = input.files[0];
    input.value = '';
    sendImage(file);
}

function sendText(text) {
    let socketMessage = {
        content: text,
        type: "text"
    }
    chatSocket.send(JSON.stringify(socketMessage));
}

function sendImage(file) {
    console.log(file)
    const reader = new FileReader();
    reader.onload = function (event) {
        console.log(event.target.result)
        let socketMessage = {
            content: event.target.result,
            type: "image"
        }
        chatSocket.send(JSON.stringify(socketMessage));
    }
    reader.readAsDataURL(file);
}

function displayMessage(username, cont, currentUser, sendTime, isText = false) {
    const sentByCurrentUser = currentUser === username;
    const message = document.createElement("div");
    message.setAttribute("class", sentByCurrentUser ? "message sent" : "message received");
    message.dataset.sender = username;
    const sender = document.createElement("span");
    sender.setAttribute("class", "sender");
    sender.appendChild(document.createTextNode(sentByCurrentUser ? "You" : username));
    message.appendChild(sender);
    const content = document.createElement("span");
    content.setAttribute("class", "content");
    if (isText) {
        const img = document.createElement("img");
        img.src = cont;
        img.alt = "Sent Image";
        content.appendChild(img);
    } else {
        content.appendChild(document.createTextNode(cont));
    }
    message.appendChild(content);
    const time = document.createElement("span");
    time.setAttribute("class", "time");
    time.appendChild(document.createTextNode(sendTime));
    const messages = document.getElementById("messages");
    const lastMessage = messages.lastChild;
    if (lastMessage && lastMessage.dataset.sender && lastMessage.dataset.sender === username) {
        lastMessage.lastChild.remove();
        message.className += " same-sender-previous-message";
    }
    message.appendChild(time);

    messages.appendChild(message);
    messages.scrollTop = messages.scrollHeight;
}

function displayConnectedUserMessage(username, currentUser) {
    const sentByCurrentUer = currentUser === username;
    const message = document.createElement("div");
    message.setAttribute("class", "message event");
    const text = sentByCurrentUer ? "Welcome " + username : username + " joined the chat";
    const content = document.createElement("span");
    content.setAttribute("class", "content");
    content.appendChild(document.createTextNode(text));
    message.appendChild(content);
    const messages = document.getElementById("messages");
    messages.appendChild(message);
}

function displayDisconnectedUserMessage(username) {
    const message = document.createElement("div");
    message.setAttribute("class", "message event");

    const text = username + " left the chat";
    const content = document.createElement("span");
    content.setAttribute("class", "content");
    content.appendChild(document.createTextNode(text));
    message.appendChild(content);

    const messages = document.getElementById("messages");
    messages.appendChild(message);
}

function addAvailableUsers(username) {
    const contact = document.createElement("div");
    contact.setAttribute("class", "contact");

    const status = document.createElement("div");
    status.setAttribute("class", "status");
    contact.appendChild(status);

    const content = document.createElement("span");
    content.setAttribute("class", "name");
    content.appendChild(document.createTextNode(username));
    contact.appendChild(content);

    const contacts = document.getElementById("contacts");
    contacts.appendChild(contact);
}

function cleanAvailableUsers() {
    const contacts = document.getElementById("contacts");
    while (contacts.hasChildNodes()) {
        contacts.removeChild(contacts.lastChild);
    }
}
