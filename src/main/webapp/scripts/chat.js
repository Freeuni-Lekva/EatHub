function sendMessage() {

    const text = document.getElementById("message").value;
    document.getElementById("message").value = "";

   const socketMessage = {
       username : null,
       roomID: null,
       content: text,
       type: "text"
   }

    socket.send(JSON.stringify(socketMessage));
}

function displayMessage(username, text) {

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

function displayConnectedUserMessage(username) {

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
