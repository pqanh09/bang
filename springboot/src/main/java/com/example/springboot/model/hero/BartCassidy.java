package com.example.springboot.model.hero;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardNotInTurnResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.CardUtils;

public class BartCassidy extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(BartCassidy.class);

	@Override
	public void useSkill() {

	}

	public BartCassidy() {
		this.name = "BartCassidy";
		this.skillDescription = "Description " + name;
		this.id = "BartCassidy";
		this.lifePoint = 4;
		this.setImage("Hero-BartCassidy.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService,
			int step, Map<String, Object> others) {
		if (character.getLifePoint() == 0) {
			return false;
		}
		String userName = character.getUserName();
		String serverMessage = "- Using " + character.getHero().getName() + "'skill to get more 1 card.";
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
				new HeroSkillResponse(userName, CardUtils.losePointCard, "",  "", serverMessage, character.getHero()));
		// get cards for character;
		int numberNewCard = (int) others.get("numberNewCard");
		List<Card> cards = commonService.getFromNewCardList(match, numberNewCard);
		character.getCardsInHand().addAll(cards);
		character.setNumCardsInHand(character.getCardsInHand().size());
		return true;
	}



}
