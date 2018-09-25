package com.example.springboot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.hero.SeanMallory;
import com.example.springboot.request.Request;
import com.example.springboot.response.RemoveCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.CommonService;

public class EndTurnActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(EndTurnActionCmd.class);
	public EndTurnActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		String userName = request.getUser();
		String sessionId = match.getUserMap().get(userName);
		TurnNode turnNode = match.getCurrentTurn();
		turnNode.setAlreadyUseBangCard(true);
		//check number card before ending
		Character character = match.getCharacterMap().get(userName);
		if(character.getCardsInHand().size() > character.getLifePoint()) {
			if(character.getHero() instanceof SeanMallory) {
				if(character.getHero().useSkill(match, character, commonService, 1, null)) {
					//character.getCardsInHand().size() > 10)
					simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/removecard", new RemoveCardResponse(userName, ResponseType.RemoveCardEndTurn, character.getCardsInHand(), character.getCardsInHand().size() - 10, 30));
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/cardaction", new UserResponse(ResponseType.RemoveCardEndTurn, userName, 30));
					return;
				}
			} else {
				simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/removecard", new RemoveCardResponse(userName, ResponseType.RemoveCardEndTurn, character.getCardsInHand(), character.getCardsInHand().size() - character.getLifePoint(), 30));
				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/cardaction", new UserResponse(ResponseType.RemoveCardEndTurn, userName, 30));
				return;
			}
			
		}
		// if ok -> next turn
		commonService.endTurn(userName, match);
		
	}

}
