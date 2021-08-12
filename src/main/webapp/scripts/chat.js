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
                    checkUpdates();
                    getChosenMeals();
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
        // document.getElementById("contacts").style.display = "block";
        // document.getElementById("chat").style.display = "block";
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
                case "invitation":
                    displayInvitationMessage(webSocketMessage.content);
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
    const input = document.getElementById('file-img');
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

function displayInvitationMessage(text) {
    const message = document.createElement("div");
    message.setAttribute("class", "message invitation");
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

function invitationSent(byUser, invitedUser) {
    let socketMessage = {
        content: byUser + " invited " + invitedUser,
        type: "invitation"
    }
    chatSocket.send(JSON.stringify(socketMessage));
}

function sendInvitation(byUser) {
    const request = new XMLHttpRequest();
    const invitedUser = document.getElementById("invited-user").value.trim();
    request.open("POST", "http://" + host + "/invite?username=" + invitedUser);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            switch (request.status) {
                case 200:
                    console.log("user invited");
                    invitationSent(byUser, invitedUser);
                    break;
                case 401:
                    document.getElementById("invitation-error").innerHTML =
                        "unauthorized";
                    break;
                case 403:
                    document.getElementById("invitation-error").innerHTML =
                        "forbidden";
                    break;
                case 404:
                    document.getElementById("invitation-error").innerHTML =
                        "user - " + invitedUser + " not found";
                    break;
                default:
                    document.getElementById("invitation-error").innerHTML =
                        "some error";
            }
        }
    };
    request.send();


    // $("#invited-user").value = "blabla"
    // $.ajax({
    //     method: "POST",
    //     url: "http://" + host + "/invite",
    //     data: { name: "John", location: "Boston" }
    // })
    //     .done(function( msg ) {
    //         // $("#")
    //         alert( "Data Saved: " + msg );
    //     });
}

function chooseMeals() {
    let table = document.getElementById("menu-list");
    let numRows = table.rows.length;
    let i, tr;
    let cost = 0;
    const chosenMeals = [];
    for (i = 1; i < numRows; i++) {
        tr = table.rows[i].cells;
        let price = parseFloat(tr[1].innerHTML);
        let mealID = tr[2].firstElementChild.name;
        let amount = parseInt(tr[2].firstElementChild.value);
        chosenMeals.push({mealId: mealID, amount: amount});
        if (amount > 0) {
            cost = cost + (price * amount);
        }
    }
    console.log(chosenMeals)
    document.getElementById("chosen-cost").innerHTML = cost.toString();
    const request = new XMLHttpRequest();
    request.open("POST", "http://" + host + "/ChooseMeal");
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            switch (request.status) {
                case 200:
                    console.log("chose meals OK");
                    document.getElementById("choose-error").innerHTML = "";
                    break;
                case 401:
                    document.getElementById("choose-error").innerHTML =
                        "unauthorized";
                    break;
                case 403:
                    document.getElementById("choose-error").innerHTML =
                        "forbidden";
                    break;
                case 404:
                    document.getElementById("choose-error").innerHTML =
                        "not found";
                    break;
                default:
                    document.getElementById("choose-error").innerHTML =
                        "some error";
            }
        }
    };
    request.send(JSON.stringify(chosenMeals));
}

function cleanChosenMeals() {
    const table = document.getElementById("chosen-meals-table");
    console.log(table);
    while (table.childElementCount !== 1) {
        table.removeChild(table.lastChild);
    }
}


function generateTable(orderList) {

    const table = document.getElementById("chosen-meals-table");
    console.log(orderList);
    let numRows = orderList.length;
    let maxTime = "00:00:00";
    let price = 0;
    for( let i = 0; i < numRows; i++) {
        let order = orderList[i];
        console.log(order);
        let row = document.createElement("tr");
        let td = document.createElement('td');
        td.appendChild(document.createTextNode(order.username));
        row.appendChild(td);
        td = document.createElement('td');
        td.appendChild(document.createTextNode(order.mealName));
        row.appendChild(td);
        td = document.createElement('td');
        td.appendChild(document.createTextNode(order.amount));
        row.appendChild(td);
        td = document.createElement('td');
        td.appendChild(document.createTextNode(order.cookingTime));
        row.appendChild(td);
        td = document.createElement('td');
        td.appendChild(document.createTextNode(order.totalPrice));
        row.appendChild(td);
        table.appendChild(row);
        if (order.cookingTime > maxTime) {
            maxTime = order.cookingTime;
        }
        price = price + order.totalPrice;
    }
    document.getElementById("chosen-meals-cost").innerHTML = price;
    document.getElementById("estimated-time").innerHTML = maxTime;
}
function checkUpdates() {
    console.log("check");

    const xhr = new XMLHttpRequest();
    xhr.open('GET', "http://" + host + '/ChooseMeal');
    xhr.onload = function () {
        switch (xhr.status) {
            case 200:
                console.log("received");
                console.log(xhr.response);
                cleanChosenMeals();
                generateTable(JSON.parse(xhr.response))
                break;
            case 401:
                document.getElementById("choose-meals-error").innerHTML =
                    "unauthorized";
                break;
            case 403:
                document.getElementById("choose-meals-error").innerHTML =
                    "forbidden";
                break;
            case 404:
                document.getElementById("choose-meals-error").innerHTML =
                    " not found";
                break;
            default:
                document.getElementById("choose-meals-error").innerHTML =
                    "some error";
        }
    };
    xhr.send();
}

function getChosenMeals() {
    console.log("meeeeals");


    setInterval(function () {
        checkUpdates();
    }, 4000);

}