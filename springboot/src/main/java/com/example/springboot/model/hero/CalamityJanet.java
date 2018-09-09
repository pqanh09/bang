package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

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


}
