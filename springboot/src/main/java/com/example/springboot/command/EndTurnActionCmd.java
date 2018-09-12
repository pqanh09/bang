package com.example.springboot.command;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.request.Request;
import com.example.springboot.response.RemoveCardResponse;
import com.example.springboot.service.CommonService;

public class EndTurnActionCmd extends AbsActionCmd implements ActionCmd {

	public EndTurnActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) {
		String userName = request.getUser();
		String sessionId = match.getUserMap().get(userName);
		TurnNode turnNode = match.getCurrentTurn();
		turnNode.setAlreadyUseBangCard(true);
		//check number card before ending
		Character character = match.getCharacterMap().get(userName);
		if(character.getCardsInHand().size() > character.getLifePoint()) {
			simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/removecardendturn", new RemoveCardResponse(userName, character.getCardsInHand()));
			return;
		}
		
		// if ok -> next turn
		commonService.endTurn(userName, match);
		
	}

}
