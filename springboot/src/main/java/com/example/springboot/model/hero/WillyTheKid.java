package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;

public class WillyTheKid extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(WillyTheKid.class);

	@Override
	public void useSkill() {
		logger.info("using VultureSam Herro's Skill");

	}

	public WillyTheKid() {
		this.name = "WILLY THE KID";
		this.skillDescription = "He can play any number of BANG! cards.";
		this.id = "WillyTheKid";
		this.lifePoint = 4;
	}

	@Override
	public boolean useSkill(Card card) {
		if(card instanceof BangCard) {
			return true;
		}
		return false;
	}


}
