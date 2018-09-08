package com.example.springboot.command;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.example.springboot.model.Character;
import com.example.springboot.model.TurnNode;
import com.example.springboot.request.Request;
import com.example.springboot.response.CardResponse;
import com.example.springboot.response.RemoveCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;

public class EndTurnActionCmd extends AbsActionCmd implements ActionCmd {

	public EndTurnActionCmd(TableService tableService, HeroService heroService, ShareService shareService,
			TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
	}

	@Override
	public void execute(Request request) {
		String userName = request.getUser();
		String sessionId = tableService.getUserMap().get(userName);
		TurnNode turnNode = turnService.getCurrentTurn();
		turnNode.setAlreadyUseBangCard(true);
		//check number card before ending
		Character character = tableService.getCharacterMap().get(userName);
		if(character.getCardsInHand().size() > character.getLifePoint()) {
			tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/removecardendturn", new RemoveCardResponse(userName, character.getCardsInHand()));
			return;
		}
		
		// if ok -> next turn
		turnService.endTurn(userName, false);
		
	}

}
