package com.example.springboot.model.hero;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.response.BarrelCardResponse;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.CommonService;

public class Jourdonnais extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(Jourdonnais.class);

	@Override
	public void useSkill() {

	}

	public Jourdonnais() {
		this.name = "Jourdonnais";
		this.skillDescription = "Description " + name;
		this.id = "Jourdonnais";
		this.lifePoint = 4;
		this.setImage("Hero-Jourdonnais.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		String userName = character.getUserName();
		Card card = commonService.getFromNewCardList(match, 1).get(0);
		
		commonService.addToOldCardList(card, match);
		
		String serverMessage = "- Using " + character.getHero().getName() + "'skill.";
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
				new HeroSkillResponse(userName, card, "", "The draw card for barrel:", serverMessage, character.getHero()));
		
		TurnNode turnNode = match.getCurrentTurn();
		if(Suit.hearts.equals(card.getSuit())) {
			if(match.getCurrentTurn().getCharacter().getHero() instanceof SlabTheKiller) {
				turnNode.getCharacter().getHero().useSkill(match, match.getCurrentTurn().getCharacter(), commonService, 1, null);
//				match.getCurrentTurn().getPlayerUsedMissed().add(userName);//TODO excuxe by hero
			} else {
				turnNode.getNextPlayer().poll();
			}
		}
		return true;
	}


}
