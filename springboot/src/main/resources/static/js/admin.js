'use strict';

var formStart = document.querySelector('#formStart');
var formConnect = document.querySelector('#formConnect');
var messageAreaServer = document.querySelector('#messageAreaServer');
var connectServerArea = document.querySelector('#connectServerArea');
var startArea = document.querySelector('#startArea');

var stompClient = null;
var count = 0;
var character = null;

var dialog = $("#dialog-confirm").dialog({
    resizable: false,
    autoOpen: false,
    modal: true,
    title: "Select ....",
    height: 250,
    width: 400,
    buttons: {
        "Ok": function () {
            $(this).dialog('close');
            callback(true);
        },
            "Cancel": function () {
            $(this).dialog('close');
            callback(false);
        }
    }
});

function callback(value) {
    if (value) {
        alert("Confirmed");
    } else {
        alert("Rejected");
    }
}

function startGame(event) {
	if (stompClient) {
		stompClient.send("/app/game.start", {});
	}
	startArea.classList.add('hidden');
	event.preventDefault();
}
function connectServer(event) {
	console.log(event);
	var socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);
	
	stompClient.connect({}, onConnected, onError);
	event.preventDefault();
}
function main() {
	connectServerArea.classList.remove('hidden');
//	startArea.classList.add('hidden');
}



function onConnected() {
	// Subscribe to the Public Topic
    stompClient.subscribe('/topic/join', onServerTopicReceived);
//    startArea.classList.remove('hidden');
    connectServerArea.classList.add('hidden');
}
function onError(error) {
	console.log(error);
}
function onServerTopicReceived(payload) {
    var response = JSON.parse(payload.body);

    var messageElement = document.createElement('li');
    messageElement.classList.add('list-group-item');

    if(response.responseType === 'Join') {
        messageElement.innerHTML=response.userName + ' joined!';
        messageAreaServer.appendChild(messageElement);
        messageAreaServer.scrollTop = messageAreaServer.scrollHeight;
        count ++;
        if(count === 4){
        	if (stompClient) {
        		stompClient.send("/app/game.start", {});
        	}
        }
    } else if (response.responseType === 'Leave') {
    	console.log('Not yet support');
    } else {
    	console.log('Error');
    	alert(JSON.stringify(response));
    }
}
function fnOpenNormalDialog() {
	dialog.dialog( "open" );
}



$('#btnOpenDialog').click(fnOpenNormalDialog);
formStart.addEventListener('submit', startGame, true);
formConnect.addEventListener('submit', connectServer, true);
main();



