package com.example.springboot.command;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.hero.BartCassidy;
import com.example.springboot.request.Request;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.CommonService;

public class DynamiteActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(DynamiteActionCmd.class);
	

	public DynamiteActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		String userName = request.getUser();
		if(!match.getCurrentTurn().getCharacter().getUserName().equals(userName)) {
			logger.error("Error not support for " + userName + "at this time");
			return;
		}
		// get cards for escaping the Dymanite;
		Card card = commonService.getFromNewCardList(match, 1).get(0);
		checkDynamite(match, card, commonService);
	}
	public static void checkDynamite(Match match, Card card, CommonService commonService) {
		//get Character
		Character character = match.getCurrentTurn().getCharacter();
		String userName = character.getUserName();
		String sessionId = match.getUserMap().get(userName);
		
		commonService.addToOldCardList(card, match);
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.DrawCardDynamite, card, null));
		
		character.setHasDynamite(false);
		
		// find dynamite card
		Card dynamiteCard = null;
		for (Card cd : character.getCardsInFront()) {
			if(cd instanceof DynamiteCard) {
				dynamiteCard = cd;
				break;
			}
		}
		character.getCardsInFront().remove(dynamiteCard);
		match.getCurrentTurn().setAlreadyCheckedDynamite(true);
		if(Suit.spades.equals(card.getSuit()) && card.getNumber() >= 2 && card.getNumber() <=9) {
			commonService.addToOldCardList(dynamiteCard, match);
			character.setLifePoint(character.getLifePoint() - 3);
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.LostLifePoint, null, null));
			// skill hero  BartCassidy
			if(character.getHero() instanceof BartCassidy) {
				Map<String, Object> others = new HashMap<>();
				others.put("numberNewCard", 3);
				character.getHero().useSkill(match, character, commonService, 1, others);
			}
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			if (character.getLifePoint() <= 0) {
				commonService.playerDead(userName, false, match);
				if(match.getPlayerTurnQueue().size() <=1) {
					//end game
					return;
				}
				commonService.callNextPlayerTurn(match, userName);
			} else {
				match.getCurrentTurn().run(match);
			}
		} else {
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			match.getPlayerTurnQueue().poll();
			String nextPlayer = match.getPlayerTurnQueue().peek();
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.EscapeDynamite, null, nextPlayer));
			match.getPlayerTurnQueue().addFirst(userName);
			Character nextCharacter = match.getCharacterMap().get(nextPlayer);
			nextCharacter.getCardsInFront().add(dynamiteCard);
			nextCharacter.setHasDynamite(true);
			String sessionIdNextPlayer = match.getUserMap().get(nextPlayer);
			commonService.notifyCharacter(match.getMatchId(), nextCharacter, sessionIdNextPlayer);
			match.getCurrentTurn().run(match);
		}
	}

}
