package com.example.springboot.command;

import java.util.List;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.request.Request;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

public class DynamiteActionCmd extends AbsActionCmd implements ActionCmd {

	

	public DynamiteActionCmd(TableService tableService, HeroService heroService, ShareService shareService, TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
	}

	@Override
	public void execute(Request request) {
		String userName = request.getUser();
		String sessionId = tableService.getUserMap().get(userName);
		
		if(!turnService.getCurrentTurn().getCharacter().getUserName().equals(userName)) {
			return;
		}
		//get Character
		Character character = tableService.getCharacterMap().get(userName);
		// get cards for escaping the jail;
		Card card = tableService.getFromNewCardList(1).get(0);
		
		//TODO ....
		tableService.addToOldCardList(card);
		tableService.getMessagingTemplate().convertAndSend("/topic/usedCard", new UseCardResponse(userName, ResponseType.DrawCardDynamite, card, null));
		
		character.setHasDynamite(false);
		
		// find dynamite card
		Card dynamiteCard = null;
		for (Card cd : character.getCardsInFront()) {
			if(cd instanceof DynamiteCard) {
				dynamiteCard = cd;
				break;
			}
		}
		character.getCardsInFront().remove(dynamiteCard);
		
		if(Suit.spades.equals(card.getSuit())) {
			tableService.addToOldCardList(dynamiteCard);
			 
			character.setLifePoint(character.getLifePoint() - 3);
			BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
			if (character.getLifePoint() == 0) {
				tableService.playerDead(userName);
				turnService.callNextPlayerTurn();
			} else {
				turnService.getCurrentTurn().run();
			}
		} else {
			
			BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
			
			tableService.getPlayerTurnQueue().poll();
			String nextPlayer = tableService.getPlayerTurnQueue().peek();
			tableService.getPlayerTurnQueue().addFirst(userName);
			Character nextCharacter = tableService.getCharacterMap().get(nextPlayer);
			nextCharacter.getCardsInFront().add(dynamiteCard);
			nextCharacter.setHasDynamite(true);
			String sessionIdNextPlayer = tableService.getUserMap().get(nextPlayer);
			BangUtils.notifyCharacter(tableService.getMessagingTemplate(), nextCharacter, sessionIdNextPlayer);
			turnService.getCurrentTurn().run();
		}
	}

}
