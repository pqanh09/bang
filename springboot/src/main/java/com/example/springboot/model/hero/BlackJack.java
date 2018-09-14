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
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.CommonService;

public class BlackJack extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(BlackJack.class);

	@Override
	public void useSkill() {

	}

	public BlackJack() {
		this.name = "BlackJack";
		this.skillDescription = "Description " + name;
		this.id = "BlackJack";
		this.lifePoint = 4;
		this.setImage("Hero-BlackJack.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useSkill(Match match, String userName, Character character, CommonService commonService, Map<String, Object> others) {
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
		// get cards for character;
		List<Card> cards = commonService.getFromNewCardList(match, 2);
		if(Suit.hearts.equals(cards.get(1).getSuit()) || Suit.diamonds.equals(cards.get(1).getSuit())) {
			cards.add(commonService.getFromNewCardList(match, 1).get(0));
		}
		character.getCardsInHand().addAll(cards);
		character.setNumCardsInHand(character.getCardsInHand().size());
		return true;
	}

}
