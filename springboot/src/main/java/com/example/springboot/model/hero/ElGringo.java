package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class ElGringo extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(ElGringo.class);

	@Override
	public void useSkill() {
		logger.info("using ElGringo Herro's Skill");

	}

	public ElGringo() {
		this.name = "ElGringo";
		this.skillDescription = "Description " + name;
		this.id = "ElGringo";
		this.lifePoint = 4;
		this.setImage("Hero-ElGringo.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
