package com.example.springboot.model.hero;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.CommonService;

public class SuzyLafayette extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(SuzyLafayette.class);

	@Override
	public void useSkill() {

	}

	public SuzyLafayette() {
		this.name = "SuzyLafayette";
		this.skillDescription = "Description " + name;
		this.id = "SuzyLafayette";
		this.lifePoint = 4;
		this.setImage("Hero-SuzyLafayette.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		String userName = character.getUserName();
		String serverMessage = "- Using " + character.getHero().getName() + "'skill to get more 1 card.";
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
				new HeroSkillResponse(userName, "", "", serverMessage, character.getHero()));
		// get cards for character;
		List<Card> cards = commonService.getFromNewCardList(match, 1);
		character.getCardsInHand().addAll(cards);
		character.setNumCardsInHand(character.getCardsInHand().size());
		return true;
	}


}
