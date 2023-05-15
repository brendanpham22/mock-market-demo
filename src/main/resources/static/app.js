var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#products").show();
    }
    else {
        $("#products").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/products' , function (greeting) {
            let currentdate = new Date();
            let datetime = "Last Sync: " + currentdate.getDate() + "/"
                            + (currentdate.getMonth()+1)  + "/"
                            + currentdate.getFullYear() + " @ "
                            + currentdate.getHours() + ":"
                            + currentdate.getMinutes() + ":"
                            + currentdate.getSeconds();
            let object = JSON.parse(greeting.body);
            showGreeting(object,datetime);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/ws/products", {}, JSON.stringify({message:"Hello"}));
}

function showGreeting(message, datetime) {
    var content = '<tr><td>Name</td><td>Code</td><td>Price</td></tr>';
    for (var i = 0; i < message.length; i++) {
        content += '<tr id="' + message[i].code + '">';
        content += '<td class="bg-primary">' + message[i].name + '</td>';
        content += '<td class="bg-secondary">' + message[i].code + '</td>';
        content += '<td class="bg-danger">' + message[i].price + '</td>';
        content += '</tr>';
    }
    content += '<tr>Current time '+ datetime +'</tr>';
    $("#greetings").html(content);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});