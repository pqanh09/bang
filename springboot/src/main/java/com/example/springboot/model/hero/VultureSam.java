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
import com.example.springboot.service.CommonService;

public class VultureSam extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(VultureSam.class);

	@Override
	public void useSkill() {
		logger.info("using VultureSam Herro's Skill");

	}

	public VultureSam() {
		this.name = "VultureSam";
		this.skillDescription = "Description " + name;
		this.id = "VultureSam";
		this.lifePoint = 4;
		this.setImage("Hero-VultureSam.jpg");
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
		String targetPlayer =  (String) others.get("targetUser");
		String serverMessage = "- Using " + character.getHero().getName() + "'skill to get card(s) from " + targetPlayer;
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
				new HeroSkillResponse(userName, "", "", serverMessage, character.getHero()));
		// get cards for character;
		List<Card> cards = (List<Card>) others.get("cards");
		character.getCardsInHand().addAll(cards);
		character.setNumCardsInHand(character.getCardsInHand().size());
		return true;
	}

}
