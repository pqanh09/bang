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
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.CommonService;

public class SlabTheKiller extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(SlabTheKiller.class);

	@Override
	public void useSkill() {

	}

	public SlabTheKiller() {
		this.name = "SlabTheKiller";
		this.skillDescription = "Description " + name;
		this.id = "SlabTheKiller";
		this.lifePoint = 4;
		this.setImage("Hero-SlabTheKiller.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}

	@Override
	public boolean useSkill(Match match, String userName, Character character, CommonService commonService,
			Map<String, Object> others) {
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
		match.getCurrentTurn().getPlayerUsedMissed().add(userName);
		return true;
	}


}
