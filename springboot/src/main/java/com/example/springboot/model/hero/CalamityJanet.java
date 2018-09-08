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
		logger.info("using VultureSam Herro's Skill");

	}

	public CalamityJanet() {
		this.name = "WILLY THE KID";
		this.skillDescription = "She can play BANG! cards as Missed! cards and vice versa.";
		this.id = "CalamityJanet";
		this.lifePoint = 4;
	}

	@Override
	public boolean useSkill(Card card) {
		if(card instanceof BangCard || card instanceof MissedCard) {
			return true;
		}
		return false;
	}


}
