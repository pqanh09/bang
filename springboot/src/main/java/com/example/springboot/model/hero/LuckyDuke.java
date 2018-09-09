package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class LuckyDuke extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(LuckyDuke.class);

	@Override
	public void useSkill() {

	}

	public LuckyDuke() {
		this.name = "LuckyDuke";
		this.skillDescription = "Description " + name;
		this.id = "LuckyDuke";
		this.lifePoint = 4;
		this.setImage("Hero-LuckyDuke.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
