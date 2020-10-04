var ws;
var stompClient = null;

var recentMessage="";

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    ws = new WebSocket('ws://localhost:8080/myhandler');
    ws.onmessage = function (data){
        showGreeting(JSON.parse(data.body).content);
    }
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            // console.log(JSON.parse(greeting.body).content.toLowerCase().includes("correct"));
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.send("/app/hello", {}, JSON.stringify({'name': "Bingo#123"}))
    });
}

function setDisconnect(){
    stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.send("/app/hello",{},JSON.stringify({'name': "END#123"}));
        setTimeout(
           setDisconnect ,1000
        )
    }

    // setConnected(false);
    // console.log("Disconnected");
}

function sendName() {
    if(recentMessage.toLowerCase().includes("instruction"))
        stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
    else {
        var res = "Pls wait for server to send instruction.";
        $("#greetings").append("<tr><td>" + res  + "</td></tr>");
    }
    ws.send(JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
    // console.log(message.toLowerCase().includes("congratulations"));
    // console.log(message.toLowerCase.includes("oops"));
    recentMessage = message;
    if (message.toLowerCase().includes("congratulations") || message.toLowerCase().includes("oops")){
        setTimeout(
            disconnect,5000
        );
    }


}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function(message) { sendName(); });
});

