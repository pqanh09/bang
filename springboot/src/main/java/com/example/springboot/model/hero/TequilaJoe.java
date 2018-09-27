package com.example.springboot.model.hero;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.CommonService;

public class TequilaJoe extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(TequilaJoe.class);

	@Override
	public void useSkill() {
		logger.info("using TequilaJoe Herro's Skill");

	}

	public TequilaJoe() {
		this.name = "TequilaJoe";
		this.skillDescription = "Description " + name;
		this.id = "TequilaJoe";
		this.lifePoint = 4;
		this.setImage("Hero-TequilaJoe.jpg");
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
		if(character.getCapacityLPoint() - character.getLifePoint() >= 2) {
			String serverMessage = "- Using " + character.getHero().getName() + "'skill.";
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
					new HeroSkillResponse(userName, "", "", serverMessage, character.getHero()));
		}
		int lifePoint = character.getLifePoint() + 2;
		if(lifePoint > character.getCapacityLPoint()) {
			lifePoint = character.getCapacityLPoint();
		}
		character.setLifePoint(lifePoint);
		return true;
	}

}
