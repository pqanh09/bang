package com.example.springboot.command;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.card.Card;
import com.example.springboot.request.Request;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

public class GetCardActionCmd extends AbsActionCmd implements ActionCmd {

	

	public GetCardActionCmd(TableService tableService, HeroService heroService, ShareService shareService, TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
	}

	@Override
	public void execute(Request request) {
		String userName = request.getUser();
		if(!turnService.getCurrentTurn().getCharacter().getUserName().equals(userName)) {
			return;
		}
		String sessionId = tableService.getUserMap().get(userName);
		//get Character
		Character character = tableService.getCharacterMap().get(userName);
		// get cards for character;
		List<Card> cards = tableService.getFromNewCardList(Constants.DEFAULT_CARD);
		character.getCardsInHand().addAll(cards);
		character.setNumCardsInHand(character.getCardsInHand().size());
		//udpate character for user 
		BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
		// update alreadyGetCard = true
		turnService.getCurrentTurn().setAlreadyGetCard(true);
		turnService.getCurrentTurn().requestPlayerUseCard();
	}

}
