package com.example.springboot.command;

import java.util.ArrayList;
import java.util.List;
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
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.card.CatPalouCard;
import com.example.springboot.model.card.DuelloCard;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.model.card.PanicCard;
import com.example.springboot.model.card.SaloonCard;
import com.example.springboot.model.card.VolcanicCard;
import com.example.springboot.model.hero.ApacheKid;
import com.example.springboot.model.hero.CalamityJanet;
import com.example.springboot.model.hero.ElenaFuente;
import com.example.springboot.model.role.RoleType;
import com.example.springboot.request.Request;
import com.example.springboot.response.CheckCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class CheckCardActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(CheckCardActionCmd.class);
	public CheckCardActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}
	@Override
	public void execute(Request request, Match match) throws Exception {
		// get turn node
		TurnNode turnNode = match.getCurrentTurn();
		String userName = request.getUser();
		if (turnNode.getCharacter().getUserName().equals(userName)) {
			processUseCardInTurn(request, match);
		} else {
			processUseCardNotInTurn(request, match);
		}
	}

	private void processUseCardNotInTurn(Request request, Match match) {
		String userName = request.getUser();
		TurnNode turnNode = match.getCurrentTurn();
		
		if (!turnNode.getNextPlayer().peek().equals(userName)) {
			return;
		}
		String sessionId = match.getUserMap().get(userName);
		// get Character
		Character character = match.getCharacterMap().get(userName);
		// get card from id
		Card card = BangUtils.findCardInHand(character, request.getId());
		if (card == null) {
			simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
					new CheckCardResponse(false, "Not found card " + request.getId()));
			logger.error("Not found card {}", request.getId());
			return;
		}
		if (ResponseType.Bang.equals(turnNode.getAction()) || ResponseType.Gatling.equals(turnNode.getAction())) {
			//skill hero ElenaFuente
			if(character.getHero() instanceof ElenaFuente) {
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(true, ""));
				return;
			}
			// skill hero CalamityJanet
			if (card instanceof MissedCard || (card instanceof BangCard && character.getHero() instanceof CalamityJanet)) {
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(true, ""));
				return;
			}
			simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
					new CheckCardResponse(false, "Can't use this card."));
			return;
		} else if (ResponseType.Duello.equals(turnNode.getAction()) || ResponseType.Indians.equals(turnNode.getAction())) {
			if (card instanceof BangCard || (card instanceof MissedCard && character.getHero() instanceof CalamityJanet)) {
				if(ResponseType.Duello.equals(turnNode.getAction()) && Suit.diamonds.equals(card.getSuit()) && turnNode.getCharacter().getHero() instanceof ApacheKid) {
					simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
							new CheckCardResponse(false, "Can't use diamonds card with hero ApachaKid."));
				} else {
					simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
							new CheckCardResponse(true, ""));
				}
				
			} else {
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(false, "Can't use this card"));
			}
			return;
		} else {
			logger.error("CheckCardActionCmd  processUseCardNotInTurn ERROR");
		}

	}
	private void processUseCardInTurn(Request request, Match match) {
		String userName = request.getUser();
		TurnNode turnNode = match.getCurrentTurn();
		String sessionId = match.getUserMap().get(userName);
		// get Character
		Character character = match.getCharacterMap().get(userName);
		// get card from id
		Card card = BangUtils.findCardInHand(character, request.getId());
		if (card == null) {
			simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
					new CheckCardResponse(false, "Not found card " + request.getId()));
			return;
		}
		
		//duello
		if(ResponseType.Duello.equals(turnNode.getAction())) {
			if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(true, ""));
			} else {
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(false, "Can't use this card."));
			}
			return;
		}
		// bang card
		if (CardType.physical.equals(card.getCardType())) {
			boolean canUseCard = false;
			String message = "";
			if (card instanceof BangCard) {
				// check alreadyUseBangCard in turnNode
				canUseCard = !match.getCurrentTurn().isAlreadyUseBangCard();
				// check hero skill WillyTheKid
				if (!canUseCard) {
					message = "Only use 1 bang card in your turn";
					canUseCard = character.getHero().useSkill(card);
				}
				// check weapon VolcanicCard
				if (!canUseCard) {
					canUseCard = BangUtils.hasCard(character, VolcanicCard.class);
				} else {
					message = "";
				}
			}
			// check if it is Missed Card
			else {
				// check hero skill CalamityJanet
				canUseCard = character.getHero().useSkill(card);
			}
			// return false
			if (!canUseCard) {
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(false, "Can't use this card."));
				return;
			}
			// check range to use
			else {
				CheckCardResponse checkCardResponse = new CheckCardResponse(false, new ArrayList<String>(),
						false, "");
				List<String> userCanBeAffectList = commonService.checkRangeToUseCard(match, character,character.getGun(), checkCardResponse);
				if(userCanBeAffectList.isEmpty()) {
					simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard", checkCardResponse);
					return;
				} else {
					checkCardResponse.setMessage("");
				}
				// skill hero ApacheKid
				int numberPlayer = userCanBeAffectList.size();
				commonService.useSkillOfApacheKid(match, userCanBeAffectList, card, false);
				if(numberPlayer == 1  && userCanBeAffectList.isEmpty()) {
					checkCardResponse.setMessage("Can't use diamonds card with hero ApachaKid.");
				}
				checkCardResponse.setCanUse(!userCanBeAffectList.isEmpty());
				checkCardResponse.setMustChooseTarget(!userCanBeAffectList.isEmpty());
				checkCardResponse.setUserCanBeAffectList(userCanBeAffectList);
				
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard", checkCardResponse);
				return;
			}
		}
		// magic card
		else if (CardType.magic.equals(card.getCardType())) {
			// BeerCard
			if (card instanceof BeerCard) {
				if (character.getLifePoint() < character.getCapacityLPoint()) {
					if(match.getPlayerTurnQueue().size() > 2) {
						simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
								new CheckCardResponse(true, ""));
					} else {
						simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
								new CheckCardResponse(false, "Can't use Beer card when there are only 2 players"));
					}
				} else {
					simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
							new CheckCardResponse(false, "You don't lose any life point."));
				}
				return;
			}
			// PanicCard
			else if (card instanceof PanicCard) {
				CheckCardResponse checkCardResponse = new CheckCardResponse(false, new ArrayList<String>(),
						false, "");
				List<String> temp = commonService.checkRangeToUseCard(match, character, 1, checkCardResponse);
				if(temp.isEmpty()) {
					simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard", checkCardResponse);
					return;
				} else {
					checkCardResponse.setMessage("");
				}
				// check number card of user
				List<String> userCanBeAffectList = new ArrayList<>();
				for (String user : temp) {
					Character chrter = match.getCharacterMap().get(user);
					if(!chrter.getCardsInFront().isEmpty() || !chrter.getCardsInHand().isEmpty()) {
						userCanBeAffectList.add(user);
					}
				}
				if(temp.size() == 1 && userCanBeAffectList.isEmpty()) {
					checkCardResponse.setMessage("Not have any card to select.");
					simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard", checkCardResponse);
					return;
				}
				// skill hero ApacheKid
				int numberPlayer = userCanBeAffectList.size();
				commonService.useSkillOfApacheKid(match, userCanBeAffectList, card, false);
				if(numberPlayer == 1  && userCanBeAffectList.isEmpty()) {
					checkCardResponse.setMessage("Can't use diamonds card with hero ApachaKid.");
				}
				
				checkCardResponse.setCanUse(!userCanBeAffectList.isEmpty());
				checkCardResponse.setMustChooseTarget(!userCanBeAffectList.isEmpty());
				checkCardResponse.setUserCanBeAffectList(userCanBeAffectList);
				
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard", checkCardResponse);
				return;
			}
			// DuelloCard
			else if (card instanceof DuelloCard) {
				List<String> userCanBeAffectList = new ArrayList<>(BangUtils.getOtherPlayer(match.getPlayerTurnQueue(), userName));
				// skill hero ApacheKid
				commonService.useSkillOfApacheKid(match, userCanBeAffectList, card, false);
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(!userCanBeAffectList.isEmpty(), userCanBeAffectList, true, ""));
				return;
			} 
			//SaloonCard
			else if (card instanceof SaloonCard) {
				String message = "";
				boolean findPlayerLoseLifePoint = false;
				for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
					if(entry.getValue().getLifePoint() < entry.getValue().getCapacityLPoint() && match.getPlayerTurnQueue().contains(entry.getKey())) {
						findPlayerLoseLifePoint = true;
						break;
					}
				}
				if(!findPlayerLoseLifePoint) {
					message = "There are no players lost life point.";
				}
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(findPlayerLoseLifePoint, message));
				return;
			}
			//CatPalouCard
			else if (card instanceof CatPalouCard) {
				// check number card of user
				
				List<String> userCanBeAffectList = new ArrayList<>();
				Character targetCharater;
				for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
					if(!match.getPlayerTurnQueue().contains(entry.getKey())) {
						continue;
					}
					targetCharater = entry.getValue();
					if(targetCharater.getUserName().equals(userName) 
							|| !match.getPlayerTurnQueue().contains(entry.getKey()) 
							||(targetCharater.getCardsInHand().isEmpty() && targetCharater.getCardsInFront().isEmpty())) {
						continue;
					}
					userCanBeAffectList.add(entry.getKey());
				}
				String message = "";
				// skill hero ApacheKid
				int numberPlayer = userCanBeAffectList.size();
				commonService.useSkillOfApacheKid(match, userCanBeAffectList, card, false);
				if(numberPlayer == 1  && userCanBeAffectList.isEmpty()) {
					message = "Can't use diamonds card with hero ApachaKid.";
				}
				
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(!userCanBeAffectList.isEmpty(), userCanBeAffectList,
								!userCanBeAffectList.isEmpty(), message));
				return;
			} 
			// JailCard
			else if (card instanceof JailCard) {
				// check number card of user
				List<String> userCanBeAffectList = new ArrayList<>();
				Character targetCharater;
				for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
					if(!match.getPlayerTurnQueue().contains(entry.getKey())) {
						continue;
					}
					targetCharater = entry.getValue();
					if(targetCharater.getUserName().equals(userName) || RoleType.SCERIFFO.equals(targetCharater.getRole().getRoleType()) || targetCharater.isBeJailed()) {
						continue;
					}
					userCanBeAffectList.add(entry.getKey());
				}
				String message = "";
				// skill hero ApacheKid
				int numberPlayer = userCanBeAffectList.size();
				commonService.useSkillOfApacheKid(match, userCanBeAffectList, card, false);
				if(numberPlayer == 1  && userCanBeAffectList.isEmpty()) {
					message = "Can't use diamonds card with hero ApachaKid.";
				}
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(!userCanBeAffectList.isEmpty(), userCanBeAffectList,
								!userCanBeAffectList.isEmpty(), message));
				return;
			}
			// Dynamite
			else if (card instanceof DynamiteCard) {
				boolean alreadyDynamiteCard = false;
				Character targetCharater;
				for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
					targetCharater = entry.getValue();
					for (Card cd : targetCharater.getCardsInFront()) {
						if(cd instanceof DynamiteCard) {
							alreadyDynamiteCard = true;
							break;
						}
					}
				}
				String message = "";
				if(alreadyDynamiteCard) {
					message = "There is only 1 Dynamite at the same time.";
				}
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
						new CheckCardResponse(!alreadyDynamiteCard, message));
				return;
			}
		}
		simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/checkcard",
				new CheckCardResponse(true, ""));
	}

}
