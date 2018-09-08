package com.example.springboot.command;

import java.util.LinkedList;
import java.util.Map.Entry;

import com.example.springboot.model.Character;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.BeerCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.CardType;
import com.example.springboot.model.card.CatPalouCard;
import com.example.springboot.model.card.DuelloCard;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.GatlingCard;
import com.example.springboot.model.card.GeneralStoreCard;
import com.example.springboot.model.card.IndiansCard;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.model.card.PanicCard;
import com.example.springboot.model.card.SaloonCard;
import com.example.springboot.model.card.StageCoachCard;
import com.example.springboot.model.card.WellsFargoCard;
import com.example.springboot.request.Request;
import com.example.springboot.response.CheckCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

public class UseCardActionCmd extends AbsActionCmd implements ActionCmd {

	public UseCardActionCmd(TableService tableService, HeroService heroService, ShareService shareService,
			TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Request request) {
		// get turn node
		TurnNode turnNode = turnService.getCurrentTurn();
		String userName = request.getUser();
		if (turnNode.getCharacter().getUserName().equals(userName)) {
			processUseCardInTurn(turnNode, request);
		} else {
			processUseCardNotInTurn(turnNode, request);
		}
	}

	private void processUseCardNotInTurn(TurnNode turnNode, Request userCardRequest) {
		String userName = userCardRequest.getUser();
		if (!turnNode.getNextPlayer().peek().equals(userName)) {
			return;
		}
		String sessionId = tableService.getUserMap().get(userName);
		// get Character
		Character character = tableService.getCharacterMap().get(userName);
		if (userCardRequest.isNoneResponse()) {
			tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
					new UseCardResponse(userName, null, null));
			character.setLifePoint(character.getLifePoint() - 1);
			BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);

			if (character.getLifePoint() == 0) {
				tableService.playerDead(userName);
			}
			if (ResponseType.Duello.equals(turnNode.getAction()) || ResponseType.Bang.equals(turnNode.getAction())) {
				turnNode.setAction(ResponseType.Unknown);
			} else {
				// TODO
			}
		} else {
			// get card from id
			Card card = BangUtils.getCardInHand(character, userCardRequest.getId());
			if (card == null) {
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%Card Error");
				return;
			}
			tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
					new UseCardResponse(userName, card, null));
			tableService.addToOldCardList(card);

			if (ResponseType.Bang.equals(turnNode.getAction()) || ResponseType.Gatling.equals(turnNode.getAction())) {
				if (card instanceof MissedCard || (card instanceof BangCard && character.getHero().useSkill(card))) {
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
				} else {
					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%Bang Gatling Error");
				}
			} else if (ResponseType.Indians.equals(turnNode.getAction())) {
				if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {

					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
				} else {
					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%Indians Error");
				}
			} else if (ResponseType.Duello.equals(turnNode.getAction())) {
				if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);

					String targetUser = turnNode.getCharacter().getUserName();
					turnNode.getNextPlayer().addFirst(targetUser);
					if (turnNode.getNextPlayer().peek() == null) {
						// request player in turn continue using card
						turnNode.requestPlayerUseCard();
					} else {
						turnNode.requestOtherPlayerUseCard();
					}
					return;
				} else {
					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%Duello Error");
				}
			} else {
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%UseCardCMD processUseCardNotInTurn Error");
			}
		}
		turnNode.getNextPlayer().poll();
		if (turnNode.getNextPlayer().peek() == null) {
			// request player in turn continue using card
			turnNode.requestPlayerUseCard();
		} else {
			turnNode.requestOtherPlayerUseCard();
		}
	}

	private void processUseCardInTurn(TurnNode turnNode, Request userCardRequest) {
		String userName = userCardRequest.getUser();
		String sessionId = tableService.getUserMap().get(userName);
		// get Character
		Character character = turnNode.getCharacter();

		if (userCardRequest.isNoneResponse()) {
			if (ResponseType.Duello.equals(turnNode.getAction())) {
				tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
						new UseCardResponse(userName, null, null));
				character.setLifePoint(character.getLifePoint() - 1);
				BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
				if (character.getLifePoint() == 0) {
					tableService.playerDead(userName);
					turnService.callNextPlayerTurn();
				} else {
					turnNode.setAction(ResponseType.Unknown);
					turnNode.requestPlayerUseCard();
				}
			} else {
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%processUseCardInTurn - nonresponse-  Duello Error");
			}
		} else {
			String targetUser = userCardRequest.getTargetUser();
			// get card from id
			Card card = BangUtils.getCardInHand(character, userCardRequest.getId());
			if (card == null) {
				return;
			}
			if (ResponseType.Duello.equals(turnNode.getAction())) {
				if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {
					turnNode.getNextPlayer().poll();
					if (turnNode.getNextPlayer().peek() == null) {
						// request player in turn continue using card
						turnNode.requestPlayerUseCard();
					} else {
						turnNode.requestOtherPlayerUseCard();
					}
					return;
				}
			}
			// gun card
			if (CardType.gun.equals(card.getCardType()) || CardType.barrel.equals(card.getCardType())
					|| CardType.otherviews.equals(card.getCardType())
					|| CardType.viewothers.equals(card.getCardType())) {
				tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
						new UseCardResponse(userName, card, null));
				Card cardFound = BangUtils.getCardByCardType(character.getCardsInFront(), card.getCardType());
				if (cardFound != null) {
					character.getCardsInFront().add(card);
					// remove card in hand
					character.getCardsInFront().remove(cardFound);
					// put into oldcardlist
					tableService.addToOldCardList(cardFound);
				} else {
					character.getCardsInFront().add(card);
					// set gun character
				}
				card.run(character);
				BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
				// request continue use cards
				turnNode.requestPlayerUseCard();
				return;
			} else if (CardType.physical.equals(card.getCardType())) {
				tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
						new UseCardResponse(userName, card, null));
				tableService.addToOldCardList(card);
				BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
				turnNode.setAction(ResponseType.Bang);
				turnNode.setAlreadyUseBangCard(true);
				turnNode.getNextPlayer().clear();
				turnNode.getNextPlayer().add(targetUser);
				turnNode.requestOtherPlayerUseCard();
				return;
			} else {
				if (card instanceof BeerCard) {
					tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
							new UseCardResponse(userName, card, null));
					card.run(character);
					tableService.addToOldCardList(card);
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof GatlingCard) {
					tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
							new UseCardResponse(userName, card, null));
					tableService.addToOldCardList(card);
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
					LinkedList<String> otherPlayers = BangUtils.getOtherPlayer(tableService.getPlayerTurnQueue(),
							userName);
					turnNode.setAction(ResponseType.Gatling);
					turnNode.setNextPlayer(otherPlayers);
					turnNode.requestOtherPlayerUseCard();
					return;
				} else if (card instanceof SaloonCard) {
					tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
							new UseCardResponse(userName, card, null));
					tableService.addToOldCardList(card);
					for (Entry<String, Character> entry : tableService.getCharacterMap().entrySet()) {
						card.run(entry.getValue());
						// notify hero of this character to others
						String sessionIdPlayer = tableService.getUserMap().get(entry.getKey());
						BangUtils.notifyCharacter(tableService.getMessagingTemplate(), entry.getValue(),
								sessionIdPlayer);
					}
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof WellsFargoCard || card instanceof StageCoachCard) {
					tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
							new UseCardResponse(userName, card, null));
					int n = (card instanceof WellsFargoCard) ? 3 : 2;
					character.getCardsInHand().addAll(tableService.getFromNewCardList(n));
					character.setNumCardsInHand(character.getCardsInHand().size());
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof JailCard || card instanceof DynamiteCard) {
					tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
							new UseCardResponse(userName, card, null));
					// set jail or dynamite
					character.setBeJailed(card instanceof JailCard);
					character.setHasDynamite(card instanceof DynamiteCard);
					character.getCardsInFront().add(card);
					// notify
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof IndiansCard) {
					tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
							new UseCardResponse(userName, card, null));
					tableService.addToOldCardList(card);
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
					turnNode.setAction(ResponseType.Indians);
					LinkedList<String> otherPlayers = BangUtils.getOtherPlayer(tableService.getPlayerTurnQueue(),
							userName);
					turnNode.setNextPlayer(otherPlayers);
					turnNode.requestOtherPlayerUseCard();
					return;
				} else if (card instanceof DuelloCard) {
					tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
							new UseCardResponse(userName, card, targetUser));
					tableService.addToOldCardList(card);
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
					turnNode.setAction(ResponseType.Duello);
					turnNode.getNextPlayer().clear();
					turnNode.getNextPlayer().add(targetUser);
					turnNode.requestOtherPlayerUseCard();
					return;
				} else if (card instanceof PanicCard || card instanceof CatPalouCard) {
					tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
							new UseCardResponse(userName, card, targetUser));
					tableService.addToOldCardList(card);
					ResponseType turnAction = card instanceof PanicCard ? ResponseType.Panic : ResponseType.CatPalou;
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
					turnNode.setAction(turnAction);
					turnNode.getNextPlayer().clear();
					turnNode.getNextPlayer().add(targetUser);
					turnNode.requestOtherPlayerUseCard();
					return;
				} else if (card instanceof GeneralStoreCard) {
					tableService.getMessagingTemplate().convertAndSend("/topic/usedCard",
							new UseCardResponse(userName, card, null));
					tableService.addToOldCardList(card);
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
					turnNode.setAction(ResponseType.GeneralStore);
					turnNode.setCardTemp(tableService.getFromNewCardList(tableService.getPlayerTurnQueue().size()));
					LinkedList<String> otherPlayers = BangUtils.getOtherPlayer(tableService.getPlayerTurnQueue(),
							userName);
					otherPlayers.addFirst(userName);
					turnNode.setNextPlayer(otherPlayers);
					turnNode.requestOtherPlayerUseCard();
					return;
				} else {
					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%processUseCardInTurn - magic-" + card.getName());
				}
			}
		}

	}

}
