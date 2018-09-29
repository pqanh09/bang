package com.example.springboot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.BarrelCard;
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
import com.example.springboot.model.card.MustangCard;
import com.example.springboot.model.card.PanicCard;
import com.example.springboot.model.card.SaloonCard;
import com.example.springboot.model.card.StageCoachCard;
import com.example.springboot.model.card.WellsFargoCard;
import com.example.springboot.model.hero.BartCassidy;
import com.example.springboot.model.hero.BelleStar;
import com.example.springboot.model.hero.ElGringo;
import com.example.springboot.model.hero.ElenaFuente;
import com.example.springboot.model.hero.JohnnyKisch;
import com.example.springboot.model.hero.MollyStark;
import com.example.springboot.model.hero.PaulRegret;
import com.example.springboot.model.hero.RoseDoolan;
import com.example.springboot.model.hero.SlabTheKiller;
import com.example.springboot.model.hero.TequilaJoe;
import com.example.springboot.request.Request;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardInTurnResponse;
import com.example.springboot.response.UseCardNotInTurnResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;
import com.example.springboot.utils.CardUtils;

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
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardNotInTurn",
					new UseCardNotInTurnResponse(userName, CardUtils.losePointCard, null, null, null));
			character.setLifePoint(character.getLifePoint() - 1);
			// skill hero  BartCassidy
			if(character.getHero() instanceof BartCassidy) {
				Map<String, Object> others = new HashMap<>();
				others.put("numberNewCard", 1);
				character.getHero().useSkill(match, character, commonService, 1, others);
			}
			// skill hero  ElGringo
			if(character.getHero() instanceof ElGringo) {
				character.getHero().useSkill(match, character, commonService, 1, null);
			}
			//
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			if (character.getLifePoint() == 0) {
				commonService.playerDead(userName, true, match, false);
				if(match.getPlayerTurnQueue().size() <=1) {
					//end game
					return;
				}
			}
			if (ResponseType.Duello.equals(turnNode.getAction()) || ResponseType.Bang.equals(turnNode.getAction())) {
				turnNode.setAction(ResponseType.Unknown);
				turnNode.getPlayerSkillBarrel().clear();
				turnNode.getPlayerUsedBarrel().clear();
				turnNode.getPlayerUsedMissed().clear();
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
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardNotInTurn",
					new UseCardNotInTurnResponse(userName, card, null, null, null));
			commonService.addToOldCardList(card, match);
			//skill hero MollyStark
			if(character.getHero() instanceof MollyStark) {
				character.getHero().useSkill(match, character, commonService, 0, null);
			}

			if (ResponseType.Bang.equals(turnNode.getAction()) || ResponseType.Gatling.equals(turnNode.getAction())) {
				//skill hero ElenaFuente
				if(character.getHero() instanceof ElenaFuente) {
					character.getHero().useSkill(match, character, commonService, 0, null);
					commonService.notifyCharacter(match.getMatchId(), character, sessionId);
				} else {
					if (card instanceof MissedCard || (card instanceof BangCard && character.getHero().useSkill(card))) {
						if(card instanceof BangCard) {
							character.getHero().useSkill(match, character, commonService, 0, null);
						}
						commonService.notifyCharacter(match.getMatchId(), character, sessionId);
						if(ResponseType.Bang.equals(turnNode.getAction()) && turnNode.getCharacter().getHero() instanceof SlabTheKiller && !turnNode.getPlayerUsedMissed().contains(userName)) {
							turnNode.getCharacter().getHero().useSkill(match, character, commonService, 1, null);
							if (turnNode.getNextPlayer().peek() == null) {
								// request player in turn continue using card
								turnNode.requestPlayerUseCard();
							} else {
								turnNode.requestOtherPlayerUseCard(match);
							}
							return;
						} else {
							turnNode.getPlayerUsedMissed().clear();
							turnNode.getPlayerUsedBarrel().clear();
							turnNode.getPlayerSkillBarrel().clear();
						}
					} else {
						logger.error("%%%%%%%%%%%%%%%%%%%%%%%Bang Gatling Error");
					}
				}
				
			} else if (ResponseType.Indians.equals(turnNode.getAction())) {
				if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {
					if(card instanceof MissedCard) {
						character.getHero().useSkill(match, character, commonService, 0, null);
					}
					commonService.notifyCharacter(match.getMatchId(), character, sessionId);
				} else {
					logger.error("%%%%%%%%%%%%%%%%%%%%%%%Indians Error");
				}
			} else if (ResponseType.Duello.equals(turnNode.getAction())) {
				if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {
					if(card instanceof MissedCard) {
						character.getHero().useSkill(match, character, commonService, 0, null);
					}
					commonService.notifyCharacter(match.getMatchId(), character, sessionId);
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
	private void checkBelleStarSkill(Character character, TurnNode turnNode, Match match, List<String> targerUsers) {
		if(character.getHero() instanceof BelleStar) {
			boolean notify = false;
			// Bang Panic Gatling
			if(ResponseType.Bang.equals(turnNode.getAction()) || ResponseType.Panic.equals(turnNode.getAction())) {
				Character targerCharacter = match.getCharacterMap().get(targerUsers.get(0));
				Card card = BangUtils.getCardByCardType(targerCharacter.getCardsInFront(), MustangCard.class);
				if(card == null) {
					card = BangUtils.getCardByCardType(targerCharacter.getCardsInFront(), BarrelCard.class);
				}
				if(card != null) {
					notify = true;
				}
			} else if(ResponseType.Gatling.equals(turnNode.getAction())) {
				for (String targetUser : targerUsers) {
					Character targerCharacter = match.getCharacterMap().get(targetUser);
					Card card = BangUtils.getCardByCardType(targerCharacter.getCardsInFront(), BarrelCard.class);
					if(card != null) {
						notify = true;
						break;
					}
				}
			} 
			if(notify) {
				character.getHero().useSkill(match, character, commonService, 0, null);
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
				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardNotInTurn",
						new UseCardNotInTurnResponse(userName, CardUtils.losePointCard, null, null, null));
				character.setLifePoint(character.getLifePoint() - 1);
				// skill hero  BartCassidy
				if(character.getHero() instanceof BartCassidy) {
					Map<String, Object> others = new HashMap<>();
					others.put("numberNewCard", 1);
					character.getHero().useSkill(match, character, commonService, 1, others);
				}
				commonService.notifyCharacter(match.getMatchId(), character, sessionId);
				if (character.getLifePoint() == 0) {
					commonService.playerDead(userName, false, match, false);
					if(match.getPlayerTurnQueue().size() <=1) {
						//end game
						return;
					}
					commonService.callNextPlayerTurn(match, userName);
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
					if(card instanceof MissedCard) {
						character.getHero().useSkill(match, character, commonService, 0, null);
					}
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardNotInTurn",
							new UseCardNotInTurnResponse(userName, card, null, null, null));
					commonService.addToOldCardList(card, match);
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
//				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card));
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
				card.apply(character);
				commonService.notifyCharacter(match.getMatchId(), character, sessionId);
				// skill JohnnyKisch
				if(character.getHero() instanceof JohnnyKisch) {
					Map<String, Object> others = new HashMap<>();
					others.put("card", card);
					character.getHero().useSkill(match, character, commonService, 1, others);
				}
				// request continue use cards
				turnNode.requestPlayerUseCard();
				return;
			} else if (CardType.physical.equals(card.getCardType())) {
				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card, targetUser));
				commonService.addToOldCardList(card, match);
				commonService.notifyCharacter(match.getMatchId(), character, sessionId);
				if(card instanceof MissedCard) {
					character.getHero().useSkill(match, character, commonService, 0, null);
				}
				turnNode.setAction(ResponseType.Bang);
				checkBelleStarSkill(character, turnNode, match, Arrays.asList(targetUser));
				if(character.getHero() instanceof RoseDoolan) {
					character.getHero().useSkill(match, character, commonService, 0, null);
				}
				Character targetCharacter = match.getCharacterMap().get(targetUser);
				if(targetCharacter.getHero() instanceof PaulRegret) {
					targetCharacter.getHero().useSkill(match, targetCharacter, commonService, 0, null);
				}
				turnNode.setAlreadyUseBangCard(true);
				turnNode.getNextPlayer().clear();
				turnNode.getNextPlayer().add(targetUser);
				turnNode.requestOtherPlayerUseCard(match);
				return;
			} else {
				if (card instanceof BeerCard) {
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card));
					//skill hero TequilaJoe
					if(character.getHero() instanceof TequilaJoe) {
						character.getHero().useSkill(match, character, commonService, 1, null);
					} else {
						card.apply(character);
					}
					commonService.addToOldCardList(card, match);
					commonService.notifyCharacter(match.getMatchId(), character, sessionId);
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof GatlingCard) {
					LinkedList<String> otherPlayers = BangUtils.getOtherPlayer(match.getPlayerTurnQueue(), userName);
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card, otherPlayers));
					commonService.addToOldCardList(card, match);
					commonService.notifyCharacter(match.getMatchId(), character, sessionId);
					// skill hero ApacheKid
					commonService.useSkillOfApacheKid(match, otherPlayers, card, true);
					turnNode.setAction(ResponseType.Gatling);
					checkBelleStarSkill(character, turnNode, match, otherPlayers);
					turnNode.setNextPlayer(otherPlayers);
					turnNode.requestOtherPlayerUseCard(match);
					return;
				} else if (card instanceof SaloonCard) {
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card));
					commonService.addToOldCardList(card, match);
					List<String> players = new ArrayList<>(match.getPlayerTurnQueue());
					for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
						if(!players.contains(entry.getKey())) {
							continue;
						}
						card.apply(entry.getValue());
						// notify hero of this character to others
						String sessionIdPlayer = match.getUserMap().get(entry.getKey());
						commonService.notifyCharacter(match.getMatchId(), entry.getValue(),
								sessionIdPlayer);
					}
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof WellsFargoCard || card instanceof StageCoachCard) {
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card));
					commonService.addToOldCardList(card, match);
					int n = (card instanceof WellsFargoCard) ? 3 : 2;
					character.getCardsInHand().addAll(commonService.getFromNewCardList(match, n));
					character.setNumCardsInHand(character.getCardsInHand().size());
					commonService.notifyCharacter(match.getMatchId(), character, sessionId);
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof DynamiteCard) {
//					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card));
					// set dynamite
					card.apply(character);
					character.getCardsInFront().add(card);
					// notify
					commonService.notifyCharacter(match.getMatchId(), character, sessionId);
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				}  else if (card instanceof JailCard) {
//					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card, targetUser));
					// set jail or dynamite
					Character targetCharacter = match.getCharacterMap().get(targetUser);
					card.apply(targetCharacter);
					targetCharacter.getCardsInFront().add(card);
					// notify
					commonService.notifyCharacter(match.getMatchId(), targetCharacter, match.getUserMap().get(targetUser));
					// request continue use cards
					turnNode.requestPlayerUseCard();
					return;
				} else if (card instanceof IndiansCard) {
					LinkedList<String> otherPlayers = BangUtils.getOtherPlayer(match.getPlayerTurnQueue(), userName);
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card, otherPlayers));
					commonService.addToOldCardList(card, match);
					turnNode.setAction(ResponseType.Indians);
					// skill hero ApacheKid
					commonService.useSkillOfApacheKid(match, otherPlayers, card, true);
					turnNode.setNextPlayer(otherPlayers);
					turnNode.requestOtherPlayerUseCard(match);
					return;
				} else if (card instanceof DuelloCard) {
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card, targetUser));
					commonService.addToOldCardList(card, match);
					turnNode.setAction(ResponseType.Duello);
					turnNode.getNextPlayer().clear();
					turnNode.getNextPlayer().add(targetUser);
					turnNode.requestOtherPlayerUseCard(match);
					return;
				} else if (card instanceof PanicCard || card instanceof CatPalouCard) {
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card, targetUser));
					commonService.addToOldCardList(card, match);
					ResponseType turnAction = card instanceof PanicCard ? ResponseType.Panic : ResponseType.CatPalou;
					turnNode.setAction(turnAction);
					if(card instanceof PanicCard) {
						checkBelleStarSkill(character, turnNode, match, Arrays.asList(targetUser));
						if(character.getHero() instanceof RoseDoolan) {
							character.getHero().useSkill(match, character, commonService, 0, null);
						}
						Character targetCharacter = match.getCharacterMap().get(targetUser);
						if(targetCharacter.getHero() instanceof PaulRegret) {
							targetCharacter.getHero().useSkill(match, targetCharacter, commonService, 0, null);
						}
					}
					turnNode.getNextPlayer().clear();
					turnNode.getNextPlayer().add(targetUser);
					turnNode.requestOtherPlayerUseCard(match);
					return;
				} else if (card instanceof GeneralStoreCard) {
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), card));
					commonService.addToOldCardList(card, match);
					turnNode.setAction(ResponseType.GeneralStore);
					turnNode.getCardTemp().clear();
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
