package com.example.springboot.command;

import java.util.ArrayList;
import java.util.List;
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
import com.example.springboot.model.card.JailCard;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.model.card.PanicCard;
import com.example.springboot.model.card.SaloonCard;
import com.example.springboot.model.card.VolcanicCard;
import com.example.springboot.model.role.RoleType;
import com.example.springboot.request.Request;
import com.example.springboot.response.CheckCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

public class CheckCardActionCmd extends AbsActionCmd implements ActionCmd {

	public CheckCardActionCmd(TableService tableService, HeroService heroService, ShareService shareService,
			TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
	}

	@Override
	public void execute(Request request) {
		// get turn node
		TurnNode turnNode = turnService.getCurrentTurn();
		String userName = request.getUser();
		if (turnNode.getCharacter().getUserName().equals(userName)) {
			processUseCardInTurn(request, turnNode);
		} else {
			processUseCardNotInTurn(request, turnNode);
		}

	}

	private void processUseCardNotInTurn(Request request, TurnNode turnNode) {
		String userName = request.getUser();
		if (!turnNode.getNextPlayer().peek().equals(userName)) {
			return;
		}
		String sessionId = tableService.getUserMap().get(userName);
		// get Character
		Character character = tableService.getCharacterMap().get(userName);
		// get card from id
		Card card = BangUtils.findCardInHand(character, request.getId());
		if (card == null) {
			tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
					new CheckCardResponse(false));
			return;
		}
		if (ResponseType.Bang.equals(turnNode.getAction()) || ResponseType.Gatling.equals(turnNode.getAction())) {
			if (card instanceof MissedCard || (card instanceof BangCard && character.getHero().useSkill(card))) {
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(true));
				return;
			}
			tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
					new CheckCardResponse(false));
			return;
		} else if (ResponseType.Duello.equals(turnNode.getAction()) || ResponseType.Indians.equals(turnNode.getAction())) {
			if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(true));
			} else {
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(false));
			}
			return;
		} else {
			System.out.println("CheckCardActionCmd  processUseCardNotInTurn ERROR");
		}

	}

	private void processUseCardInTurn(Request request, TurnNode turnNode) {
		String userName = request.getUser();
		String sessionId = tableService.getUserMap().get(userName);
		// get Character
		Character character = tableService.getCharacterMap().get(userName);
		// get card from id
		Card card = BangUtils.findCardInHand(character, request.getId());
		if (card == null) {
			tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
					new CheckCardResponse(false));
			return;
		}
		
		//duello
		if(ResponseType.Duello.equals(turnNode.getAction())) {
			if (card instanceof BangCard || (card instanceof MissedCard && character.getHero().useSkill(card))) {
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(true));
			} else {
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(false));
			}
			return;
		}
		// bang card
		if (CardType.physical.equals(card.getCardType())) {
			boolean canUseCard = false;
			if (card instanceof BangCard) {
				// check alreadyUseBangCard in turnNode
				canUseCard = !turnService.getCurrentTurn().isAlreadyUseBangCard();
				// check hero skill WillyTheKid
				if (!canUseCard) {
					canUseCard = character.getHero().useSkill(card);
				}
				// check weapon VolcanicCard
				if (!canUseCard) {
					canUseCard = BangUtils.hasCard(character, VolcanicCard.class);
				}
			}
			// check if it is Missed Card
			else {
				// check hero skill CalamityJanet
				canUseCard = character.getHero().useSkill(card);
			}
			// return false
			if (!canUseCard) {
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(false));
				return;
			}
			// check range to use
			else {
				List<String> userCanBeAffectList = BangUtils.checkRangeToUseCard(tableService.getRangeMap(), character,
						tableService.getCharacterMap(), character.getGun(), tableService.getPlayerTurnQueue());
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(!userCanBeAffectList.isEmpty(), userCanBeAffectList,
								!userCanBeAffectList.isEmpty()));
				return;
			}
		}
		// magic card
		else if (CardType.magic.equals(card.getCardType())) {
			// BeerCard
			if (card instanceof BeerCard) {
				if (character.getLifePoint() < character.getCapacityLPoint()) {
					tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
							new CheckCardResponse(true));
				} else {
					tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
							new CheckCardResponse(false));
				}
				return;
			}
			// PanicCard
			else if (card instanceof PanicCard) {
				List<String> temp = BangUtils.checkRangeToUseCard(tableService.getRangeMap(), character,
						tableService.getCharacterMap(), 1, tableService.getPlayerTurnQueue());
				// check number card of user
				List<String> userCanBeAffectList = new ArrayList<>();
				for (String user : temp) {
					Character chrter = tableService.getCharacterMap().get(user);
					if(!chrter.getCardsInFront().isEmpty() || !chrter.getCardsInHand().isEmpty()) {
						userCanBeAffectList.add(user);
					}
				}
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(!userCanBeAffectList.isEmpty(), userCanBeAffectList,
								!userCanBeAffectList.isEmpty()));
				return;
			}
			// DuelloCard
			else if (card instanceof CatPalouCard || card instanceof DuelloCard) {
				List<String> userCanBeAffectList = BangUtils.getOtherPlayer(tableService.getCharacterMap().keySet(),
						userName);
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(true, userCanBeAffectList, true));
				return;
			} 
			//SaloonCard
			else if (card instanceof SaloonCard) {
				boolean findPlayerLoseLifePoint = false;
				for (Entry<String, Character> entry : tableService.getCharacterMap().entrySet()) {
					if(entry.getValue().getLifePoint() < entry.getValue().getCapacityLPoint()) {
						findPlayerLoseLifePoint = true;
						break;
					}
				}
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(findPlayerLoseLifePoint));
				return;
			}
			//CatPalouCard
			else if (card instanceof CatPalouCard) {
				// check number card of user
				List<String> userCanBeAffectList = new ArrayList<>();
				Character targetCharater;
				for (Entry<String, Character> entry : tableService.getCharacterMap().entrySet()) {
					targetCharater = entry.getValue();
					if(targetCharater.getUserName().equals(userName) || !tableService.getPlayerTurnQueue().contains(entry.getKey())) {
						continue;
					}
					userCanBeAffectList.add(entry.getKey());
				}
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(!userCanBeAffectList.isEmpty(), userCanBeAffectList,
								!userCanBeAffectList.isEmpty()));
				return;
			} 
			// JailCard
			else if (card instanceof JailCard) {
				// check number card of user
				List<String> userCanBeAffectList = new ArrayList<>();
				Character targetCharater;
				for (Entry<String, Character> entry : tableService.getCharacterMap().entrySet()) {
					targetCharater = entry.getValue();
					if(targetCharater.getUserName().equals(userName) || RoleType.SCERIFFO.equals(targetCharater.getRoleType())) {
						continue;
					}
					userCanBeAffectList.add(entry.getKey());
				}
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(!userCanBeAffectList.isEmpty(), userCanBeAffectList,
								!userCanBeAffectList.isEmpty()));
				return;
			}
			// Dynamite
			else if (card instanceof DynamiteCard) {
				boolean alreadyDynamiteCard = false;
				Character targetCharater;
				for (Entry<String, Character> entry : tableService.getCharacterMap().entrySet()) {
					targetCharater = entry.getValue();
					for (Card cd : targetCharater.getCardsInFront()) {
						if(cd instanceof DynamiteCard) {
							alreadyDynamiteCard = true;
							break;
						}
					}
				}
				tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
						new CheckCardResponse(!alreadyDynamiteCard));
				return;
			}
		}
		tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/checkcard",
				new CheckCardResponse(true));
	}

}
