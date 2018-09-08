package com.example.springboot.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.request.Request;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;

public class PickHeroActionCmd extends AbsActionCmd implements ActionCmd {

	

	public PickHeroActionCmd(TableService tableService, HeroService heroService, ShareService shareService, TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
	}

	@Override
	public void execute(Request request) {
		String userName = request.getUser();
		String sessionId = tableService.getUserMap().get(userName);
		//get hero which is picked from user
		Hero hero = heroService.getHero(request.getId());
		//get Character
		Character character = tableService.getCharacterMap().get(userName);
		//set hero in character
		character.setHero(hero);
		
		// get cards for character;
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < Constants.BEGIN_CARD; i++) {
			cards.add(tableService.getNewCards().pollFirst());
		}
		character.setCardsInHand(cards);
		//notify hero of this character to others
		tableService.getMessagingTemplate().convertAndSend("/topic/character", new CharacterResponse(ResponseType.Character, userName, character.getVO()));
		tableService.getMessagingTemplate().convertAndSendToUser(sessionId, "/queue/character", new CharacterResponse(ResponseType.Character, userName, character));
		if(checkOtherUserPickedHero()) {
			turnService.createPlayerTurn();
			turnService.getCurrentTurn().run();
		}
	}
	private boolean checkOtherUserPickedHero() {
		for (Entry<String, Character> entry : tableService.getCharacterMap().entrySet()) {
			if(entry.getValue().getHero() == null) {
				return false;
			}
		}
		return true;
	}

}
