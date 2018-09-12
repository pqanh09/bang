package com.example.springboot.command;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.request.Request;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class JailActionCmd extends AbsActionCmd implements ActionCmd {

	

	public JailActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) {
		String userName = request.getUser();
		if(!match.getCurrentTurn().getCharacter().getUserName().equals(userName)) {
			return;
		}
		String sessionId = match.getUserMap().get(userName);
		
		//get Character
		Character character = match.getCharacterMap().get(userName);
		// get cards for escaping the jail;
		Card card = commonService.getFromNewCardList(match, 1).get(0);
		
		//TODO ....
		commonService.addToOldCardList(card, match);
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.DrawCardJail, card, null));
		
		character.setBeJailed(false);
		// find dynamite card
		Card jailCard = null;
		for (Card cd : character.getCardsInFront()) {
			if(cd instanceof JailCard) {
				jailCard = cd;
				break;
			}
		}
		character.getCardsInFront().remove(jailCard);
		
		BangUtils.notifyCharacter(simpMessageSendingOperations, character, sessionId);
		
		commonService.addToOldCardList(jailCard, match);
		if(Suit.hearts.equals(card.getSuit())) {
			match.getCurrentTurn().run();
		} else {
			commonService.endTurn(userName, match);
		}
	}

}
