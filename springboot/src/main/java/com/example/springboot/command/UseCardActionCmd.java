package com.example.springboot.command;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
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
import com.example.springboot.model.hero.BartCassidy;
import com.example.springboot.model.hero.ElGringo;
import com.example.springboot.model.hero.JohnnyKisch;
import com.example.springboot.model.hero.TequilaJoe;
import com.example.springboot.request.Request;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class UseCardActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(UseCardActionCmd.class);
	public UseCardActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		// get turn node
		TurnNode turnNode = match.getCurrentTurn();
		String userName = request.getUser();
		if (turnNode.getCharacter().getUserName().equals(userName)) {
			processUseCardInTurn(match, request);
		} else {
			processUseCardNotInTurn(match, request);
		}
	}

	private void processUseCardNotInTurn(Match match, Request userCardRequest) throws Exception {
		String userName = userCardRequest.getUser();
		TurnNode turnNode = match.getCurrentTurn();
		if (!turnNode.getNextPlayer().peek().equals(userName)) {
			return;
		}
		String sessionId = match.getUserMap().get(userName);
		// get Character
		Character character = match.getCharacterMap().get(userName);
		if (userCardRequest.isNoneResponse()) {
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard",
					new UseCardResponse(userName, null, null));
			character.setLifePoint(character.getLifePoint() - 1);
			// skill hero  BartCassidy
			if(character.getHero() instanceof BartCassidy) {
				Map<String, Object> others = new HashMap<>();
				others.put("numberNewCard", 1);
				character.getHero().useSkill(match, userName, character, commonService, others);
			}
			// skill hero  ElGringo
			if(character.getHero() instanceof ElGringo) {
				character.getHero().useSkill(match, userName, character, commonService, null);
			}
			//
			BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
			if (character.getLifePoint() == 0) {
				commonService.playerDead(userName, true, match);
				if(match.getPlayerTurnQueue().size() <=1) {
					//end game
					return;
				}
			}
			if (ResponseType.Duello.equals(turnNode.getAction()) || ResponseType.Bang.equals(turnNode.getAction())) {
				turnNode.setAction(ResponseType.Unknown);
			} else {
				// TODO
			}
		} else {
			// get card from id
			Card card = commonService.getCardInHand(match, character, userCardRequest.getId(), null);
			if (card == null) {
				logger.error("%%%%%%%%%%%%%%%%%%%%%%%Card Error");
				return;
			}
			commonService.addToOldCardList(card, match);

			if (ResponseType.Bang.equals(turnNode.getAction()) || ResponseType.Gatling.equals(turnNode.getAction())) {
				if (card instanceof MissedCard || (card instanceof BangCard && character.getHero().useSkill(card))) {
					BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
				} else {
					logger.error("%%%%%%%%%%%%%%%%%%%%%%%Bang Gatling Error");
				}
			} else if (ResponseType.Indians.equals(turnNode.getAction())) {
				if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {

					BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
				} else {
					logger.error("%%%%%%%%%%%%%%%%%%%%%%%Indians Error");
				}
			} else if (ResponseType.Duello.equals(turnNode.getAction())) {
				if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {
					BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);

					String targetUser = turnNode.getCharacter().getUserName();
					turnNode.getNextPlayer().addFirst(targetUser);
					if (turnNode.getNextPlayer().peek() == null) {
						// request player in turn continue using card
						turnNode.requestPlayerUseCard();
					} else {
						turnNode.requestOtherPlayerUseCard(match);
					}
					return;
				} else {
					logger.error("%%%%%%%%%%%%%%%%%%%%%%%Duello Error");
				}
			} else {
				logger.error("%%%%%%%%%%%%%%%%%%%%%%%UseCardCMD processUseCardNotInTurn Error");
			}
		}
		//
		if(match.getPlayerTurnQueue().size() > 1) {
			turnNode.getNextPlayer().poll();
			if (turnNode.getNextPlayer().peek() == null) {
				// request player in turn continue using card
				turnNode.requestPlayerUseCard();
			} else {
				turnNode.requestOtherPlayerUseCard(match);
			}
		}
	}

	private void processUseCardInTurn(Match match, Request userCardRequest) {
		String userName = userCardRequest.getUser();
		TurnNode turnNode = match.getCurrentTurn();
		String sessionId = match.getUserMap().get(userName);
		// get Character
		Character character = turnNode.getCharacter();

		if (userCardRequest.isNoneResponse()) {
			if (ResponseType.Duello.equals(turnNode.getAction())) {
				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard",
						new UseCardResponse(userName, null, null));
				character.setLifePoint(character.getLifePoint() - 1);
				// skill hero  BartCassidy
				if(character.getHero() instanceof BartCassidy) {
					Map<String, Object> others = new HashMap<>();
					others.put("numberNewCard", 1);
					character.getHero().useSkill(match, userName, character, commonService, others);
				}
				BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
				if (character.getLifePoint() == 0) {
					commonService.playerDead(userName, false, match);
					if(match.getPlayerTurnQueue().size() <=1) {
						//end game
						return;
					}
					commonService.callNextPlayerTurn(match);
				} else {
					turnNode.setAction(ResponseType.Unknown);
					turnNode.requestPlayerUseCard();
				}
			} else {
				logger.error("%%%%%%%%%%%%%%%%%%%%%%%processUseCardInTurn - nonresponse-  Duello Error");
			}
		} else {
			String targetUser = userCardRequest.getTargetUser();
			// get card from id
			Card card = commonService.getCardInHand(match, character, userCardRequest.getId(), targetUser);
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
						turnNode.requestOtherPlayerUseCard(match);
					}
					return;
				}
			}
			// gun card
			if (CardType.gun.equals(card.getCardType()) || CardType.barrel.equals(card.getCardType())
					|| CardType.otherviews.equals(card.getCardType())
					|| CardType.viewothers.equals(card.getCardType())) {
				Card cardFound = BangUtils.getCardByCardType(character.getCardsInFront(), card.getCardType());
				if (cardFound != null) {
					character.getCardsInFront().add(card);
					// remove card in hand
					character.getCardsInFront().remove(cardFound);
					// put into oldcardlist
					commonService.addToOldCardList(cardFound, match);
				} else {
					character.getCardsInFront().add(card);
					// set gun character
				}
				card.run(character);
				BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
				// skill JohnnyKisch
				if(character.getHero() instanceof JohnnyKisch) {
					Map<String, Object> others = new HashMap<>();
					others.put("card", card);
					character.getHero().useSkill(match, userName, character, commonService, others);
				}
				// request continue use cards
				turnNode.requestPlayerUseCard();
				return;
			} else if (CardType.physical.equals(card.getCardType())) {
				commonService.addToOldCardList(card, match);
				BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
				turnNode.setAction(ResponseType.Bang);
				turnNode.setAlreadyUseBangCard(true);
				turnNode.getNextPlayer().clear();
				turnNode.getNextPlayer().add(targetUser);
				turnNode.requestOtherPlayerUseCard(match);
				return;
			} else {
				if (card instanceof BeerCard) {
					//skill hero TequilaJoe
					if(character.getHero() instanceof TequilaJoe) {
						character.getHero().useSkill(match, userName, character, commonService, null);
					} else {
						card.run(character);
					}
					commonService.addToOldCardList(card, match);
					BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof GatlingCard) {
					commonService.addToOldCardList(card, match);
					BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
					LinkedList<String> otherPlayers = BangUtils.getOtherPlayer(match.getPlayerTurnQueue(),
							userName);
					// skill hero ApacheKid
					commonService.useSkillOfApacheKid(match, otherPlayers, card, false);
					turnNode.setAction(ResponseType.Gatling);
					turnNode.setNextPlayer(otherPlayers);
					turnNode.requestOtherPlayerUseCard(match);
					return;
				} else if (card instanceof SaloonCard) {
					commonService.addToOldCardList(card, match);
					for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
						card.run(entry.getValue());
						// notify hero of this character to others
						String sessionIdPlayer = match.getUserMap().get(entry.getKey());
						BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), entry.getValue(),
								sessionIdPlayer);
					}
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof WellsFargoCard || card instanceof StageCoachCard) {
					int n = (card instanceof WellsFargoCard) ? 3 : 2;
					character.getCardsInHand().addAll(commonService.getFromNewCardList(match, n));
					character.setNumCardsInHand(character.getCardsInHand().size());
					BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof DynamiteCard) {
					// set dynamite
					character.setHasDynamite(true);
					character.getCardsInFront().add(card);
					// notify
					BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				}  else if (card instanceof JailCard) {
					// set jail or dynamite
					Character targetCharacter = match.getCharacterMap().get(targetUser);
					targetCharacter.setBeJailed(true);
					targetCharacter.getCardsInFront().add(card);
					// notify
					BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), targetCharacter, match.getUserMap().get(targetUser));
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof IndiansCard) {
					commonService.addToOldCardList(card, match);
					turnNode.setAction(ResponseType.Indians);
					LinkedList<String> otherPlayers = BangUtils.getOtherPlayer(match.getPlayerTurnQueue(),
							userName);
					// skill hero ApacheKid
					commonService.useSkillOfApacheKid(match, otherPlayers, card, true);
					turnNode.setNextPlayer(otherPlayers);
					turnNode.requestOtherPlayerUseCard(match);
					return;
				} else if (card instanceof DuelloCard) {
					commonService.addToOldCardList(card, match);
					turnNode.setAction(ResponseType.Duello);
					turnNode.getNextPlayer().clear();
					turnNode.getNextPlayer().add(targetUser);
					turnNode.requestOtherPlayerUseCard(match);
					return;
				} else if (card instanceof PanicCard || card instanceof CatPalouCard) {
					commonService.addToOldCardList(card, match);
					ResponseType turnAction = card instanceof PanicCard ? ResponseType.Panic : ResponseType.CatPalou;
					turnNode.setAction(turnAction);
					turnNode.getNextPlayer().clear();
					turnNode.getNextPlayer().add(targetUser);
					turnNode.requestOtherPlayerUseCard(match);
					return;
				} else if (card instanceof GeneralStoreCard) {
					commonService.addToOldCardList(card, match);
					turnNode.setAction(ResponseType.GeneralStore);
					turnNode.setCardTemp(commonService.getFromNewCardList(match, match.getPlayerTurnQueue().size()));
					LinkedList<String> otherPlayers = BangUtils.getOtherPlayer(match.getPlayerTurnQueue(),
							userName);
					otherPlayers.addFirst(userName);
					turnNode.setNextPlayer(otherPlayers);
					turnNode.requestOtherPlayerUseCard(match);
					return;
				} else {
					logger.error("%%%%%%%%%%%%%%%%%%%%%%%processUseCardInTurn - magic-" + card.getName());
				}
			}
		}

	}

}
