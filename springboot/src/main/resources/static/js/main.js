'use strict';
var myapp = angular.module('myapp', []);
myapp.controller('FirstCtrl',
	function($scope, $timeout, $interval) {
		var socket = null;
		var stompClient = null;
		var dialogInputUserName = angular.element('#dialogInputUserName');
		var userDialog = angular.element('#userDialog');
		var dialogSelectHero = angular.element('#dialogSelectHero');
		var dialogFullImage = angular.element('#dialogFullImage');
		//card: Panic - Cat Palou - General Store - Remove Card when ending turn
		var cardDialog = angular.element('#cardDialog');
		//skill
		var userSkillDialog = angular.element('#userSkillDialog');
		var cardSkillDialog = angular.element('#cardSkillDialog');
		var userCardSkillDialog = angular.element('#userCardSkillDialog');
		
		var wsUserQueue,wsGameQueue,wsGameTopic;
		
		$scope.countDown = 10;
		var messageServerPromise, countDownPromise, checkCardMessagePromise;
		$scope.actionStr = '';
		$scope.playerMap = null;
//		$scope.showCardInTurn = false;
		$scope.showCardNotInTurn = false;
		$scope.hallPage = true;
		$scope.gamePage = false;
		$scope.userName = '';
		$scope.host = false;
		$scope.endGame = false;
		$scope.matches = null;
		$scope.myInfo = {
			matchId: null,
			character: null,
			role: null
		};
		$scope.message = {
			show: false,
			style: '',
			msg: ''
		};
		$scope.cardInfo = {
			hasCardInHand: false,
			enableCancelBtn: true,
			enableOkBtn: true,
			titleBtnCard: '',
			titleDialog: '',
			cards: [],
			selectedCards: [],
			numberCard: 0
		};
		
		$scope.playerCountDown = '';
		$scope.skillInfo = {
			enableUseSkillBtn: false,
			enableCancelBtn: true,
			enableOkBtn: true,
			titleBtnCard: '',
			titleDialog: '',
			step: 1,
			players: [],
			selectedPlayer: '',
			cards: [],
			selectedCards: [],
			numberCard: 0,
			selectedCardMap: {},
			targetUser: ''
		}
		$scope.userCanBeAffectList = [];
		$scope.selectedUser = '';
		$scope.selectedCard = '';
		$scope.turnCard = null;
		$scope.playerUserCardInTurn = '';
		$scope.notTurnCards = [];
		$scope.playerUserCardNotInTurn = '';
		$scope.cardsInFront = [];
		$scope.cardsInHand = [];
		$scope.characters = [];
		$scope.tempCharacters = null;
		$scope.image = '';
		$scope.heros = [];
		$scope.cards = [];
		$scope.characterTurn = '';
		$scope.playerUsingCard = '';
		$scope.playerGettingCard = '';
		$scope.playerDrawingCard = '';
		$scope.playerUsingBarrel = '';
		$scope.playerUsingSkill = false;
		$scope.actionType = '';
		$scope.dialogTitle = '';
		$scope.chattingMsg = '';
		$scope.hasCardInHand = false;
		$scope.sendMessage = function(keyEvent) {
			 if (keyEvent.which === 13){
				 stompClient.send('/app/game.execute', {}, JSON
							.stringify({
								id : $scope.chattingMsg,
								actionType : 'Chatting'
							}));
				 $scope.chattingMsg = '';
			 }
		};
		$scope.useBarrelCardFunc = function(cardId) {
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						id : cardId,
						actionType : 'UseBarrel'
					}));
			$scope.playerUsingBarrel = '';
		};
		$scope.zoomImage = function(imageUrl) {
			$scope.image = imageUrl;
			dialogFullImage.modal({
				backdrop : 'static',
				keyboard : true,
				show : true
			});
		}
		$scope.userNameKeypressFunc = function(keyEvent) {
			
			if (keyEvent.which === 13){
				if ($scope.userName) {
					dialogInputUserName.modal('hide');
					socket = new SockJS('/ws');
					stompClient = Stomp.over(socket);
					stompClient.connect({}, onConnected, onError);
				} else {
					console.log('Error');
				}
			}
		}
		$scope.getCardFunc = function() {
			$scope.playerGettingCard = '';
			$scope.showCardNotInTurn = false;
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						actionType : 'GetCard'
					}));
		};
		$scope.drawFunc = function() {
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						actionType : $scope.actionType
					}));
		};
		$scope.useHeroSkillFunc = function() {
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						actionType : 'UseSkill',
						step: 1
					}));
		};
		$scope.endTurnFunc = function() {
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						actionType : 'EndTurn'
					}));
		};
		$scope.okUserDialogFunc = function() {
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						id : $scope.selectedCard,
						actionType : 'UseCard',
						targetUser : $scope.selectedUser
					}));
		};
		$scope.okUserSkillDialogFunc = function() {
			$interval.cancel(countDownPromise);
			$scope.playerCountDown = '';
			stompClient.send('/app/game.execute', {}, JSON
			.stringify({
				actionType : 'UseSkill',
				step:  $scope.skillInfo.step,
				others: {
					targetUser: $scope.skillInfo.selectedPlayer
				}
			}));
		};
		$scope.okUserCardSkillDialogFunc = function() {
			$interval.cancel(countDownPromise);
			$scope.playerCountDown = '';
			var cardPlayerMap = {};
			angular.forEach($scope.skillInfo.cards, function(card){
				cardPlayerMap[card.id] = card.assignedPlayer;
		    });
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						actionType : 'UseSkill',
						step:  $scope.skillInfo.step,
						others: cardPlayerMap
			}));
		};
		$scope.updateAssignedPlayerUserCardSkillDialogFunc = function() {
			var players = angular.copy($scope.skillInfo.players);
			var index;
			angular.forEach($scope.skillInfo.cards, function(card){
				index = players.indexOf(card.assignedPlayer);
				if (index > -1) {
					players.splice(index, 1);
				}
		    });
			if(players.length === 0){
				$scope.skillInfo.enableOkBtn = true;
			} else {
				$scope.skillInfo.enableOkBtn = false;
			}
		};
		$scope.pickCardDialogFunc = function(cardId) {
			if($scope.actionType === 'Panic' || $scope.actionType === 'CatPalou' || $scope.actionType === 'GeneralStore'){
				$interval.cancel(countDownPromise);
				cardDialog.modal('hide');
				stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						actionType : $scope.actionType,
						id : cardId
					}));
			} else if($scope.actionType === 'RemoveCardEndTurn'){ 
				angular.forEach($scope.cardInfo.cards, function(card){
					if(card.id === cardId){
						$scope.cardInfo.selectedCards.push(cardId);
						card.selected = true;
					}
				});
				if($scope.cardInfo.selectedCards.length >= $scope.cardInfo.numberCard){
					$interval.cancel(countDownPromise);
					cardDialog.modal('hide');
					stompClient.send('/app/game.execute', {}, JSON
						.stringify({
							actionType : $scope.actionType,
							others: {
								cards: $scope.cardInfo.selectedCards
							}
						}));
				}
			} else {
				console.log('Not yet handled.');
			}
			
		};
		$scope.pickCardSkillDialogFunc = function(cardId) {
			if($scope.myInfo.character.hero.name === 'PatBrennan' || $scope.myInfo.character.hero.name === 'KitCarlson' || $scope.myInfo.character.hero.name === 'LuckyDuke' || $scope.myInfo.character.hero.name === 'UncleWill' || $scope.myInfo.character.hero.name === 'JoseDelgado'){
				$scope.skillInfo.selectedCards.push(cardId);
				$interval.cancel(countDownPromise);
				$scope.playerCountDown = '';
				cardSkillDialog.modal('hide');
				stompClient.send('/app/game.execute', {}, JSON
						.stringify({
							actionType : 'UseSkill',
							step:  $scope.skillInfo.step,
							others: {
								targetUser: $scope.skillInfo.selectedPlayer,
								cards: $scope.skillInfo.selectedCards
							}
						}));
			} else if($scope.myInfo.character.hero.name === 'SidKetchum' || $scope.myInfo.character.hero.name === 'DocHolyday'){ 
				angular.forEach($scope.skillInfo.cards, function(card){
					if(card.id === cardId){
						$scope.skillInfo.selectedCards.push(cardId);
						card.selected = true;
					}
			    });
				if($scope.skillInfo.selectedCards.length >= $scope.skillInfo.numberCard){
					$interval.cancel(countDownPromise);
					$scope.playerCountDown = '';
					cardSkillDialog.modal('hide');
					stompClient.send('/app/game.execute', {}, JSON
						.stringify({
							actionType : 'UseSkill',
							step:  $scope.skillInfo.step,
							others: {
								targetUser: $scope.skillInfo.selectedPlayer,
								cards: $scope.skillInfo.selectedCards
							}
					}));
				}
				
			} else {
				console.log('Not yet handled.');
			}
			
		};
		$scope.okCardSkillDialogFunc = function() {
			console.log('TODO');
		};
		$scope.lostLifePointFunc = function() {
			$interval.cancel(countDownPromise);
			$scope.playerCountDown = '';
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						actionType : 'UseCard',
						noneResponse : true
					}));
			$scope.playerUsingCard = '';
		};
		$scope.canceUserDialoglFunc = function() {
		};

		$scope.heroPickFunc = function(heroId) {
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						id : heroId,
						actionType : 'PickHero'
					}));
			$scope.dialogTitle = '';
		};
		$scope.pickRandomCardDialogFunc = function(cardId) {
			$interval.cancel(countDownPromise);
			cardDialog.modal('hide');
			stompClient.send('/app/game.execute',{},
				JSON.stringify({
					actionType : $scope.actionType
				}));
		};
		$scope.useCardFunc = function(cardId) {
			$scope.selectedCard = cardId;
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						id : cardId,
						actionType : 'CheckCard'
					}));

		};
		
		$scope.createUserFunc = function() {
			if ($scope.userName) {
				socket = new SockJS('/ws');
				stompClient = Stomp.over(socket);
				stompClient.connect({}, onConnected, onError);
			} else {
				console.log('Error');
			}
		};
		$scope.joinMatchFunc = function(matchId) {
			stompClient.send('/app/game.join', {}, 
					JSON.stringify({
						id : matchId,
						actionType : 'Join'
					}));
		};
		$scope.createNewMatchFunc = function(matchId) {
			stompClient.send('/app/game.create', {});
		};
		$scope.startGameFunc = function() {
			stompClient.send('/app/game.start', {});
		};
		$scope.refreshMatchFunc = function() {
			stompClient.send('/app/game.get', {});
		};
		$scope.hideRole = function(character) {
			if($scope.userName === character.userName){
				if(character.roleImage !== '/data/image/role/Sceriffo.jpg' && character.lifePoint > 1 && !$scope.endGame){
					character.roleImage = '/data/image/role/Anonymous.jpg';
				}
			}
		};
		$scope.showRole = function(character) {
			if($scope.userName === character.userName && $scope.myInfo.role){
				character.roleImage = $scope.myInfo.role.image;
			}
		};
		
		function countDownFunc(){
            if( $scope.countDown > 0 ) {
            	$scope.countDown -=1;
            } else {
            	$interval.cancel(countDownPromise);
            	if($scope.playerCountDown === $scope.userName){
            		if($scope.actionType === 'Skill'){
            			$scope.playerCountDown = '';
                    	userSkillDialog.modal('hide');
                    	userCardSkillDialog.modal('hide');
                    	cardSkillDialog.modal('hide');
                    	stompClient.send('/app/game.execute', {}, JSON
        	    			.stringify({
        	    				actionType : 'UseSkill',
        	    				step:  $scope.skillInfo.step
        	    			}));
            		} 
            		else if($scope.actionType === 'GetCard'){
            			$scope.getCardFunc();
            		}
            		else if($scope.actionType === 'UseCard'){
            			userDialog.modal('hide');
            			stompClient.send('/app/game.execute', {}, JSON
            					.stringify({
            						actionType : 'EndTurn'
            					}));
            			$scope.playerGettingCard = '';
            		}
            		else if($scope.actionType === 'DrawCardDynamite' || $scope.actionType === 'DrawCardJail'){
            			$scope.drawFunc();
            		}
            		else if($scope.actionType === 'CatPalou' || $scope.actionType === 'Panic'){
            			$scope.pickRandomCardDialogFunc();
            		}
            		else if($scope.actionType === 'GeneralStore'){
        				cardDialog.modal('hide');
        				stompClient.send('/app/game.execute', {}, JSON
        					.stringify({
        						actionType : $scope.actionType,
        						id : $scope.cardInfo.cards[0].id
        					}));
            		}
            		else if($scope.actionType === 'RemoveCardEndTurn'){
        				cardDialog.modal('hide');
        				stompClient.send('/app/game.execute', {}, JSON
        					.stringify({
        						actionType : $scope.actionType
        					}));
            		}
            		else if($scope.actionType === 'Bang' 
            			|| $scope.actionType === 'Gatling'
    					|| $scope.actionType=== 'Duello'
						|| $scope.actionType === 'Indians'){
            			$scope.lostLifePointFunc();
            		}
            		else {
            			console.log('Something is error!');
            		}
            		
            	}
            	
            }
        }
		function callCountDownFunc(response){
			$interval.cancel(countDownPromise);
			$scope.countDown = response.countDown;
			$scope.playerCountDown = response.userName;
			countDownPromise = $interval(countDownFunc, 1000);
		}
		function callMessageServerFunc(message, time, type){
			$scope.message.msg = message;
			$scope.message.show = true; 
			$timeout.cancel(messageServerPromise);
			var time = (time) ? time : 5000;
			messageServerPromise = $timeout(function() {
						$scope.message.show = false; 
			}, time);
		}
		
		function callCheckCardMessageFunc(message){
			var checkCardMessage = document.querySelector('#checkCardMessage');
			$timeout.cancel(checkCardMessagePromise);
			if(checkCardMessage) {
				checkCardMessage.remove();
			}
			var yourCardHeader = document.querySelector('#yourCardHeader');
			checkCardMessage = document.createElement('div');
			checkCardMessage.id = 'checkCardMessage';
			checkCardMessage.classList.add('fadeInDown');
			checkCardMessage.classList.add('animated');
			checkCardMessage.classList.add('check-card-message');
			checkCardMessage.innerHTML = message;
			yourCardHeader.appendChild(checkCardMessage);
			checkCardMessagePromise = $timeout(function() {
				var checkCardMessage = document.querySelector('#checkCardMessage');
				if(checkCardMessage) {
					checkCardMessage.remove();
				}
			}, 2000);
		}
		function initWSUser(){
			wsUserQueue = stompClient.subscribe('/user/queue/user', onUserQueueReceived);
			wsGameQueue = stompClient.subscribe('/user/queue/game', onGameQueueReceived);
			wsGameTopic = stompClient.subscribe('/topic/game', onGameTopicReceived);
		} 
		
		function destroyWSUser(){
			if(wsUserQueue){
				wsUserQueue.unsubscribe();
			}
			if(wsGameQueue){
				wsGameQueue.unsubscribe();
			}
			if(wsGameTopic){
				wsGameTopic.unsubscribe();
			}
		} 
		function onConnected() {
			initWSUser();
			
			$scope.userName = $scope.userName.replace(/ /g,'');
			// Tell your username to the server
			stompClient.send('/app/user.create', {}, JSON.stringify({
				id : $scope.userName,
				actionType : 'Create'
			}));
		}
		
		function onGameTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Read') {
				$scope.matches = response.matches;
				$scope.$apply();
			} else if (response.responseType === 'Update') {
				stompClient.send('/app/game.get', {});
			}
		}
		function onGameQueueReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Player') {
				$scope.myInfo.matchId = response.matchId;
				$scope.gamePage = true;
				$scope.hallPage = false;
				$scope.host = response.host;
				destroyWSUser();
				//subscribe
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/role', onRoleReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/player', onPlayerReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/hero', onHeroReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/server', onServerReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/skill', onSkillTopicReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/character', onCharacterQueueReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/skill', onSkillQueueReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/countdown', onCountDownTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/character', onCharacterTopicReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/checkcard',onCheckCardQueueReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/removecard',onRemoveCardBeforeEndTurnQueueReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/turn',onTurnTopicReceived);
//				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/usedCard',onUsedCardTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/usedCardInTurn',onUsedCardInTurnTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/usedCardNotInTurn',onUsedCardNotInTurnTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/cardaction',onCardActionTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/oldcard',onOldCardTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/action',onActionTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/chatting',onChattingTopicReceived);
				//
				
				$scope.characters.length = 0;
				angular.forEach(response.playerMap, function(value, key) {
					$scope.characters.push(value);
				});
				$scope.myInfo.character = $scope.characters[0];
				
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
			$scope.$apply();
		}
		function onUserQueueReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Create') {
				$scope.userName = response.userName;
				stompClient.send('/app/game.get', {});
			} else {
				console.log('ERROR: ' + response.responseType);
				console.log(JSON.stringify(response));
				$timeout(
					function() {
						dialogInputUserName.modal({backdrop:'static',keyboard:true,show:true});
					}, 500);
			}
			$scope.$apply();
		}
		function onRoleReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Role') {
				$scope.myInfo.role = response.role;
				$scope.host = false;
				$scope.$apply();
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
		}
		function onPlayerReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Player') {
				$scope.characters.length = 0;
				angular.forEach(response.playerMap, function(value, key) {
					$scope.characters.push(value);
				});
				$scope.$apply();
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
		}
		
		
		function onHeroReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Hero') {
				$timeout(function() {
					$scope.heros = response.heros;
					$scope.dialogTitle = 'Your role is '
							+ $scope.myInfo.role.name
							+ '. Please select hero...';
					dialogSelectHero.modal({backdrop:'static',keyboard:true,show:true});
				}, 200);
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
		}
		
		function onCharacterQueueReceived(payload) {
			var response = JSON.parse(payload.body);
			if ($scope.userName === response.character.userName) {
				$scope.cardsInHand = response.character.cardsInHand;
				$scope.cardsInFront = response.character.cardsInFront;
				$scope.myInfo.character.hero = response.character.hero;
				$scope.$apply();
			} else {
				console.log('Error');
				alert(JSON.stringify(response));
			}
		}
		function onCountDownTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'CountDownStart') {
				$scope.actionStr = 'Using skill...';
				//callCountDownFunc(response);
				$scope.$apply();
			} 
			else if (response.responseType === 'CountDownEnd') {
				console.log('Do nothing');
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
				var changedCharater = null;
				$scope.characters.forEach(function(character) {
					if (character.userName === userName) {
						character.hero = response.character.hero;
						character.cardsInFront = response.character.cardsInFront;
						character.numCardsInHand = response.character.numCardsInHand;
						character.gun = response.character.gun;
						character.lifePoint = response.character.lifePoint;
						character.capacityLPoint = response.character.capacityLPoint;
						character.viewOthers = response.character.viewOthers;
						character.othersView = response.character.othersView;
						character.barrel = response.character.barrel;
						character.roleImage = response.character.roleImage;
						character.beJailed = response.character.beJailed;
						character.hasDynamite = response.character.hasDynamite;
						$scope.$apply();
					}
				});
			} else if (response.responseType === 'Dead') {
				addAlertMessage('danger', 'is dead', response.userName, 10000);
				$scope.characters.forEach(function(character) {
					if (character.userName === userName) {
						character.hero = response.character.hero;
						character.cardsInFront = response.character.cardsInFront;
						character.numCardsInHand = response.character.numCardsInHand;
						character.gun = response.character.gun;
						character.lifePoint = response.character.lifePoint;
						character.capacityLPoint = response.character.capacityLPoint;
						character.viewOthers = response.character.viewOthers;
						character.othersView = response.character.othersView;
						character.barrel = response.character.barrel;
						character.roleImage = response.character.roleImage;
						character.beJailed = response.character.beJailed;
						character.hasDynamite = response.character.hasDynamite;
						$scope.$apply();
					}
				});
				$scope.$apply();
			} else {
				
				console.log('Error');
				alert(JSON.stringify(response));
			}
		}
		function onCheckCardQueueReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'CheckCard') {
				if (response.canUse) {
					if (response.mustChooseTarget) {
						if (userDialog) {
							$timeout(
									function() {
										$scope.userCanBeAffectList = response.userCanBeAffectList;
										$scope.selectedUser = $scope.userCanBeAffectList[0];
										userDialog.modal({backdrop:'static',keyboard:true,show:true});
									}, 200);
						} else {
							console.log('ERROR');
						}
					} else {
						$interval.cancel(countDownPromise);
						$scope.playerCountDown = '';
						stompClient.send('/app/game.execute', {},
								JSON.stringify({
									id : $scope.selectedCard,
									actionType : 'UseCard'
								}));
					}

				} else {
					console.log('Can not use card');
					if(response.message){
						callCheckCardMessageFunc(response.message);
					}
				}
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
		}
		function onRemoveCardBeforeEndTurnQueueReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'RemoveCardEndTurn') {
				
				$scope.actionType = response.responseType;
				$scope.cardInfo.cards = response.cards;
				angular.forEach($scope.skillInfo.cards, function(card){
					card.selected = false;
			    });
				$scope.cardInfo.titleBtnCard = 'Remove';
				$scope.cardInfo.hasCardInHand = false;
				$scope.cardInfo.enableOkBtn = false;
				$scope.cardInfo.enableCancelBtn = false;
				$scope.cardInfo.selectedCards.length = 0;
				$scope.cardInfo.numberCard = response.numberCard;
				$scope.cardInfo.titleDialog = 'You must remove ' + response.numberCard + ' card(s) before ending turn.';
				$timeout(function() {
					cardDialog.modal({backdrop:'static',keyboard:true,show:true});
				}, 500);
				/// count down time
				//callCountDownFunc(response);
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
		}
		
		function onUsedCardInTurnTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'UseCardInTurn') {
				$scope.playerUserCardInTurn = response.userName;
				$scope.turnCard = response.card;
				$scope.showCardNotInTurn = false;
			} else {
				console.log('ERROR');
				console.log(JSON.stringify(response));
			}
			$scope.$apply();
		}
		function onUsedCardNotInTurnTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'UseCardNotInTurn') {
				$scope.playerUserCardNotInTurn = response.userName;
				$scope.notTurnCards = response.cards;
				$scope.showCardNotInTurn = true;
//				$scope.showCardInTurn = false;
			} else {
				console.log('ERROR');
				console.log(JSON.stringify(response));
			}
			$scope.$apply();
		}
		function onOldCardTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'OldCard') {
				if(response.cards.length > 0){
					$scope.playerUserCardInTurn = 'OldCard';
					$scope.turnCard = response.cards[0];
					if($scope.actionType === 'RemoveCardEndTurn'){
						$scope.showCardNotInTurn = false;
					}
					$scope.$apply();
				}
			} else {
				console.log('Error');
				alert(JSON.stringify(response));
			}
		}
		function onChattingTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Chatting') {
				addChattingMessage('info', response.message, response.userName);
			} else {
				console.log('Error');
				alert(JSON.stringify(response));
			}
		}
		function onTurnTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Turn') {
				console.log(' Turn of ' + response.userName
						+ ' is started! ');

				$scope.characterTurn = response.userName;
				dialogFullImage.modal('hide');
				userDialog.modal('hide');
				cardDialog.modal('hide');
				userSkillDialog.modal('hide');
				cardSkillDialog.modal('hide');
				userCardSkillDialog.modal('hide');
				
				$scope.$apply();
			} else if (response.responseType === 'EndTurn') {
				console.log(' Turn of ' + response.userName
						+ ' is finished! ');
				$scope.playerDrawingCard = '';
				$scope.playerUsingCard = '';
				$scope.playerGettingCard = '';
				$scope.characterTurn = '';
				$scope.$apply();
			} else {
				console.log('Error');
				alert(JSON.stringify(response));
			}
		}
		function onCardActionTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'GetCard') {
				if($scope.userName === response.userName){
					$scope.skillInfo.enableUseSkillBtn = true;
				}
				if($scope.actionType === 'DrawCardDynamite' || $scope.actionType === 'DrawCardJail'){
					$scope.showCardNotInTurn = true;
				} else {
					$scope.showCardNotInTurn = false;
				}
				
				$scope.actionType = response.responseType;
				$scope.playerGettingCard = response.userName;
				$scope.playerUsingCard = '';
				$scope.playerDrawingCard = '';
				$scope.actionStr = 'Getting card...';
				/// count down time
  				//callCountDownFunc(response);
				///
				$scope.$apply();
			} else if (response.responseType === 'UseCard') {
				if($scope.userName === response.userName){
					$scope.skillInfo.enableUseSkillBtn = true;
				}
				if(!$scope.actionType === 'UseCard'){
					$scope.showCardNotInTurn = false;
				}
				$scope.playerUsingCard = response.userName;
				$scope.actionType = response.responseType;
				$scope.playerGettingCard = ''
				$scope.playerDrawingCard = '';
				$scope.actionStr = 'Using card...';
				/// count down time
  				//callCountDownFunc(response);
				
				$scope.$apply();
			} else if (response.responseType === 'DrawCardJail') {

				$scope.playerDrawingCard = response.userName;
				$scope.actionType = response.responseType;
				$scope.playerUsingCard = '';
				$scope.playerGettingCard = '';
				$scope.actionStr = 'Drawing Jail...';
				
				/// count down time
  				//callCountDownFunc(response);
				
				$scope.$apply();
			} else if (response.responseType === 'DrawCardDynamite') {
				$scope.playerDrawingCard = response.userName;
				$scope.actionType = response.responseType;
				$scope.playerUsingCard = '';
				$scope.playerGettingCard = '';
				$scope.actionStr = 'Drawing Dynamite...';
				/// count down time
  				//callCountDownFunc(response);
				
				$scope.$apply();
			} else if (response.responseType === 'RemoveCardEndTurn') {
				$scope.actionType = response.responseType;
				$scope.playerDrawingCard = '';
				$scope.playerUsingCard = '';
				$scope.playerGettingCard = '';
				$scope.actionStr = 'Ending turn...';
				
				/// count down time
  				//callCountDownFunc(response);
				
				$scope.$apply();
			} else {
				//
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
		}
		function onActionTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			$scope.skillInfo.enableUseSkillBtn = false;
			$scope.playerUsingBarrel = '';
			if (response.responseType === 'Bang'
					|| response.responseType === 'Gatling'
					|| response.responseType === 'Duello'
					|| response.responseType === 'Indians') {
				$scope.actionType = response.responseType;
				$scope.actionStr = 'Using card...';
				$scope.playerUsingCard = response.userName;
				if (response.canUseBarrel
						&& (response.responseType === 'Bang' || response.responseType === 'Gatling')) {
					$scope.playerUsingBarrel = response.userName;
				}
				if(response.responseType === 'Bang'){
					$scope.actionStr = 'Be shot...';
				} else 
				if(response.responseType === 'Gatling'){
					$scope.actionStr = 'Be shot...';
				} else 
				if(response.responseType === 'Indians'){
					$scope.actionStr = 'Bang Indians...';
				} else 
				if(response.responseType === 'Duello'){
					$scope.actionStr = 'Duelling...';
				}
				/// count down time
				//callCountDownFunc(response);
				$scope.$apply();
			} else if (response.responseType === 'Panic') {
				$scope.actionType = response.responseType;
				$scope.playerUsingCard = response.userName;
				$scope.actionStr = 'Selecting card...';
				if ($scope.userName === response.userName) {
					$scope.cardInfo.cards = response.cards;
					$scope.cardInfo.titleBtnCard = 'Get';
					$scope.cardInfo.hasCardInHand = response.hasCardInHand;
					$scope.cardInfo.enableOkBtn = false;
					$scope.cardInfo.enableCancelBtn = false;
					$scope.cardInfo.selectedCards.length = 0;
					$scope.cardInfo.numberCard = 1;
					$scope.cardInfo.titleDialog = 'Get 1 card from ' + response.targetUser;
					$timeout(function() {
						cardDialog.modal({backdrop:'static',keyboard:true,show:true});
					}, 500);
				}
				/// count down time
				//callCountDownFunc(response);
			} else if (response.responseType === 'CatPalou') {
				$scope.playerUsingCard = response.userName;
				$scope.actionStr = 'Selecting card...';
				$scope.actionType = response.responseType;
				if ($scope.userName === response.userName) {
					$scope.cardInfo.cards = response.cards;
					$scope.cardInfo.titleBtnCard = 'Remove';
					$scope.cardInfo.hasCardInHand = response.hasCardInHand;
					$scope.cardInfo.enableOkBtn = false;
					$scope.cardInfo.enableCancelBtn = false;
					$scope.cardInfo.selectedCards.length = 0;
					$scope.cardInfo.numberCard = 1;
					$scope.cardInfo.titleDialog = 'Remove 1 card from ' + response.targetUser;
					$timeout(function() {
						cardDialog.modal({backdrop:'static',keyboard:true,show:true});
					}, 500);
				}
				/// count down time
				//callCountDownFunc(response);
			} else if (response.responseType === 'GeneralStore') {
				$scope.playerUsingCard = response.userName;
				$scope.actionStr = 'Getting card...';
				$scope.actionType = response.responseType;
				if ($scope.userName === response.userName) {
					$scope.cardInfo.cards = response.cards;
					$scope.cardInfo.titleBtnCard = 'Get';
					$scope.cardInfo.hasCardInHand = false;
					$scope.cardInfo.enableOkBtn = false;
					$scope.cardInfo.enableCancelBtn = false;
					$scope.cardInfo.selectedCards.length = 0;
					$scope.cardInfo.numberCard = 1;
					$scope.cardInfo.titleDialog = 'General Store! Get 1 card. ';
					$timeout(function() {
						cardDialog.modal({backdrop:'static',keyboard:true,show:true});
					}, 500);
				}
				/// count down time
				//callCountDownFunc(response);
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
			$scope.$apply();

		}
		function onSkillQueueReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Skill') {
				$scope.actionType = response.responseType;
				if(response.status && $scope.userName === response.userName && response.hero && $scope.myInfo.character.hero.name === response.hero.name){
					if(response.hero.name === 'JesseJones'){
						$scope.skillInfo.enableOkBtn = true;
						$scope.skillInfo.enableCancelBtn = false;
						$scope.skillInfo.step = response.step;
						$scope.skillInfo.players = response.players;
						$scope.skillInfo.titleDialog = 'Select player to get card';
						$scope.skillInfo.selectedPlayer = $scope.skillInfo.players[0];
						$timeout(function() {
							userSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
						}, 500);
					} else if(response.hero.name === 'PatBrennan'){
						$scope.skillInfo.step = response.step;
						if($scope.skillInfo.step === 2 && response.players){
							$scope.skillInfo.enableOkBtn = true;
							$scope.skillInfo.enableCancelBtn = false;
							$scope.skillInfo.players = response.players;
							$scope.skillInfo.titleDialog = 'Select player to get card';
							$scope.skillInfo.selectedPlayer = $scope.skillInfo.players[0];
							$timeout(function() {
								userSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
							}, 500);
						}
						if($scope.skillInfo.step === 3){
							$scope.skillInfo.cards = response.cards;
							$scope.skillInfo.titleBtnCard = 'Get';
							$scope.skillInfo.enableOkBtn = false;
							$scope.skillInfo.enableCancelBtn = false;
							$scope.skillInfo.selectedCards.length = 0;
							$scope.skillInfo.titleDialog = 'Select card instead of getting card in your turn.';
							$timeout(function() {
								cardSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
							}, 500);
						}
						
					} else if(response.hero.name === 'KitCarlson'){
						$scope.skillInfo.step = response.step;
						$scope.skillInfo.cards = response.cards;
						$scope.skillInfo.titleBtnCard = 'Remove';
						$scope.skillInfo.enableOkBtn = false;
						$scope.skillInfo.enableCancelBtn = false;
						$scope.skillInfo.selectedCards.length = 0;
						$scope.skillInfo.titleDialog = 'Choose 2 in 3. Remove 1 card..';
						$timeout(function() {
							cardSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
						}, 500);
					} else if(response.hero.name === 'LuckyDuke'){
						$scope.skillInfo.step = response.step;
						$scope.skillInfo.cards = response.cards;
						$scope.skillInfo.titleBtnCard = 'Select';
						$scope.skillInfo.enableOkBtn = false;
						$scope.skillInfo.enableCancelBtn = false;
						$scope.skillInfo.selectedCards.length = 0;
						$scope.skillInfo.titleDialog = 'Select card which is use to draw';
						$timeout(function() {
							cardSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
						}, 500);
					} else if(response.hero.name === 'SidKetchum'){
						$scope.skillInfo.step = response.step;
						$scope.skillInfo.cards = response.cards;
						angular.forEach($scope.skillInfo.cards, function(card){
							card.selected = false;
					    });
						$scope.skillInfo.titleBtnCard = 'Remove';
						$scope.skillInfo.enableOkBtn = false;
						$scope.skillInfo.enableCancelBtn = false;
						$scope.skillInfo.selectedCards.length = 0;
						$scope.skillInfo.numberCard = 2;
						$scope.skillInfo.titleDialog = 'Remove 2 card to get 1 life point';
						$timeout(function() {
							cardSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
						}, 500);
					} else if(response.hero.name === 'UncleWill'){
						$scope.skillInfo.step = response.step;
						$scope.skillInfo.cards = response.cards;
						$scope.skillInfo.titleBtnCard = 'Remove';
						$scope.skillInfo.enableOkBtn = false;
						$scope.skillInfo.enableCancelBtn = false;
						$scope.skillInfo.selectedCards.length = 0;
						$scope.skillInfo.titleDialog = 'Remove 1 card to use General Store';
						$timeout(function() {
							cardSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
						}, 500);
					} else if(response.hero.name === 'JoseDelgado'){
						$scope.skillInfo.step = response.step;
						$scope.skillInfo.cards = response.cards;
						$scope.skillInfo.titleBtnCard = 'Remove';
						$scope.skillInfo.enableOkBtn = false;
						$scope.skillInfo.enableCancelBtn = false;
						$scope.skillInfo.selectedCards.length = 0;
						$scope.skillInfo.titleDialog = 'Remove 1 blue card to get 2 new cards';
						$timeout(function() {
							cardSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
						}, 500);
					}  else if(response.hero.name === 'DocHolyday'){
						$scope.skillInfo.step = response.step;
						if($scope.skillInfo.step === 2 && response.cards){
							$scope.skillInfo.step = response.step;
							$scope.skillInfo.cards = response.cards;
							angular.forEach($scope.skillInfo.cards, function(card){
								card.selected = false;
						    });
							$scope.skillInfo.titleBtnCard = 'Remove';
							$scope.skillInfo.enableOkBtn = false;
							$scope.skillInfo.enableCancelBtn = false;
							$scope.skillInfo.selectedCards.length = 0;
							$scope.skillInfo.numberCard = 2;
							$scope.skillInfo.titleDialog = 'Remove 2 card to bang a persopm';
							$timeout(function() {
								cardSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
							}, 500);
						} else if($scope.skillInfo.step === 3 && response.players){
							$scope.skillInfo.enableOkBtn = true;
							$scope.skillInfo.enableCancelBtn = false;
							$scope.skillInfo.players = response.players;
							$scope.skillInfo.titleDialog = 'Select player to bang...';
							$scope.skillInfo.selectedPlayer = $scope.skillInfo.players[0];
							$timeout(function() {
								userSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
							}, 500);
						}
						
					} else if(response.hero.name === 'VeraCuster'){
						$scope.skillInfo.enableOkBtn = true;
						$scope.skillInfo.enableCancelBtn = false;
						$scope.skillInfo.step = response.step;
						$scope.skillInfo.players = response.players;
						$scope.skillInfo.titleDialog = 'Select player to copy skill...';
						$scope.skillInfo.selectedPlayer = $scope.skillInfo.players[0];
						$timeout(function() {
							userSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
						}, 500);
					} else if(response.hero.name === 'ClausTheSaint'){
						$scope.skillInfo.step = response.step;
						$scope.skillInfo.cards = response.cards;
						$scope.skillInfo.players = response.players;
						angular.forEach($scope.skillInfo.cards, function(card){
							card.assignedPlayer = response.userName;
					    });
						$scope.skillInfo.enableOkBtn = false;
						$scope.skillInfo.enableCancelBtn = false;
						$scope.skillInfo.titleDialog = 'Keep 2 for yourself, give 1 to each player.';
						$timeout(function() {
							userCardSkillDialog.modal({backdrop:'static',keyboard:true,show:true});
						}, 500);
					} else {
						console.log('Unknown hero');
						console.log(JSON.stringify(response));
					}
				} else {
					console.log('Cant not use skill');
				}
			}  else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
			$scope.$apply();
		}
		function onSkillTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'UseCardNotInTurn') {
				$scope.playerUserCardNotInTurn = response.message;
				$scope.notTurnCards = response.cards;
				$scope.showCardNotInTurn = true;
				addAlertMessage('success', response.serverMessage, response.userName, 10000);
//				callMessageServerFunc(response.serverMessage)
			}else if (response.responseType === 'Skill') {
				var msg = response.userName + ' use skill of ' + response.hero.name;
				if(response.targetUser){
					msg = msg + ' to ' + response.targetUser;
				}
				console.log('ERROR');
				console.log(msg);
			}  else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
			$scope.$apply();
		}
		function onServerReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Update') {
				if($scope.userName === response.userName){
					$scope.host = true;
				}
			} else if (response.responseType === 'Gift') {
//				addMessage(response.userName
//						+ ' receives 3 cards after killing a FUORILEGGE');
				addAlertMessage('success', response.message, response.userName, 10000);
			} else if (response.responseType === 'LoseCard') {
//				addMessage('SCERIFFO loses all his cards after killing a VICE');
				addAlertMessage('danger', response.message, response.userName, 10000);

			} else if (response.responseType === 'Join') {
				if(response.userName !== $scope.userName){
					addChattingMessage('success', 'has joined!', response.userName);
//					callMessageServerFunc(response.userName + ' has joined!', 10000);
				}
//				addMessage(response.userName + 'has joined!');
			}  else if (response.responseType === 'Leave') {
				
//				callMessageServerFunc(response.userName + '  has leaved!', 10000);
				addChattingMessage('warning', 'has leaved!', response.userName);
//				addMessage(response.userName + ' has leaved!');
			} else if (response.responseType === 'Winner') {
//				callMessageServerFunc(response.userName + ' win!!!!!', 10000);
//				addMessage(response.userName + ' win!!!!!');
				addAlertMessage('success', '', response.message, 100000);
				$interval.cancel(countDownPromise);
				$scope.endGame = true;
				$scope.myInfo.character.roleImage = $scope.myInfo.role.image;
				$scope.characterTurn = '';
				$scope.playerUsingCard = '';
				$scope.playerGettingCard = '';
				$scope.playerDrawingCard = '';
				$scope.playerUsingBarrel = '';
				$scope.playerCountDown = '';
				$scope.playerUsingSkill = false;
				$scope.actionType = '';
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
			$scope.$apply();
		}
		
//		function addChattingMessage(message) {
//			var messageArea = document
//					.querySelector('#chattingArea');
//			var messageElement = document.createElement('li');
//			messageElement.innerHTML = message;
//			messageElement.classList.add('li-server-notification');
////			messageElement.classList.add('animated');
////			messageElement.classList.add('bounceInRight');
//			messageArea.appendChild(messageElement);
//			messageArea.scrollTop = messageArea.scrollHeight;
//		}
		function addAlertMessage(type, message, strongMessage, timeToHide) {
			var messageArea = document.querySelector('#alertArea');
			var messageElement = document.createElement('div');
			var messageElementId = 'alert-' + new Date().getTime();
			messageElement.id = messageElementId;
			messageElement.classList.add('alert');
			if(type === 'warning'){
				messageElement.classList.add('alert-warning');
			} else if(type === 'success'){
				messageElement.classList.add('alert-success');
			} else if(type === 'danger'){
				messageElement.classList.add('alert-danger');
			} else {
				messageElement.classList.add('alert-info');
			}
			messageElement.classList.add('my-alert');
			if(strongMessage){
				messageElement.innerHTML = '<strong>'+ strongMessage +'</strong> ' +message;
			} else {
				messageElement.innerHTML = message;
			}
			messageArea.appendChild(messageElement);
			
			if(!timeToHide){
				timeToHide = 5000;
			}
			$timeout(function(messageElement) {
				var alertElement = document.querySelector('#' + messageElementId);
				alertElement.remove();
			}, timeToHide);
			
		}
		function addChattingMessage(type, message, strongMessage) {
			var messageArea = document.querySelector('#chattingArea');
			var messageElement = document.createElement('li');
			messageElement.classList.add('li-server-notification');
			messageElement.classList.add('alert');
			if(type === 'warning'){
				messageElement.classList.add('alert-warning');
			} else if(type === 'success'){
				messageElement.classList.add('alert-success');
			} else {
				messageElement.classList.add('alert-info');
			}
			messageElement.innerHTML = '<strong>'+ strongMessage +'</strong>: ' +message;
			messageArea.appendChild(messageElement);
			messageArea.scrollTop = messageArea.scrollHeight;
		}
		
		function onError(error) {
			console.log(error);
		}
		
		dialogInputUserName.modal({backdrop:'static',keyboard:true,show:true});
	});
