package com.example.springboot.command;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.request.Request;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.CommonService;

public class BarrelActionCmd extends AbsActionCmd implements ActionCmd {


	public BarrelActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) {
		String userName = request.getUser();
		SimpMessageSendingOperations simpMessageSendingOperations = commonService.getSimpMessageSendingOperations();
		// get cards for escaping the bang/gatlling;
		Card card = commonService.getFromNewCardList(match, 1).get(0);
		
		
		//TODO ....
		commonService.addToOldCardList(card, match);
		
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.UseBarrel, card, null));
		
		TurnNode turnNode = match.getCurrentTurn();
		turnNode.getPlayerUsedBarrel().add(userName);
		if(Suit.hearts.equals(card.getSuit())) {
			turnNode.getNextPlayer().poll();
			if (turnNode.getNextPlayer().peek() == null) {
				// request player in turn continue using card
				turnNode.requestPlayerUseCard();
			} else {
				turnNode.requestOtherPlayerUseCard(match);
			}
		} else {
			turnNode.requestOtherPlayerUseCard(match);
		}
	}


}
