package com.example.springboot.model.hero;

import java.util.Map;
import java.util.Random;

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
import com.example.springboot.utils.BangUtils;

public class PaulRegret extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(PaulRegret.class);

	@Override
	public void useSkill() {

	}

	public PaulRegret() {
		this.name = "PaulRegret";
		this.skillDescription = "Description " + name;
		this.id = "PaulRegret";
		this.lifePoint = 3;
		this.setImage("Hero-PaulRegret.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		String userName = character.getUserName();
//		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
		character.setOthersView(1);
		commonService.notifyCharacter(match.getMatchId(), character, match.getUserMap().get(userName));
		return true;
	}


}
