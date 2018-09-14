package com.example.springboot.command;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.request.Request;
import com.example.springboot.response.RemoveCardResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class RemoveCardActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(RemoveCardActionCmd.class);
	

	public RemoveCardActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		String userName = request.getUser();
		String sessionId = match.getUserMap().get(userName);
		//get Character
		Character character = match.getCharacterMap().get(userName);
		// get cards for removing;
		List<Card> cards = new ArrayList<>();
		Card card = BangUtils.getCardInHand(character, request.getId());
		if(card != null) {
			cards.add(card);
			commonService.addToOldCardList(card, match);
		}
		BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/removecardendturn", new RemoveCardResponse(userName, cards));
		
		//check number card is  not ok
		if(character.getCardsInHand().size() > character.getLifePoint()) {
			simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/removecardendturn", new RemoveCardResponse(userName, character.getCardsInHand()));
			return;
		}
		
		// if ok -> next turn
		commonService.endTurn(userName, match);
	}

}
