package com.example.springboot.command;

import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.request.Request;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;

public class BarrelActionCmd extends AbsActionCmd implements ActionCmd {

	

	public BarrelActionCmd(TableService tableService, HeroService heroService, ShareService shareService, TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
	}

	@Override
	public void execute(Request request) {
		String userName = request.getUser();
		if(!turnService.getCurrentTurn().getCharacter().getUserName().equals(userName)) {
			return;
		}
		// get cards for escaping the bang/gatlling;
		Card card = tableService.getFromNewCardList(1).get(0);
		
		//TODO ....
		tableService.addToOldCardList(card);
		
		tableService.getMessagingTemplate().convertAndSend("/topic/usedCard", new UseCardResponse(userName, ResponseType.UseBarrel, card, null));
		
		TurnNode turnNode = turnService.getCurrentTurn();
		turnNode.getPlayerUsedBarrel().add(userName);
		if(Suit.hearts.equals(card.getSuit())) {
			turnNode.getNextPlayer().poll();
			if (turnNode.getNextPlayer().peek() == null) {
				// request player in turn continue using card
				turnNode.requestPlayerUseCard();
			} else {
				turnNode.requestOtherPlayerUseCard();
			}
		} else {
			turnNode.requestOtherPlayerUseCard();
		}
	}

}
