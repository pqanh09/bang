package com.example.springboot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(JailActionCmd.class);
	

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
		
		BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
		
		commonService.addToOldCardList(jailCard, match);
		if(Suit.hearts.equals(card.getSuit())) {
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.EscapeJail, null, null));
			match.getCurrentTurn().run();
		} else {
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.LostTurn, null, null));
			commonService.endTurn(userName, match);
		}
	}

}
