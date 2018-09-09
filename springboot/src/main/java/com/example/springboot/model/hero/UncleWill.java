package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class UncleWill extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(UncleWill.class);

	@Override
	public void useSkill() {

	}

	public UncleWill() {
		this.name = "UncleWill";
		this.skillDescription = "Description " + name;
		this.id = "UncleWill";
		this.lifePoint = 4;
		this.setImage("Hero-UncleWill.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
