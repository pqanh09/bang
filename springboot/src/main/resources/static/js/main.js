'use strict';
var myapp = angular.module('myapp', []);
myapp.controller('FirstCtrl',
	function($scope, $timeout) {
		var socket = null;
		var stompClient = null;
		var characters = {};
		var dialogInputUserName = angular.element('#dialogInputUserName');
		var dialogSelectUser = angular.element('#dialogSelectUser');
		var dialogSelectHero = angular.element('#dialogSelectHero');
		var dialogSelectCard = angular.element('#dialogSelectCard');
		var dialogSelectCardPC = angular.element('#dialogSelectCardPC');
		var dialogFullImage = angular.element('#dialogFullImage');
		
		$scope.hallPage = true;
		$scope.gamePage = false;
		$scope.userName = '';
		$scope.host = false;
		$scope.matches = null;
		$scope.matchId = null;
		
		$scope.dialogSelectCardTitle = '';
		$scope.dialogSelectCardActionType = '';
		$scope.dialogSelectCardActionStr = '';
		$scope.userCanBeAffectList = [];
		$scope.selectedUser = '';
		$scope.selectedCard = '';
		$scope.selectedHero = null;
		$scope.oldCard = null;
		$scope.cardsInFront = [];
		$scope.cardsInHand = [];
		$scope.characters = [];
		$scope.role = null;
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
		
		$scope.sendMessage = function(keyEvent) {
			 if (keyEvent.which === 13){
				 stompClient.send("/app/game.execute", {}, JSON
							.stringify({
								id : $scope.chattingMsg,
								actionType : 'Chatting'
							}));
				 $scope.chattingMsg = '';
			 }
		};
		$scope.useBarrelCardFunc = function(cardId) {
			stompClient.send("/app/game.execute", {}, JSON
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
		$scope.getCardFunc = function() {
			stompClient.send("/app/game.execute", {}, JSON
					.stringify({
						actionType : 'GetCard'
					}));
			$scope.playerGettingCard = '';

		};
		$scope.drawFunc = function() {
			stompClient.send("/app/game.execute", {}, JSON
					.stringify({
						actionType : $scope.actionType
					}));
			// $scope.playerDrawingCard = '';
		};
		$scope.useHeroSkillFunc = function() {

		};
		$scope.endTurnFunc = function() {
			stompClient.send("/app/game.execute", {}, JSON
					.stringify({
						actionType : 'EndTurn'
					}));
		};
		$scope.okUserDialogFunc = function() {
			stompClient.send("/app/game.execute", {}, JSON
					.stringify({
						id : $scope.selectedCard,
						actionType : 'UseCard',
						targetUser : $scope.selectedUser
					}));
		};
		$scope.lostLifePointFunc = function() {
			stompClient.send("/app/game.execute", {}, JSON
					.stringify({
						actionType : 'UseCard',
						noneResponse : true
					}));
			$scope.playerUsingCard = '';
		};
		$scope.canceUserDialoglFunc = function() {
		};

		$scope.heroPickFunc = function(heroId) {
			stompClient.send("/app/game.execute", {}, JSON
					.stringify({
						id : heroId,
						actionType : 'PickHero'
					}));
			$scope.dialogTitle = '';
		};
		$scope.pickCardToRemoveFunc = function(cardId) {
			if ($scope.dialogSelectCardActionType === 'RemoveCard') {
				stompClient.send("/app/game.execute", {}, JSON
						.stringify({
							id : cardId,
							actionType : 'RemoveCard'
						}));
			} else if ($scope.dialogSelectCardActionType === 'GeneralStore') {
				stompClient.send("/app/game.execute", {}, JSON
						.stringify({
							id : cardId,
							actionType : 'GeneralStore'
						}));
			}
		};
		$scope.pickCardToRemoveFuncPC = function(cardId) {
			stompClient.send("/app/game.execute",{},
				JSON.stringify({
					id : cardId,
					actionType : $scope.dialogSelectCardActionType
				}));
		};
		$scope.pickCardToRemoveFuncPCRandom = function(cardId) {
			stompClient.send("/app/game.execute",{},
				JSON.stringify({
					actionType : $scope.dialogSelectCardActionType
				}));
		};
		$scope.useCardFunc = function(cardId) {
			$scope.selectedCard = cardId;
			stompClient.send("/app/game.execute", {}, JSON
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
			stompClient.send("/app/game.join", {}, 
					JSON.stringify({
						id : matchId,
						actionType : 'Join'
					}));
		};
		$scope.createNewMatchFunc = function(matchId) {
			stompClient.send("/app/game.create", {});
		};
		$scope.startGameFunc = function() {
			stompClient.send("/app/game.start", {});
			$scope.host = false;
		};
		$scope.refreshMatchFunc = function() {
			stompClient.send("/app/game.get", {});
		};
		
		//normal func
		
		function onConnected() {
			// ok
			stompClient.subscribe('/user/queue/user', onUserReceived);
			stompClient.subscribe('/user/queue/game', onGameReceived);
			
			$scope.userName = $scope.userName.replace(/ /g,'');
			// Tell your username to the server
			stompClient.send("/app/user.create", {}, JSON.stringify({
				id : $scope.userName,
				actionType : 'Create'
			}));
		}
		
		
		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		
		
		function onGameReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Get') {
				$scope.matches = response.matches;
			} else if (response.responseType === 'Join' || response.responseType === 'Create') {
				$scope.matchId = response.matchId;
				$scope.gamePage = true;
				$scope.hallPage = false;
				$scope.host = response.host;
				//subscribe
				stompClient.subscribe('/user/queue/'+ $scope.matchId +'/role', onRoleReceived);
				stompClient.subscribe('/user/queue/'+ $scope.matchId +'/hero', onHeroReceived);
				stompClient.subscribe('/topic/'+ $scope.matchId +'/server', onServerReceived);
				stompClient.subscribe('/user/queue/'+ $scope.matchId +'/character', onCharacterQueueReceived);
				stompClient.subscribe('/topic/'+ $scope.matchId +'/character', onCharacterTopicReceived);
				stompClient.subscribe('/user/queue/'+ $scope.matchId +'/checkcard',onCheckCardQueueReceived);
				stompClient.subscribe('/user/queue/'+ $scope.matchId +'/removecardendturn',onRemoveCardBeforeEndTurnQueueReceived);
				stompClient.subscribe('/topic/'+ $scope.matchId +'/removecardendturn',onRemoveCardBeforeEndTurnTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.matchId +'/turn',onTurnTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.matchId +'/usedCard',onUsedCardTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.matchId +'/cardaction',onCardActionTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.matchId +'/oldcard',onOldCardTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.matchId +'/action',onActionTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.matchId +'/chatting',onChattingTopicReceived);
				
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
			$scope.$apply();
		}
		function onUserReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Create') {
				$scope.userName = response.userName;
				stompClient.send("/app/game.get", {});
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
				$scope.role = response.role;
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
							+ $scope.role.name
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
				$scope.$apply();
			} else {
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
						update = true;
						$scope.$apply();
					}
				});
				if (!update) {
					$scope.characters.push(response.character);
					$scope.$apply();
				}
			} else if (response.responseType === 'Dead') {
				addMessage(response.userName + ' is dead!!!');
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
						if (dialogSelectUser) {
							$timeout(
									function() {
										$scope.userCanBeAffectList = response.userCanBeAffectList;
										$scope.selectedUser = $scope.userCanBeAffectList[0];
										dialogSelectUser.modal({backdrop:'static',keyboard:true,show:true});
									}, 200);
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
		function onRemoveCardBeforeEndTurnQueueReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'RemoveCard') {
				$timeout(
						function() {
							$scope.cards = response.cards;
							$scope.dialogSelectCardTitle = 'remove';
							$scope.dialogSelectCardActionType = response.responseType;
							$scope.dialogSelectCardActionStr = 'Remove';
							$scope.playerUsingCard = response.userName;
							$scope.actionType = response.responseType;
							dialogSelectCard.modal({backdrop:'static',keyboard:true,show:true});
						}, 500);
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
		}
		
		function onRemoveCardBeforeEndTurnTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'RemoveCard') {
				addMessage(response.userName
						+ ' has just removed card '
						+ response.cards[0].name
						+ ' before ending turn!');

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
				if (response.card) {
					msg = msg + ' use ' + response.card.name;
					if (response.targetuser) {
						msg = msg + ' on ' + response.targetuser;
					}

				} else {
					msg = msg + ' accept losing life point';

				}
				addMessage(msg);
			} else if (response.responseType === 'DrawCardJail') {
				addMessage(response.userName + ' - drawed card '
						+ response.card.name);

				$scope.playerDrawingCard = '';
			} else if (response.responseType === 'DrawCardDynamite') {
				addMessage(response.userName + ' - drawed card '
						+ response.card.name);
				$scope.playerDrawingCard = '';
			} else if (response.responseType === 'EscapeJail') {
				addMessage(response.userName + ' - escapes the Jail. ');
				$scope.playerDrawingCard = '';
			} else if (response.responseType === 'EscapeDynamite') {
				addMessage(response.userName + ' - escapes the Dynamite. It is transfered to'
						+ response.card.targetUser);
				$scope.playerDrawingCard = '';
			} else if (response.responseType === 'LostTurn') {
				addMessage(response.userName + ' - lost his/her turn.');
				$scope.playerDrawingCard = '';
			} else if (response.responseType === 'LostLifePoint') {
				addMessage(response.userName + ' - lost 3 life point because Dynamite.');
				$scope.playerDrawingCard = '';
			} else if (response.responseType === 'UseBarrel') {
				if(response.canUseBarrel === undefined){
					addMessage(response.userName + ' - drawed card ' + response.card.name);
				} else {
					if(response.canUseBarrel){
						addMessage(response.userName + ' uses successfully the Barrel. ');
					} else {
						addMessage(response.userName + ' does not use successfully the Barrel');
					}
				}
				$scope.playerDrawingCard = '';
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
			$scope.$apply();
		}
		function onOldCardTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'OldCard') {
				$scope.oldCard = response.cards[0];
				$scope.$apply();
			} else {
				console.log('Error');
				alert(JSON.stringify(response));
			}
		}
		function onChattingTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Chatting') {
				addChattingMessage(response.userName + ': ' +response.message);
			} else {
				console.log('Error');
				alert(JSON.stringify(response));
			}
		}
		function onTurnTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Turn') {
				addMessage(' Turn of ' + response.userName
						+ ' is started! ');

				$scope.characterTurn = response.userName;
				$scope.$apply();
			} else if (response.responseType === 'EndTurn') {
				addMessage(' Turn of ' + response.userName
						+ ' is finished! ');
				$scope.playerUsingCard = '';
				$scope.$apply();
			} else {
				console.log('Error');
				alert(JSON.stringify(response));
			}
		}
		function onCardActionTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'GetCard') {
				addMessage(response.userName
						+ ' will get card......');

				$scope.playerGettingCard = response.userName;
				$scope.playerUsingCard = '';
				$scope.playerDrawingCard = '';
				$scope.$apply();
			} else if (response.responseType === 'UseCard') {
				addMessage(response.userName
						+ ' will use card......');

				$scope.playerUsingCard = response.userName;
				$scope.actionType = response.responseType;
				$scope.playerGettingCard = ''
				$scope.playerDrawingCard = '';
				$scope.$apply();
			} else if (response.responseType === 'DrawCardJail') {
				addMessage(response.userName
						+ ' will draw card for escaping the Jail......');

				$scope.playerDrawingCard = response.userName;
				$scope.actionType = response.responseType;
				$scope.playerUsingCard = '';
				$scope.playerGettingCard = '';
				$scope.$apply();
			} else if (response.responseType === 'DrawCardDynamite') {
				addMessage(response.userName
						+ ' will draw card for escaping dynamite......');

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
			if (response.responseType === 'Bang'
					|| response.responseType === 'Gatling'
					|| response.responseType === 'Duello'
					|| response.responseType === 'Indians') {
				$scope.actionType = response.responseType;
				$scope.playerUsingCard = response.userName;
				if (response.canUseBarrel
						&& (response.responseType === 'Bang' || response.responseType === 'Gatling')) {
					$scope.playerUsingBarrel = response.userName;
				} else {
					$scope.playerUsingBarrel = '';
				}
				$scope.$apply();
			} else if (response.responseType === 'Panic') {
				if ($scope.userName === response.userName) {
					$timeout(
							function() {
								$scope.playerUsingCard = '';
								$scope.cards = response.cards;
								$scope.dialogSelectCardTitle = 'get';
								$scope.dialogSelectCardActionType = response.responseType;
								$scope.dialogSelectCardActionStr = 'Get';
								dialogSelectCardPC.modal({backdrop:'static',keyboard:true,show:true});
							}, 500);
				}
			} else if (response.responseType === 'CatPalou') {
				if ($scope.userName === response.userName) {
					$timeout(
							function() {
								$scope.playerUsingCard = '';
								$scope.cards = response.cards;
								$scope.dialogSelectCardTitle = 'remove';
								$scope.dialogSelectCardActionType = response.responseType;
								$scope.dialogSelectCardActionStr = 'Remove';
								dialogSelectCardPC.modal({backdrop:'static',keyboard:true,show:true});
							}, 500);
				}
			} else if (response.responseType === 'GeneralStore') {
				if ($scope.userName === response.userName) {
					$timeout(
							function() {
								$scope.playerUsingCard = '';
								$scope.cards = response.cards;
								$scope.dialogSelectCardTitle = 'get';
								$scope.dialogSelectCardActionType = response.responseType;
								$scope.dialogSelectCardActionStr = 'Get';
								$scope.actionType = response.responseType;
								dialogSelectCard.modal({backdrop:'static',keyboard:true,show:true});
							}, 500);
				}
			} else {
				$scope.playerUsingBarrel = '';
				$scope.$apply();
				console.log('ERROR');
				alert(JSON.stringify(response));
			}

		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		function onServerReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Gitf') {
				addMessage(response.userName
						+ ' receives 3 cards after killing a FUORILEGGE');
			} else if (response.responseType === 'LoseCard') {
				addMessage('SCERIFFO loses all his cards after killing a VICE');

			} else if (response.responseType === 'Join') {
				addMessage(response.userName + 'has joined!');
			}  else if (response.responseType === 'Leave') {
				addMessage(response.userName + ' has leaved!');
			} else if (response.responseType === 'Winner') {
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
		
		function addMessage(message) {
			var messageArea = document
					.querySelector('#messageArea');
			var messageElement = document.createElement('li');
			messageElement.innerHTML = message;
			messageElement.classList.add('li-server-notification');
			messageArea.appendChild(messageElement);
			messageArea.scrollTop = messageArea.scrollHeight;
		}
		function addChattingMessage(message) {
			var messageArea = document
					.querySelector('#chattingArea');
			var messageElement = document.createElement('li');
			messageElement.innerHTML = message;
			messageElement.classList.add('li-server-notification');
			messageArea.appendChild(messageElement);
			messageArea.scrollTop = messageArea.scrollHeight;
		}
		
		function onError(error) {
			console.log(error);
		}
		
		dialogInputUserName.modal({backdrop:'static',keyboard:true,show:true});
	});
