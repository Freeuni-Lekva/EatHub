const host = document.location.host;
let chatSocket;
let savedMeals = [];
let flag = true;
let maxTime = "00:00:00";
let ended = false;

function joinRoom(currentUser, roomID) {
    const request = new XMLHttpRequest();
    console.log(currentUser, roomID)
    request.open("POST", "http://" + host + "/join-room?room-id=" + roomID);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            switch (request.status) {
                case 200:

                    let html = request.response;
                    let auth = request.getResponseHeader("Authorization")
                    if (!auth) {
                        document.getElementById("join-error").innerHTML =
                            "no auth";
                        break;
                    }
                    let token = auth.split("Basic ")[1];
                    let dom = new DOMParser().parseFromString(html, 'text/html');
                    document.getElementsByTagName("html")[0].innerHTML =
                        dom.getElementsByTagName("html")[0].innerHTML;
                    joinChat(token, currentUser, roomID);
                    // checkUpdates();
                    // if (!ended) {
                    //     getChosenMeals();
                    // }
                    display_time();
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
        document.getElementById("title").innerHTML = "Room - " + roomID;
        document.getElementById("message").focus();
    };

    chatSocket.onmessage = function (event) {

        if (typeof event.data === "string") {
            const webSocketMessage = JSON.parse(event.data);
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
                case "time-change":
                    displayTimeChangeMessage(webSocketMessage.username, webSocketMessage.content);
                    break;
                case "pay-for-all":
                    displayPayForAll(webSocketMessage.username, webSocketMessage.content);
                    break;
                case "split-bill":
                    displaySplitBillMessage(webSocketMessage.username, webSocketMessage.content);
                    break
                case "chosen-meals":
                    displayChosenMeals(webSocketMessage.content);
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

function displayPayForAll(username, content) {
    ended = true;
    disableControls();
    displayPaymentMessage(content);
}

function displaySplitBillMessage(username, content) {
    ended = true;
    disableControls();
    displayPaymentMessage(content);
}


function displayPaymentMessage(text) {
    const message = document.createElement("div");
    message.setAttribute("class", "message payment");
    const content = document.createElement("span");
    content.setAttribute("class", "content");
    content.appendChild(document.createTextNode(text));
    message.appendChild(content);
    const messages = document.getElementById("messages");
    messages.appendChild(message);
}

function disableControls() {
    document.getElementById("invitations").style.display = "none";
    document.getElementById("pay-buttons").style.display = "none";
    document.getElementById("meal-choose-button").style.display = "none";
    document.getElementById("meal-choose-button").disabled = true;
    document.getElementById("change-chosen-time").style.display = "none";

}

function sendMessage() {
    const text = document.getElementById("message").value.trim();
    if (text !== '') {
        document.getElementById("message").value = "";
        sendText(text)
    }
    const input = document.getElementById('file-img');

    if (!input.files[0]) {
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
    const reader = new FileReader();
    reader.onload = function (event) {
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

function displayTimeChangeMessage(username, time) {
    const message = document.createElement("div");
    message.setAttribute("class", "message change");
    const content = document.createElement("span");
    content.setAttribute("class", "content");
    content.appendChild(document.createTextNode(username + " changed time to \n" + time));
    message.appendChild(content);
    const messages = document.getElementById("messages");
    messages.appendChild(message);
    document.getElementById('date-time').value = time;
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
                    document.getElementById("invitation-error").innerHTML =
                        "";
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
                case 406:
                    document.getElementById("invitation-error").innerHTML =
                        "user - " + invitedUser + " is already invited";
                    break;
                default:
                    document.getElementById("invitation-error").innerHTML =
                        "some error";
            }
        }
    };
    request.send();

}

function chooseMeals() {
    let table = document.getElementById("menu-list");
    let numRows = table.rows.length;
    let i, tr;
    let cost = 0;
    const chosenMeals = [];
    for (i = 1; i < numRows; i++) {
        tr = table.rows[i].cells;
        let price = parseFloat(tr[2].innerHTML);
        let mealID = tr[3].firstElementChild.name;
        let amount = parseInt(tr[3].firstElementChild.value);
        chosenMeals.push({mealId: mealID, amount: amount});
        if (amount > 0) {
            cost = cost + (price * amount);
        }
    }
    document.getElementById("chosen-cost").innerHTML = cost.toString();
    const request = new XMLHttpRequest();
    request.open("POST", "http://" + host + "/ChooseMeal");
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            switch (request.status) {
                case 200:
                    sendUpdateChosenMealMessage(request.response)
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

function displayChosenMeals(content) {
    cleanChosenMeals();
    generateTable(JSON.parse(content));
}

function sendUpdateChosenMealMessage(content) {
    let socketMessage = {
        content: content,
        type: "chosen-meals"
    }

    chatSocket.send(JSON.stringify(socketMessage));
}

function cleanChosenMeals() {
    const table = document.getElementById("chosen-meals-table");
    while (table.childElementCount !== 1) {
        table.removeChild(table.lastChild);
    }
}


function generateTable(orderList) {

    const table = document.getElementById("chosen-meals-table");
    let numRows = orderList.length;
    let price = 0;
    let row;
    for (let i = 0; i < numRows; i++) {
        let order = orderList[i];
        row = document.createElement("tr");
        addToTableRow(row, document.createTextNode(order.username))

        addToTableRow(row, document.createTextNode(order.mealName))
        addToTableRow(row, document.createTextNode(order.amount))
        addToTableRow(row, document.createTextNode(order.cookingTime))
        addToTableRow(row, document.createTextNode(order.totalPrice))

        table.appendChild(row);
        if (order.cookingTime > maxTime) {
            maxTime = order.cookingTime;
        }
        price = price + order.totalPrice;
    }
    document.getElementById("chosen-meals-cost").innerHTML = price.toString();
    document.getElementById("estimated-time").innerHTML = maxTime;
}

function checkUpdates() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', "http://" + host + '/ChooseMeal');
    xhr.onload = function () {
        switch (xhr.status) {
            case 200:
                cleanChosenMeals();
                generateTable(JSON.parse(xhr.response));
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

// no need for polling
function getChosenMeals() {
    setInterval(function () {
        checkUpdates();
    }, 4000);
}

function searchMeal() {
    let table = document.getElementById("menu-list").getElementsByTagName('tbody')[0];
    if (flag) {
        while (table.hasChildNodes()) {
            if (!flag) {
                savedMeals.unshift(table.lastChild);
            }
            table.removeChild(table.lastChild);
            flag = !flag;
        }
        flag = false;
    }
    let value = document.getElementById("search-text").value;

    while (table.hasChildNodes()) {

        table.removeChild(table.lastChild);
    }

    let numRows = savedMeals.length;
    let i, tr;
    for (i = 0; i < numRows; i++) {
        tr = savedMeals[i].cells;
        if (tr[1].innerHTML.includes(value)) {
            table.appendChild(savedMeals[i]);
        }
    }
}

function addToTableRow(tr, child) {
    let td;
    td = document.createElement("td");
    td.appendChild(child)
    tr.appendChild(td);
}

function display_time() {
    if (ended) return;
    let refresh = 1000;
    setTimeout('update_time()', refresh);
}

function update_time() {
    let hours = parseInt(maxTime.substr(0, 2));
    let mins = parseInt(maxTime.substr(3, 5));

    let today = new Date(new Date().getTime() + ((hours * 60 + mins) * 60 * 1000));

    let dd = today.getDate();
    let mm = today.getMonth() + 1; //January is 0 so need to add 1 to make it 1!
    let yyyy = today.getFullYear();
    let hh = today.getHours();
    let MM = today.getMinutes();
    dd = addZero(dd);
    mm = addZero(mm);
    yyyy = addZero(yyyy);
    hh = addZero(hh);
    MM = addZero(MM);
    let currTime = yyyy + '-' + mm + '-' + dd + 'T' + hh + ":" + MM;

    let old_time = document.getElementById('date-time').value;
    if (old_time < currTime) {
        document.getElementById('date-time').value = currTime;
    }
    document.getElementById('date-time').min = currTime;

    display_time();
}

function addZero(time) {
    if (time < 10) {
        return '0' + time;
    }
    return time
}

function chooseTime() {
    let time = document.getElementById('date-time').value;
    let socketMessage = {
        content: time,
        type: "time-change"
    }

    chatSocket.send(JSON.stringify(socketMessage));
}

function sendSplitBill(username) {
    let socketMessage = {
        content: username + " split bill ",
        type: "split-bill"
    }

    chatSocket.send(JSON.stringify(socketMessage));
}


function splitBill() {
    const xhr = new XMLHttpRequest();
    xhr.open('PUT', "http://" + host + '/transaction?time=' + document.getElementById('date-time').value);
    xhr.onload = function () {
        switch (xhr.status) {
            case 200:
                console.log(xhr.response);
                sendSplitBill(xhr.response);
                document.getElementById("transaction-error").innerHTML = "";
                break;
            case 401:
                document.getElementById("transaction-error").innerHTML =
                    "unauthorized";
                break;
            case 403:
                document.getElementById("transaction-error").innerHTML =
                    "forbidden";
                break;
            case 404:
                document.getElementById("transaction-error").innerHTML =
                    " not found";
                break;
            case 412:
                document.getElementById("transaction-error").innerHTML =
                    " not enough money on balance ";
                break;
            default:
                document.getElementById("transaction-error").innerHTML =
                    "some error";
        }
    };
    xhr.send();
}

function sendPayForAll(username) {
    let socketMessage = {
        content: username + " payed",
        type: "pay-for-all"
    }

    chatSocket.send(JSON.stringify(socketMessage));
}

function payForAll() {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', "http://" + host + '/transaction?time=' + document.getElementById('date-time').value);
    xhr.onload = function () {
        switch (xhr.status) {
            case 200:
                console.log(xhr.response);
                sendPayForAll(xhr.response);
                document.getElementById("transaction-error").innerHTML = "";
                break;
            case 401:
                document.getElementById("transaction-error").innerHTML =
                    "unauthorized";
                break;
            case 403:
                document.getElementById("transaction-error").innerHTML =
                    "forbidden";
                break;
            case 404:
                document.getElementById("transaction-error").innerHTML =
                    "not found";
                break;
            case 412:
                document.getElementById("transaction-error").innerHTML =
                    " not enough money on balance ";
                break;
            default:
                document.getElementById("transaction-error").innerHTML =
                    "some error";
        }
    };
    xhr.send();
}