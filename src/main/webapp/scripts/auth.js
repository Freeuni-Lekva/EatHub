let socket;
let currentUser;
const host = document.location.host;
function signIn() {
    const roomID = document.getElementById("roomID").value;

    const credentials = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
    };

    const request = new XMLHttpRequest();
    request.open("POST", "http://" + host + "/auth");
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {

        if (request.readyState === XMLHttpRequest.DONE) {

            switch (request.status) {

                case 200:
                    currentUser = credentials.username;
                    console.log(request.responseXML);
                    const webSocketAccessToken = JSON.parse(request.responseText);
                    openSocket(webSocketAccessToken.token, roomID);
                    break;

                case 403:
                    currentUser = null;
                    document.getElementById("authentication-error").innerHTML = "Oops... These credentials are invalid.";
                    break;

                default:
                    document.getElementById("authentication-error").innerHTML = "Oops... Looks like something is broken.";
            }
        }
    };

    request.send(JSON.stringify(credentials));
}

function openSocket(accessToken, roomID) {

    if (socket) {
        socket.close();
    }

    socket = new WebSocket("ws://" + host + "/chat?access-token=" + accessToken + "&room-id=" + roomID);

    socket.onopen = function (event) {
        // window.open("http://" + host + "/chat.html")
        document.getElementById("authentication").style.display = "none";
        document.getElementById("contacts").style.display = "block";
        document.getElementById("chat").style.display = "block";
        document.getElementById("message").focus();
    };

    socket.onmessage = function (event) {

        if (typeof event.data === "string") {

            const webSocketMessage = JSON.parse(event.data);
            console.log(webSocketMessage);
            switch (webSocketMessage.type) {

                case "welcome":
                    displayConnectedUserMessage(webSocketMessage.username);
                    break;

                case "text":
                    displayMessage(webSocketMessage.username, webSocketMessage.content);
                    break;

                case "goodbye":
                    displayDisconnectedUserMessage(webSocketMessage.username);
                    break;

                case "active-users":
                    cleanAvailableUsers();
                    for (var i = 0; i < webSocketMessage.usernames.length; i++) {
                        addAvailableUsers(webSocketMessage.usernames[i]);
                    }
                    break;
            }
        }
    };
}
