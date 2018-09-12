package com.example.springboot.command;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.request.Request;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class GetCardActionCmd extends AbsActionCmd implements ActionCmd {

	

	public GetCardActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
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
		// get cards for character;
		List<Card> cards = commonService.getFromNewCardList(match, Constants.DEFAULT_CARD);
		character.getCardsInHand().addAll(cards);
		character.setNumCardsInHand(character.getCardsInHand().size());
		//udpate character for user 
		BangUtils.notifyCharacter(simpMessageSendingOperations, character, sessionId);
		// update alreadyGetCard = true
		match.getCurrentTurn().setAlreadyGetCard(true);
		match.getCurrentTurn().requestPlayerUseCard();
	}

}
