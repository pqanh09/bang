package com.example.springboot.command;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.request.Request;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class DynamiteActionCmd extends AbsActionCmd implements ActionCmd {

	

	public DynamiteActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) {
		String userName = request.getUser();
		String sessionId = match.getUserMap().get(userName);
		
		if(!match.getCurrentTurn().getCharacter().getUserName().equals(userName)) {
			return;
		}
		//get Character
		Character character = match.getCharacterMap().get(userName);
		// get cards for escaping the jail;
		Card card = commonService.getFromNewCardList(match, 1).get(0);
		
		//TODO ....
		commonService.addToOldCardList(card, match);
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.DrawCardDynamite, card, null));
		
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
		
		if(Suit.spades.equals(card.getSuit())) {
			commonService.addToOldCardList(dynamiteCard, match);
			 
			character.setLifePoint(character.getLifePoint() - 3);
			BangUtils.notifyCharacter(simpMessageSendingOperations, character, sessionId);
			if (character.getLifePoint() <= 0) {
				commonService.playerDead(userName, false, match);
				commonService.callNextPlayerTurn(match);
			} else {
				match.getCurrentTurn().run();
			}
		} else {
			
			BangUtils.notifyCharacter(simpMessageSendingOperations, character, sessionId);
			
			match.getPlayerTurnQueue().poll();
			String nextPlayer = match.getPlayerTurnQueue().peek();
			match.getPlayerTurnQueue().addFirst(userName);
			Character nextCharacter = match.getCharacterMap().get(nextPlayer);
			nextCharacter.getCardsInFront().add(dynamiteCard);
			nextCharacter.setHasDynamite(true);
			String sessionIdNextPlayer = match.getUserMap().get(nextPlayer);
			BangUtils.notifyCharacter(simpMessageSendingOperations, nextCharacter, sessionIdNextPlayer);
			match.getCurrentTurn().run();
		}
	}

}
