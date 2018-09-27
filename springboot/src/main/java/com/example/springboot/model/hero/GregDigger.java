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

public class GregDigger extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(GregDigger.class);

	@Override
	public void useSkill() {
		logger.info("using GregDigger Herro's Skill");

	}

	public GregDigger() {
		this.name = "GregDigger";
		this.skillDescription = "Description " + name;
		this.id = "GregDigger";
		this.lifePoint = 4;
		this.setImage("Hero-GregDigger.jpg");
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
		if(character.getLifePoint() < character.getCapacityLPoint()) {
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
