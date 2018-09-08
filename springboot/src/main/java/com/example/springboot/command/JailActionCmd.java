package com.example.springboot.command;

import java.util.List;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.JailCard;
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

public class JailActionCmd extends AbsActionCmd implements ActionCmd {

	

	public JailActionCmd(TableService tableService, HeroService heroService, ShareService shareService, TurnService turnService) {
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
		// get cards for escaping the jail;
		Card card = tableService.getFromNewCardList(1).get(0);
		
		//TODO ....
		tableService.addToOldCardList(card);
		tableService.getMessagingTemplate().convertAndSend("/topic/usedCard", new UseCardResponse(userName, ResponseType.DrawCardJail, card, null));
		
		character.setBeJailed(false);
		// find dynamite card
		Card jailCard = null;
		for (Card cd : character.getCardsInFront()) {
			if(cd instanceof JailCard) {
				jailCard = cd;
				break;
			}
		}
		character.getCardsInFront().remove(jailCard);
		
		BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
		
		tableService.addToOldCardList(jailCard);
		if(Suit.hearts.equals(card.getSuit())) {
			turnService.getCurrentTurn().run();
		} else {
			turnService.endTurn(userName);
		}
	}

}
