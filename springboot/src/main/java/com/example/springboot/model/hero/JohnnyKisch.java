package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class JohnnyKisch extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(JohnnyKisch.class);

	@Override
	public void useSkill() {

	}

	public JohnnyKisch() {
		this.name = "JohnnyKisch";
		this.skillDescription = "Description " + name;
		this.id = "JohnnyKisch";
		this.lifePoint = 4;
		this.setImage("Hero-JohnnyKisch.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
