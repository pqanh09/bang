package com.example.springboot.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.BarrelCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.hero.BartCassidy;
import com.example.springboot.request.Request;
import com.example.springboot.response.OldCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardNotInTurnResponse;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;
import com.example.springboot.utils.CardUtils;

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
		
		
		
		character.setHasDynamite(false);
		
		// find dynamite card
		Card dynamiteCard = BangUtils.getCardByCardType(character.getCardsInFront(),DynamiteCard.class);
		character.getCardsInFront().remove(dynamiteCard);
		match.getCurrentTurn().setAlreadyCheckedDynamite(true);
		
		List<Card> cards = new ArrayList<>();
//		cards.add(card);
		
		if(Suit.spades.equals(card.getSuit()) && card.getNumber() >= 2 && card.getNumber() <=9) {
			commonService.addToOldCardList(dynamiteCard, match);
			commonService.addToOldCardList(card, match);
			character.setLifePoint(character.getLifePoint() - 3);
			cards.add(CardUtils.lose3PointCard);
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/usedCardNotInTurn", new UseCardNotInTurnResponse(userName, cards, null, null));
			// skill hero  BartCassidy
			if(character.getHero() instanceof BartCassidy) {
				Map<String, Object> others = new HashMap<>();
				others.put("numberNewCard", 3);
				character.getHero().useSkill(match, character, commonService, 1, others);
			}
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			if (character.getLifePoint() <= 0) {
				commonService.playerDead(userName, false, match, false);
				if(match.getPlayerTurnQueue().size() <=1) {
					//end game
					return;
				}
				commonService.callNextPlayerTurn(match, userName);
			} else {
				match.getCurrentTurn().run(match);
			}
		} else {
			commonService.addToOldCardList(card, match);
			cards.add(CardUtils.escapeDynamiteCard);
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/usedCardNotInTurn", new UseCardNotInTurnResponse(userName, cards, null, null));
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			match.getPlayerTurnQueue().poll();
			String nextPlayer = match.getPlayerTurnQueue().peek();
			match.getPlayerTurnQueue().addFirst(userName);
			Character nextCharacter = match.getCharacterMap().get(nextPlayer);
			nextCharacter.getCardsInFront().add(dynamiteCard);
			nextCharacter.setHasDynamite(true);
			String sessionIdNextPlayer = match.getUserMap().get(nextPlayer);
			commonService.notifyCharacter(match.getMatchId(), nextCharacter, sessionIdNextPlayer);
			match.getCurrentTurn().run(match);
		}
		OldCardResponse oldCardResponse = new OldCardResponse();
		oldCardResponse.getCards().add(card);
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/oldcard", oldCardResponse);
	}

}
