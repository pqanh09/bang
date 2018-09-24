'use strict';
var myapp = angular.module('myapp', []);
myapp.controller('FirstCtrl',
	function($scope, $timeout, $interval) {
		var socket = null;
		var stompClient = null;
		var dialogInputUserName = angular.element('#dialogInputUserName');
		var dialogSelectUser = angular.element('#dialogSelectUser');
		var dialogSelectHero = angular.element('#dialogSelectHero');
		var dialogFullImage = angular.element('#dialogFullImage');
		//card: Panic - Cat Palou - General Store - Remove Card when ending turn
		var cardDialog = angular.element('#cardDialog');
		//skill
		var userSkillDialog = angular.element('#userSkillDialog');
		var cardSkillDialog = angular.element('#cardSkillDialog');
		var userCardSkillDialog = angular.element('#userCardSkillDialog');
		$scope.timeSkill = 10;
		var timerSkill;
		var timerSkillFunc = function() {
            if( $scope.timeSkill > 0 ) {
            	$scope.timeSkill -=1;
            } else {
            	$interval.cancel(timerSkill);
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
            			stompClient.send('/app/game.execute', {}, JSON
            					.stringify({
            						actionType : 'GetCard'
            					}));
            			$scope.playerGettingCard = '';
            		}
            		else if($scope.actionType === 'UseCard'){
//            			dialogSelectCardPC.modal('hide');
            			dialogSelectUser.modal('hide');
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
		
		$scope.hallPage = true;
		$scope.gamePage = false;
		$scope.userName = '';
		$scope.host = false;
		$scope.matches = null;
		$scope.myInfo = {
			matchId: null,
			hero: null,
			role: null
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
//		$scope.dialogSelectCardTitle = '';
//		$scope.dialogSelectCardActionType = '';
//		$scope.dialogSelectCardActionStr = '';
		$scope.userCanBeAffectList = [];
		$scope.selectedUser = '';
		$scope.selectedCard = '';
		$scope.oldCard = null;
		$scope.cardsInFront = [];
		$scope.cardsInHand = [];
		$scope.characters = [];
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
		$scope.getCardFunc = function() {
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						actionType : 'GetCard'
					}));
			$scope.playerGettingCard = '';

		};
		$scope.drawFunc = function() {
			stompClient.send('/app/game.execute', {}, JSON
					.stringify({
						actionType : $scope.actionType
					}));
			// $scope.playerDrawingCard = '';
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
			$interval.cancel(timerSkill);
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
			$interval.cancel(timerSkill);
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
				$interval.cancel(timerSkill);
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
					$interval.cancel(timerSkill);
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
			if($scope.myInfo.hero.name === 'PatBrennan' || $scope.myInfo.hero.name === 'KitCarlson' || $scope.myInfo.hero.name === 'LuckyDuke' || $scope.myInfo.hero.name === 'UncleWill' || $scope.myInfo.hero.name === 'JoseDelgado'){
				$scope.skillInfo.selectedCards.push(cardId);
				$interval.cancel(timerSkill);
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
			} else if($scope.myInfo.hero.name === 'SidKetchum' || $scope.myInfo.hero.name === 'DocHolyday'){ 
				angular.forEach($scope.skillInfo.cards, function(card){
					if(card.id === cardId){
						$scope.skillInfo.selectedCards.push(cardId);
						card.selected = true;
					}
			    });
				if($scope.skillInfo.selectedCards.length >= $scope.skillInfo.numberCard){
					$interval.cancel(timerSkill);
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
			$interval.cancel(timerSkill);
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
		
		//normal func
		
		function onConnected() {
			// ok
			stompClient.subscribe('/user/queue/user', onUserReceived);
			
			stompClient.subscribe('/user/queue/game', onGameQueueReceived);
			stompClient.subscribe('/topic/game', onGameTopicReceived);
			
			$scope.userName = $scope.userName.replace(/ /g,'');
			// Tell your username to the server
			stompClient.send('/app/user.create', {}, JSON.stringify({
				id : $scope.userName,
				actionType : 'Create'
			}));
		}
		
		
		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		
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
			if (response.responseType === 'Join' || response.responseType === 'Create') {
				$scope.myInfo.matchId = response.matchId;
				$scope.gamePage = true;
				$scope.hallPage = false;
				$scope.host = response.host;
				//subscribe
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/role', onRoleReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/hero', onHeroReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/server', onServerReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/skill', onSkillTopicReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/character', onCharacterQueueReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/skill', onSkillQueueReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/countdown', onCountDownTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/character', onCharacterTopicReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/checkcard',onCheckCardQueueReceived);
				stompClient.subscribe('/user/queue/'+ $scope.myInfo.matchId +'/removecard',onRemoveCardBeforeEndTurnQueueReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/removecard',onRemoveCardBeforeEndTurnTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/turn',onTurnTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/usedCard',onUsedCardTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/cardaction',onCardActionTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/oldcard',onOldCardTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/action',onActionTopicReceived);
				stompClient.subscribe('/topic/'+ $scope.myInfo.matchId +'/chatting',onChattingTopicReceived);
				
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
				$scope.$apply();
				$scope.host = false;
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
				$scope.myInfo.hero = response.character.hero;
				$scope.$apply();
			} else {
				console.log('Error');
				alert(JSON.stringify(response));
			}
		}
		function onCountDownTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'CountDownStart') {
				$interval.cancel(timerSkill);
				$scope.playerCountDown = response.userName;
				$scope.timeSkill = response.countDown;
				timerSkill = $interval(timerSkillFunc, 1000);
				$scope.$apply();
			} 
			else if (response.responseType === 'CountDownEnd') {
//				$interval.cancel(timerSkill);
//				$scope.playerCountDown = '';
//				$scope.timeSkill = 0;
//				$scope.$apply();
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
						stompClient.send('/app/game.execute', {},
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
				$scope.cardInfo.titleDialog = 'You must remove ' + response.numberCard + ' before ending turn.';
				$timeout(function() {
					cardDialog.modal({backdrop:'static',keyboard:true,show:true});
				}, 500);
				/// count down time
				$interval.cancel(timerSkill);
				$scope.timeSkill = response.countDown;
				$scope.playerCountDown = response.userName;
				timerSkill = $interval(timerSkillFunc, 1000);
				///
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
		}
		
		//TODO not use
		function onRemoveCardBeforeEndTurnTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'RemoveCardEndTurn') {
				if($scope.userName !== response.userName){
					/// count down time
					$interval.cancel(timerSkill);
					$scope.timeSkill = response.userName;
					$scope.playerCountDown = response.userName;
					timerSkill = $interval(timerSkillFunc, 1000);
					///
				}
				$scope.$apply();
			} else if (response.responseType === 'RemoveCard') {
				addMessage(response.userName
						+ ' has just removed card '
						+ response.cards[0].name);
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
				$scope.actionType = response.responseType;
				/// count down time
				$interval.cancel(timerSkill);
				$scope.timeSkill = response.userName;
				$scope.playerCountDown = response.userName;
				timerSkill = $interval(timerSkillFunc, 1000);
				///
				$scope.$apply();
			} else if (response.responseType === 'UseCard') {
				addMessage(response.userName
						+ ' will use card......');
				if($scope.userName === response.userName){
					$scope.skillInfo.enableUseSkillBtn = true;
				}
				$scope.playerUsingCard = response.userName;
				$scope.actionType = response.responseType;
				$scope.playerGettingCard = ''
				$scope.playerDrawingCard = '';
				
				/// count down time
				$interval.cancel(timerSkill);
				$scope.timeSkill = response.userName;
				$scope.playerCountDown = response.userName;
				timerSkill = $interval(timerSkillFunc, 1000);
				///
				
				$scope.$apply();
			} else if (response.responseType === 'DrawCardJail') {
				addMessage(response.userName
						+ ' will draw card for escaping the Jail......');

				$scope.playerDrawingCard = response.userName;
				$scope.actionType = response.responseType;
				$scope.playerUsingCard = '';
				$scope.playerGettingCard = '';
				
				/// count down time
				$interval.cancel(timerSkill);
				$scope.timeSkill = response.userName;
				$scope.playerCountDown = response.userName;
				timerSkill = $interval(timerSkillFunc, 1000);
				///
				
				$scope.$apply();
			} else if (response.responseType === 'DrawCardDynamite') {
				addMessage(response.userName
						+ ' will draw card for escaping dynamite......');

				$scope.playerDrawingCard = response.userName;
				$scope.actionType = response.responseType;
				$scope.playerUsingCard = '';
				$scope.playerGettingCard = '';
				
				/// count down time
				$interval.cancel(timerSkill);
				$scope.timeSkill = response.userName;
				$scope.playerCountDown = response.userName;
				timerSkill = $interval(timerSkillFunc, 1000);
				///
				
				$scope.$apply();
			} else {
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
		}
		function onActionTopicReceived(payload) {
			var response = JSON.parse(payload.body);
			$scope.skillInfo.enableUseSkillBtn = false;
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
				/// count down time
				$interval.cancel(timerSkill);
				$scope.timeSkill = response.countDown;
				$scope.playerCountDown = response.userName;
				timerSkill = $interval(timerSkillFunc, 1000);
				///
				$scope.$apply();
			} else if (response.responseType === 'Panic') {
				$scope.actionType = response.responseType;
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
				$interval.cancel(timerSkill);
				$scope.timeSkill = response.countDown;
				$scope.playerCountDown = response.userName;
				timerSkill = $interval(timerSkillFunc, 1000);
				///
			} else if (response.responseType === 'CatPalou') {
				if ($scope.userName === response.userName) {
					$scope.actionType = response.responseType;
					
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
				$interval.cancel(timerSkill);
				$scope.timeSkill = response.countDown;
				$scope.playerCountDown = response.userName;
				timerSkill = $interval(timerSkillFunc, 1000);
				///
			} else if (response.responseType === 'GeneralStore') {
				if ($scope.userName === response.userName) {
					$scope.actionType = response.responseType;
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
				$interval.cancel(timerSkill);
				$scope.timeSkill = response.countDown;
				$scope.playerCountDown = response.userName;
				timerSkill = $interval(timerSkillFunc, 1000);
				///
			} else {
				$scope.playerUsingBarrel = '';
				console.log('ERROR');
				alert(JSON.stringify(response));
			}
			$scope.$apply();

		}
		function onSkillQueueReceived(payload) {
			var response = JSON.parse(payload.body);
			if (response.responseType === 'Skill') {
				$scope.actionType = response.responseType;
				//addMessage(JSON.stringify(response));
				if(response.status && $scope.userName === response.userName && response.hero && $scope.myInfo.hero.name === response.hero.name){
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
			if (response.responseType === 'Skill') {
				var msg = response.userName + ' use skill of ' + response.hero.name;
				if(response.targetUser){
					msg = msg + ' to ' + response.targetUser;
				}
				addMessage(msg);
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
					$scope.host = response.host;
				}
			
			} else if (response.responseType === 'Gitf') {
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
				$scope.playerCountDown = '';
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
			messageElement.classList.add('animated');
			messageElement.classList.add('rubberBand');
			messageArea.appendChild(messageElement);
			messageArea.scrollTop = messageArea.scrollHeight;
		}
		function addChattingMessage(message) {
			var messageArea = document
					.querySelector('#chattingArea');
			var messageElement = document.createElement('li');
			messageElement.innerHTML = message;
			messageElement.classList.add('li-server-notification');
			messageElement.classList.add('animated');
			messageElement.classList.add('bounceInRight');
			messageArea.appendChild(messageElement);
			messageArea.scrollTop = messageArea.scrollHeight;
		}
		
		function onError(error) {
			console.log(error);
		}
		
		dialogInputUserName.modal({backdrop:'static',keyboard:true,show:true});
	});
