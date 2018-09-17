package com.example.springboot.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.model.hero.PaulRegret;
import com.example.springboot.model.hero.RoseDoolan;
import com.example.springboot.request.Request;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.CommonService;
import com.example.springboot.service.HeroService;

public class PickHeroActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(PickHeroActionCmd.class);
	private HeroService heroService;
	

	public HeroService getHeroService() throws Exception {
		return heroService;
	}

	public void setHeroService(HeroService heroService) {
		this.heroService = heroService;
	}

	public PickHeroActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations, HeroService heroService) {
		super(commonService, simpMessageSendingOperations);
		this.heroService = heroService;
	}

	@Override
	public void execute(Request request, Match match) {
		String userName = request.getUser();
		String sessionId = match.getUserMap().get(userName);
		//get hero which is picked from user
		Hero hero = heroService.getHero(request.getId());
		//get Character
		Character character = match.getCharacterMap().get(userName);
		//set hero in character
		character.setHero(hero);
		if(hero instanceof PaulRegret  || hero instanceof RoseDoolan) {
			hero.useSkill(match, userName, character, commonService, 1, null);
		}
		// get cards for character;
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < Constants.BEGIN_CARD; i++) {
			cards.add(match.getNewCards().pollFirst());
		}
		character.setCardsInHand(cards);
		//notify hero of this character to others
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/character", new CharacterResponse(ResponseType.Character, userName, character.getVO()));
		simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/character", new CharacterResponse(ResponseType.Character, userName, character));
		if(checkOtherUserPickedHero(match)) {
			commonService.createTurnNode(match);
			match.getCurrentTurn().run(match);
		}
	}
	private boolean checkOtherUserPickedHero(Match match) {
		for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
			if(entry.getValue().getHero() == null) {
				return false;
			}
		}
		return true;
	}

}
