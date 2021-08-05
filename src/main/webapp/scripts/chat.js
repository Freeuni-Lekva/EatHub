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
                        document.getElementById("authentication-error").innerHTML =
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
                case 403:
                    document.getElementById("authentication-error").innerHTML =
                        "Oops... These credentials are invalid.";
                    break;
                default:
                    document.getElementById("authentication-error").innerHTML =
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
        // document.getElementById("authentication-error").innerHTML = "Oops... These credentials are invalid.";

    }
    chatSocket.onopen = function (event) {
        // window.open("http://" + host + "/chat.jsp")
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
                    displayMessage(webSocketMessage.username, webSocketMessage.content, currentUser);
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
    const text = document.getElementById("message").value;
    if (text) {
        document.getElementById("message").value = "";
        const socketMessage = {
            username: null,
            roomID: null,
            content: text,
            type: "text"
        }
        chatSocket.send(JSON.stringify(socketMessage));
    }
}

function displayMessage(username, text, currentUser) {
    const sentByCurrentUer = currentUser === username;
    const message = document.createElement("div");
    message.setAttribute("class", sentByCurrentUer === true ? "message sent" : "message received");
    message.dataset.sender = username;
    const sender = document.createElement("span");
    sender.setAttribute("class", "sender");
    sender.appendChild(document.createTextNode(sentByCurrentUer === true ? "You" : username));
    message.appendChild(sender);
    const content = document.createElement("span");
    content.setAttribute("class", "content");
    content.appendChild(document.createTextNode(text));
    message.appendChild(content);
    const messages = document.getElementById("messages");
    const lastMessage = messages.lastChild;
    if (lastMessage && lastMessage.dataset.sender && lastMessage.dataset.sender === username) {
        message.className += " same-sender-previous-message";
    }
    messages.appendChild(message);
    messages.scrollTop = messages.scrollHeight;
}

function displayConnectedUserMessage(username, currentUser) {
    const sentByCurrentUer = currentUser === username;
    const message = document.createElement("div");
    message.setAttribute("class", "message event");
    const text = sentByCurrentUer === true ? "Welcome " + username : username + " joined the chat";
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
