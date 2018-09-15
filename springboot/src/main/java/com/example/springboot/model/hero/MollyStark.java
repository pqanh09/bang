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

public class MollyStark extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(MollyStark.class);

	@Override
	public void useSkill() {
		logger.info("using MollyStark Herro's Skill");

	}

	public MollyStark() {
		this.name = "MollyStark";
		this.skillDescription = "Description " + name;
		this.id = "MollyStark";
		this.lifePoint = 4;
		this.setImage("Hero-MollyStark.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useSkill(Match match, String userName, Character character, CommonService commonService,
			Map<String, Object> others) {
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
		// get cards for character;
		List<Card> cards = commonService.getFromNewCardList(match, 1);
		character.getCardsInHand().addAll(cards);
		character.setNumCardsInHand(character.getCardsInHand().size());
		return true;
	}

}
