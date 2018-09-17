package com.example.springboot.model.hero;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.service.CommonService;

public class WillyTheKid extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(WillyTheKid.class);

	@Override
	public void useSkill() {
		logger.info("using VultureSam Herro's Skill");

	}

	public WillyTheKid() {
		this.name = "WillyTheKid";
		this.skillDescription = "Description " + name;
		this.id = "WillyTheKid";
		this.lifePoint = 4;
		this.setImage("Hero-WillyTheKid.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		if(card instanceof BangCard) {
			return true;
		}
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step, Map<String, Object> others) {
		// TODO Auto-generated method stub
		return false;
	}


}
