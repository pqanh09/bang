package com.example.springboot.model.hero;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.service.CommonService;

public class PatBrennan extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(PatBrennan.class);

	@Override
	public void useSkill() {
		logger.info("using PatBrennan Herro's Skill");

	}

	public PatBrennan() {
		this.name = "PatBrennan";
		this.skillDescription = "Description " + name;
		this.id = "PatBrennan";
		this.lifePoint = 4;
		this.setImage("Hero-PatBrennan.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useSkill(Match match, String userName, Character character, CommonService commonService,
			Map<String, Object> others) {
		// TODO Auto-generated method stub
		return false;
	}

}
