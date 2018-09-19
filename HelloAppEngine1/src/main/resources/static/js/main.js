'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var heroForm = document.querySelector('#heroForm');
var startForm = document.querySelector('#startForm');
var getCardForm = document.querySelector('#getCardForm');
var useHeroSkillForm = document.querySelector('#useHeroSkillForm');
var endTurnForm = document.querySelector('#endTurnForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var messageAreaServer = document.querySelector('#messageAreaServer');
var connectingElement = document.querySelector('.connecting');
var useCardBtn = document.querySelector('#useCardBtn');
var getCardBtn = document.querySelector('#getCardBtn');
var pickHeroBtn = document.querySelector('#pickHeroBtn');
var startGameBtn = document.querySelector('#startGameBtn');

var stompClient = null;
var username = null;
var character = null;
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}



function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onPublicTopicReceived);
    
    stompClient.subscribe('/user/queue/reply', onPrivateQueueReceived);

    // Tell your username to the server
    stompClient.send("/app/game.join",
        {},
        JSON.stringify({id: username, actionType: 'Join'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function getHero(event) {
	if(stompClient) {
        stompClient.send("/app/game.gethero", {});
	}
	event.preventDefault();
}

function startGame(event) {
	if(stompClient) {
        stompClient.send("/app/game.start", {});
	}
	event.preventDefault();
}
function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        stompClient.send("/app/game.execute", {}, messageInput.value);
        messageInput.value = '';
    }
    event.preventDefault();
}

function onPublicTopicReceived(payload) {
    var response = JSON.parse(payload.body);

    var messageElement = document.createElement('li');
    var textElement = document.createElement('p');
    var messageText = '';

    if(response.responseType === 'Join') {
        messageElement.classList.add('event-message');
        response.content = response.userName + ' joined!';
        messageText = document.createTextNode(response.content);
        
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        messageAreaServer.appendChild(messageElement);
        messageAreaServer.scrollTop = messageArea.scrollHeight;
        return;
    } else if (response.responseType === 'Leave') {
        messageElement.classList.add('event-message');
        response.content = response.userName + ' left!';
        messageText = document.createTextNode(response.content);
        
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        messageAreaServer.appendChild(messageElement);
        messageAreaServer.scrollTop = messageArea.scrollHeight;
        return;
    }  else if (response.responseType === 'Turn') {
    	if(username === response.userName){
    		useCardBtn.classList.remove('hidden');
    		// notify it's  turn for player 
    		console.log('Trigger time to use card');
    	} else {
    		useCardBtn.classList.add('hidden');
    	}
        return;
    } else if (response.responseType === 'Character') {
    	if(username === response.character.userName){
    		console.log('Inogre private character in public')
    	} else {
    		var infoCharacter = document.querySelector('#infoCharacter'+response.character.id);
            if(infoCharacter){
            	infoCharacter.value = JSON.stringify(response.character);
            } else {
            	console.log('ERRORRRRRRRRRRRRRRRRRRR!(onPublicTopicReceived)');
            }
    	}
        return;
    } else if (response.responseType === 'GetCard') {
    	if(username === response.userName){
    		getCardBtn.classList.remove('hidden');
    	} else {
    		getCardBtn.classList.add('hidden');
    	}
    } else if (response.responseType === 'UseCard') {
		getCardBtn.classList.add('hidden');
    }
    
    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode('$');
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor('Server');

    messageElement.appendChild(avatarElement);
    
    messageText = document.createTextNode(JSON.stringify(response));
    

    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageAreaServer.appendChild(messageElement);
    messageAreaServer.scrollTop = messageArea.scrollHeight;
}
function onPrivateQueueReceived(payload) {
	var response = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode('$');
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor('Server');

    messageElement.appendChild(avatarElement);

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(response.responseType + ' -:- ' +JSON.stringify(response));
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
    if (response.responseType === 'Character') {
    	if(username === response.character.userName){
	    	var infoCharacter = document.querySelector('#infoCharacter'+response.character.id);
	        if(infoCharacter){
	        	infoCharacter.value = JSON.stringify(response.character);
	        } else {
	        	console.log('ERRORRRRRRRRRRRRRRRRRRR!(onPrivateQueueReceived)');
	        }
    	}
    }
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
heroForm.addEventListener('submit', getHero, true)
startForm.addEventListener('submit', startGame, true)
getCardForm.addEventListener('submit', getCard, true)
function getCard(event) {
	if(stompClient) {
        stompClient.send("/app/game.getcard", {}, JSON.stringify({actionType: 'GetCard'}));
	}
	event.preventDefault();
}
useHeroSkillForm.addEventListener('submit', useHeroSkill, true)
function useHeroSkill(event) {
	if(stompClient) {
        stompClient.send("/app/game.useheroskill", {});
	}
	event.preventDefault();
}
endTurnForm.addEventListener('submit', endTurn, true)
function endTurn(event) {
	if(stompClient) {
        stompClient.send("/app/game.endturn", {});
	}
	event.preventDefault();
}
