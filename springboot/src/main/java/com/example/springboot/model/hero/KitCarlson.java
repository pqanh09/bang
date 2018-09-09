package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class KitCarlson extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(KitCarlson.class);

	@Override
	public void useSkill() {

	}

	public KitCarlson() {
		this.name = "KitCarlson";
		this.skillDescription = "Description " + name;
		this.id = "KitCarlson";
		this.lifePoint = 4;
		this.setImage("Hero-KitCarlson.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
