'use strict';
var myapp = angular.module('myapp', []);
myapp.controller('FirstCtrl', function($scope, $timeout) {
	var socket = null;
	var stompClient = null;
	var characters = {};
	var dialogSelectUser = angular.element('#dialogSelectUser');
	var dialogSelectHero = angular.element('#dialogSelectHero');
	var dialogSelectCard = angular.element('#dialogSelectCard');
	var dialogSelectCardPC = angular.element('#dialogSelectCardPC');
	$scope.modeImage = true;
	$scope.dialogSelectCardTitle = '';
	$scope.dialogSelectCardActionType = '';
	$scope.dialogSelectCardActionStr = '';
	$scope.userName = '';
	$scope.userCanBeAffectList = [];
	$scope.selectedUser = '';
	$scope.selectedCard = '';
	$scope.selectedHero = null;
	$scope.oldCard = null;
	$scope.cardsInFront = [];
	$scope.cardsInHand = [];
	$scope.characters = [];
	$scope.role = null;
	$scope.heros = [];
	$scope.cards = [];
	$scope.characterTurn = '';
	$scope.playerUsingCard = '';
	$scope.playerGettingCard = '';
	$scope.playerDrawingCard = '';
	$scope.playerUsingBarrel = '';
	$scope.playerUsingSkill = false;
	$scope.actionType = '';
	$scope.usernamePage = true;
	$scope.waitingPage = false;
	$scope.mainPage = false;
	$scope.messages = [];
	$scope.notifyMessage = '';
	$scope.dialogTitle = '';
	$scope.useBarrelCardFunc = function(cardId) {
		stompClient.send("/app/game.execute", {}, JSON.stringify({
			id : cardId,
			actionType : 'UseBarrel'
		}));
		$scope.playerUsingBarrel = '';
	};
	$scope.getCardFunc = function() {
		stompClient.send("/app/game.execute", {}, JSON.stringify({
			actionType : 'GetCard'
		}));
		$scope.playerGettingCard = '';
		
	};
	$scope.drawFunc = function() {
		stompClient.send("/app/game.execute", {}, JSON.stringify({
			actionType : $scope.actionType
		}));
//		$scope.playerDrawingCard = '';
	};
	$scope.useHeroSkillFunc = function() {
		
	};
	$scope.endTurnFunc = function() {
		stompClient.send("/app/game.execute", {},
			JSON.stringify({
				actionType : 'EndTurn'
		}));
	};
	$scope.okUserDialogFunc = function() {
		stompClient.send("/app/game.execute", {}, JSON.stringify({
			id : $scope.selectedCard,
			actionType : 'UseCard',
			targetUser: $scope.selectedUser
		}));
	};
	$scope.lostLifePointFunc = function() {
		stompClient.send("/app/game.execute", {}, JSON.stringify({
			actionType : 'UseCard',
			noneResponse: true
		}));
	};
	$scope.canceUserDialoglFunc = function() {
	};
	
	
	$scope.heroPickFunc = function(heroId) {
		console.log(heroId);
		stompClient.send("/app/game.execute", {}, JSON.stringify({
			id : heroId,
			actionType : 'PickHero'
		}));
		$scope.dialogTitle = '';
	};
	$scope.pickCardToRemoveFunc = function(cardId) {
		if($scope.dialogSelectCardActionType === 'RemoveCard'){
			stompClient.send("/app/game.execute", {}, JSON.stringify({
				id : cardId,
				actionType : 'RemoveCard'
			}));
		} else if($scope.dialogSelectCardActionType === 'GeneralStore'){
			stompClient.send("/app/game.execute", {}, JSON.stringify({
				id : cardId,
				actionType : 'GeneralStore'
			}));
		}
	};
	$scope.pickCardToRemoveFuncPC = function(cardId) {
		stompClient.send("/app/game.execute", {}, JSON.stringify({
			id : cardId,
			actionType : $scope.dialogSelectCardActionType
		}));
	};
	$scope.pickCardToRemoveFuncPCRandom = function(cardId) {
		stompClient.send("/app/game.execute", {}, JSON.stringify({
			actionType : $scope.dialogSelectCardActionType
		}));
	};
	$scope.joinFunc = function() {
		if ($scope.userName) {
			console.log($scope.userName);
			$scope.notifyMessage = 'Waiting.............plz!';
			$scope.usernamePage = false;
			$scope.waitingPage = true;

			socket = new SockJS('/ws');
			stompClient = Stomp.over(socket);
			stompClient.connect({}, onConnected, onError);
		} else {
			console.log('Error');
		}
	};
	$scope.useCardFunc = function(cardId) {
		$scope.selectedCard = cardId;
		stompClient.send("/app/game.execute", {}, JSON.stringify({
			id : cardId,
			actionType : 'CheckCard'
		}));
		
	};
	function onError(error) {
		console.log(error);
	}
	function onConnected() {
		// ok
		stompClient.subscribe('/user/queue/join', onJoinQueueReceived);
		stompClient.subscribe('/user/queue/hero', onHeroQueueReceived);
		stompClient.subscribe('/user/queue/role', onRoleQueueReceived);
		stompClient.subscribe('/user/queue/character', onCharacterQueueReceived);
		stompClient.subscribe('/topic/character', onCharacterTopicReceived);
		stompClient.subscribe('/user/queue/checkcard', onCheckCardQueueReceived);
		stompClient.subscribe('/user/queue/removecardendturn', onRemoveCardBeforeEndTurnQueueReceived);
		stompClient.subscribe('/topic/removecardendturn', onRemoveCardBeforeEndTurnTopicReceived);
		stompClient.subscribe('/topic/turn', onTurnTopicReceived);
		stompClient.subscribe('/topic/usedCard', onUsedCardTopicReceived);
		stompClient.subscribe('/topic/cardaction', onCardActionTopicReceived);
		stompClient.subscribe('/topic/oldcard', onOldCardTopicReceived);
		stompClient.subscribe('/topic/action', onActionTopicReceived);
		stompClient.subscribe('/topic/server', onServerTopicReceived);
		
		// Tell your username to the server
		stompClient.send("/app/game.join", {}, JSON.stringify({
			id : $scope.userName,
			actionType : 'Join'
		}));
	}
	function addMessage(message){
		$scope.messages.unshift(message);
	}
	function onServerTopicReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'Gitf') {
			addMessage(response.userName + ' will receive after killing a FUORILEGGE');
		} else if (response.responseType === 'LoseCard'){
			addMessage(response.userName + ' will lose all his cards after killing a VICE');
			
		} else if (response.responseType === 'Winner'){
			addMessage(response.userName + ' win!!!!!');
			
			$scope.characterTurn = '';
			$scope.playerUsingCard = '';
			$scope.playerGettingCard = '';
			$scope.playerDrawingCard = '';
			$scope.playerUsingBarrel = '';
			$scope.playerUsingSkill = false;
			$scope.actionType = '';
		} else {
			console.log('ERROR');
			alert(JSON.stringify(response));
		}
		$scope.$apply();
	}
	function onCardActionTopicReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'GetCard') {
			addMessage(response.userName + ' will get card......');
			
			$scope.playerGettingCard = response.userName;
			$scope.playerUsingCard = '';
			$scope.playerDrawingCard = '';
			$scope.$apply();
		} else if (response.responseType === 'UseCard') {
			addMessage(response.userName + ' will use card......');
			
			$scope.playerUsingCard = response.userName;
			$scope.actionType = response.responseType;
			$scope.playerGettingCard = ''
			$scope.playerDrawingCard = '';
			$scope.$apply();
		}  else if (response.responseType === 'DrawCardJail') {
			addMessage(response.userName + ' will draw card for escaping the Jail......');
			
			$scope.playerDrawingCard = response.userName;
			$scope.actionType = response.responseType;
			$scope.playerUsingCard = '';
			$scope.playerGettingCard = '';
			$scope.$apply();
		}  else if (response.responseType === 'DrawCardDynamite') {
			addMessage(response.userName + ' will draw card for escaping dynamite......');
			
			$scope.playerDrawingCard = response.userName;
			$scope.actionType = response.responseType;
			$scope.playerUsingCard = '';
			$scope.playerGettingCard = '';
			$scope.$apply();
		} else {
			console.log('ERROR');
			alert(JSON.stringify(response));
		}
	}
	function onActionTopicReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'Bang' || response.responseType === 'Gatling' ||  response.responseType === 'Duello' ||  response.responseType === 'Indians') {
			$scope.actionType = response.responseType;
			$scope.playerUsingCard = response.userName;
			if(response.canUseBarrel && (response.responseType === 'Bang' || response.responseType === 'Gatling')){
				$scope.playerUsingBarrel = response.userName;
			} else {
				$scope.playerUsingBarrel = '';
			}
			$scope.$apply();
		} else if (response.responseType === 'Panic') {
			if ($scope.userName === response.userName) {
				$timeout(function() {
					$scope.playerUsingCard = '';
					$scope.cards = response.cards;
					$scope.dialogSelectCardTitle = 'get';
					$scope.dialogSelectCardActionType = response.responseType;
					$scope.dialogSelectCardActionStr = 'Get';
					dialogSelectCardPC.modal('show');
				}, 500);
			}
		} else if (response.responseType === 'CatPalou') {
			if ($scope.userName === response.userName) {
				$timeout(function() {
					$scope.playerUsingCard = '';
					$scope.cards = response.cards;
					$scope.dialogSelectCardTitle = 'remove';
					$scope.dialogSelectCardActionType = response.responseType;
					$scope.dialogSelectCardActionStr = 'Remove';
					dialogSelectCardPC.modal('show');
				}, 500);
			}
		} else if (response.responseType === 'GeneralStore') {
			if ($scope.userName === response.userName) {
				 $timeout(function() {
					 $scope.playerUsingCard = '';
					 $scope.cards = response.cards;
					 $scope.dialogSelectCardTitle = 'get';
					 $scope.dialogSelectCardActionType = response.responseType;
					 $scope.dialogSelectCardActionStr = 'Get';
					 $scope.actionType = response.responseType;
					 dialogSelectCard.modal('show');
					 }, 500);
			}
		} else {
			$scope.playerUsingBarrel = '';
			$scope.$apply();
			console.log('ERROR');
			alert(JSON.stringify(response));
		}
		
	}
	function onRemoveCardBeforeEndTurnQueueReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'RemoveCard') {
			$timeout(function() {
				$scope.cards = response.cards;
				$scope.dialogSelectCardTitle = 'remove';
				$scope.dialogSelectCardActionType = response.responseType;
				$scope.dialogSelectCardActionStr = 'Remove';
				$scope.playerUsingCard = response.userName;
				$scope.actionType = response.responseType;
				dialogSelectCard.modal('show');
			}, 500);
		} else {
			console.log('ERROR');
			alert(JSON.stringify(response));
		}
	}
	function onRemoveCardBeforeEndTurnTopicReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'RemoveCard') {
			addMessage(response.userName + ' has just removed card ' + response.cards[0].name + ' before ending turn!');
			
			$scope.$apply();
		} else {
			console.log('ERROR');
			alert(JSON.stringify(response));
		}
	}
	function onUsedCardTopicReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'UseCard') {
			var msg = response.userName;
			if(response.card){
				msg = msg + ' - ' + response.card.name;
				if(response.targetuser){
					msg = msg + ' - ' + response.targetuser;
				}
				
			} else {
				msg = msg + ' accept losing life point';
				
			}
			addMessage(msg);
			$scope.$apply();
		} else if (response.responseType === 'DrawCardJail') {
			addMessage(response.userName + ' - draw card ' + response.card.name + ' to escape the Jail');
			
			$scope.playerDrawingCard = '';
			$scope.$apply();
		} else if (response.responseType === 'DrawCardDynamite') {
			addMessage(response.userName + ' - draw card ' + response.card.name + ' to escape the Dynamite');
			
			$scope.playerDrawingCard = '';
			$scope.$apply();
		} else if (response.responseType === 'UseBarrel') {
			addMessage(response.userName + ' - draw card ' + response.card.name + ' to escape the bang/gatlling');
			
			$scope.playerDrawingCard = '';
			$scope.$apply();
		} else {
			console.log('ERROR');
			alert(JSON.stringify(response));
		}
	}
	function onOldCardTopicReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'OldCard') {
			$scope.oldCard = response.cards[0];
			$scope.$apply();
		}
	}
	
	function onCheckCardQueueReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'CheckCard') {
			if (response.canUse) {
				if (response.mustChooseTarget) {
					if (dialogSelectUser) {
						$timeout(function() {
							$scope.userCanBeAffectList = response.userCanBeAffectList;
							$scope.selectedUser = $scope.userCanBeAffectList[0];
							dialogSelectUser.modal('show');
						},200);
					} else {
						console.log('ERROR');
					}
				} else {
					stompClient.send("/app/game.execute", {},
						JSON.stringify({
							id : $scope.selectedCard,
							actionType : 'UseCard'
						}));
				}

			} else {
				console.log('Can not use card');
			}
		} else {
			console.log('ERROR');
			alert(JSON.stringify(response));
		}
	}
	function onJoinQueueReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'Unknown') {
			$scope.notifyMessage = 'UserName is existed. Plz input userName again. Tks!';
			$scope.$apply();
		} else if (response.responseType === 'Join') {
			$scope.notifyMessage = 'Joined successfully. Plz wait to start game.'
					+ response.userName;
			$scope.userName = response.userName;
			$scope.$apply();
		} else {
			console.log('ERROR');
			alert(JSON.stringify(response));
		}
		
	}
	function onHeroQueueReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'Hero') {
			$timeout(function() {
				$scope.heros =  response.heros;
				$scope.dialogTitle =  'Your role is '+ $scope.role.name +'. Please select hero...';
				dialogSelectHero.modal('show');
			},200);
		}
		
	}
	function onRoleQueueReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'Role') {
			$scope.role = response.role;
			$scope.$apply();
		}
		
	}
	function onCharacterQueueReceived(payload) {
		var response = JSON.parse(payload.body);
		if ($scope.userName === response.character.userName) {
			$scope.cardsInHand = response.character.cardsInHand;
			$scope.cardsInFront = response.character.cardsInFront;
			$scope.$apply();
		} else {
			console.log('Error');
			alert(JSON.stringify(response));
		}
		
	}
	function onTurnTopicReceived(payload) {
		var response = JSON.parse(payload.body);
		if (response.responseType === 'Turn') {
			addMessage(' Turn of '+ response.userName + ' is started! ');
			
			$scope.characterTurn = response.userName;
			$scope.$apply();
		} else if (response.responseType === 'EndTurn') {
			addMessage(' Turn of '+ response.userName + ' is finished! ');
			$scope.playerUsingCard = '';
			$scope.$apply();
		}  
		else {
			console.log('Error');
			alert(JSON.stringify(response));
		}
	}
	function onCharacterTopicReceived(payload) {
		$scope.waitingPage = false;
		$scope.mainPage = true;
		var response = JSON.parse(payload.body);
		var userName = response.character.userName;
		if (response.responseType === 'Character') {
			var update = false;
			var changedCharater = null;
			$scope.characters.forEach(function(character) {
				if(character.userName === userName){
					character.hero = response.character.hero;
					character.cardsInFront = response.character.cardsInFront;
					character.numCardsInHand = response.character.numCardsInHand;
					character.gun = response.character.gun;
					character.lifePoint = response.character.lifePoint;
					character.capacityLPoint = response.character.capacityLPoint;
					character.viewOthers = response.character.viewOthers;
					character.othersView = response.character.othersView;
					character.barrel = response.character.barrel;
					character.roleType = response.character.roleType;
					character.beJailed = response.character.beJailed;
					character.hasDynamite = response.character.hasDynamite;
					update = true;
					$scope.$apply();
				}
			});
			if(!update){
				$scope.characters.push(response.character);
				$scope.$apply();
			}
		} else if (response.responseType === 'Dead') {
			addMessage(response.userName + ' is dead!!!');
			
			$scope.$apply();
		} else {
			console.log('Error');
			alert(JSON.stringify(response));
		}
	};
});
