package com.example.springboot.model.hero;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.CommonService;

public class PixiePete extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(PixiePete.class);

	@Override
	public void useSkill() {
		logger.info("using PixiePete Herro's Skill");

	}

	public PixiePete() {
		this.name = "PixiePete";
		this.skillDescription = "Description " + name;
		this.id = "PixiePete";
		this.lifePoint = 3;
		this.setImage("Hero-PixiePete.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		String userName = character.getUserName();
		String serverMessage = "- Using " + character.getHero().getName() + "'skill to get 3 cards.";
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
				new HeroSkillResponse(userName, "", "", serverMessage, character.getHero()));
		// get cards for character;
		List<Card> cards = commonService.getFromNewCardList(match, 3);
		character.getCardsInHand().addAll(cards);
		character.setNumCardsInHand(character.getCardsInHand().size());
		return true;
	}

}
