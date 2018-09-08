package com.example.springboot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.example.springboot.model.Character;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.request.Request;
import com.example.springboot.response.CardResponse;
import com.example.springboot.response.RemoveCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

public class RemoveCardActionCmd extends AbsActionCmd implements ActionCmd {

	

	public RemoveCardActionCmd(TableService tableService, HeroService heroService, ShareService shareService, TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
	}

	@Override
	public void execute(Request request) {
		String userName = request.getUser();
		String sessionId = tableService.getUserMap().get(userName);
		//get Character
		Character character = tableService.getCharacterMap().get(userName);
		// get cards for removing;
		List<Card> cards = new ArrayList<>();
		Card card = BangUtils.getCardInHand(character, request.getId());
		if(card != null) {
			cards.add(card);
			tableService.addToOldCardList(card);
		}
		BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
		tableService.getMessagingTemplate().convertAndSend("/topic/removecardendturn", new RemoveCardResponse(userName, cards));
		
		//check number card is  not ok
		if(character.getCardsInHand().size() > character.getLifePoint()) {
			tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/removecardendturn", new RemoveCardResponse(userName, character.getCardsInHand()));
			return;
		}
		
		// if ok -> next turn
		turnService.endTurn(userName, false);
	}

}
