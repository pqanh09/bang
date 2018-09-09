package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class PaulRegret extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(PaulRegret.class);

	@Override
	public void useSkill() {

	}

	public PaulRegret() {
		this.name = "PaulRegret";
		this.skillDescription = "Description " + name;
		this.id = "PaulRegret";
		this.lifePoint = 4;
		this.setImage("Hero-PaulRegret.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
