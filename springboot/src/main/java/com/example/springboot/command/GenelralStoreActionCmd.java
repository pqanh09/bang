package com.example.springboot.command;

import java.util.List;

import com.example.springboot.model.Character;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.request.Request;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

public class GenelralStoreActionCmd extends AbsActionCmd implements ActionCmd {

	

	public GenelralStoreActionCmd(TableService tableService, HeroService heroService, ShareService shareService, TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
	}

	@Override
	public void execute(Request request) {
		TurnNode turnNode = turnService.getCurrentTurn();
		// current player
		String userName = request.getUser();
		String sessionId = tableService.getUserMap().get(userName);
		Character character = tableService.getCharacterMap().get(userName);
		List<Card> cards = turnNode.getCardTemp();
		Card card = null;
		for (Card cd : cards) {
			if(cd.getId().equals(request.getId())) {
				card = cd;
				break;
			}
		}
		if(card == null) {
			System.out.println("GenelralStoreActionCmd Error!!!!!!!!!!!!!!!!!!!");
		} else {
			turnNode.getCardTemp().remove(card);
			character.getCardsInHand().add(card);
			BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
			turnNode.getNextPlayer().poll();
			if (turnNode.getNextPlayer().peek() == null) {
				// request player in turn continue using card
				turnNode.requestPlayerUseCard();
			} else {
				turnNode.requestOtherPlayerUseCard();
			}
		}
		
	}

}
