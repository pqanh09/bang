package com.example.springboot.model.hero;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.service.CommonService;

public class CalamityJanet extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(CalamityJanet.class);

	@Override
	public void useSkill() {
		logger.info("using CalamityJanet Herro's Skill");

	}

	public CalamityJanet() {
		this.name = "CalamityJanet";
		this.skillDescription = "Description " + name;
		this.id = "CalamityJanet";
		this.lifePoint = 4;
		this.setImage("Hero-CalamityJanet.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		if(card instanceof BangCard || card instanceof MissedCard) {
			return true;
		}
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step, Map<String, Object> others) {
		String serverMessage = "- Using " + character.getHero().getName() + "'skill.";
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
				new HeroSkillResponse(character.getUserName(), "", "", serverMessage, character.getHero()));
		return true;
	}


}
