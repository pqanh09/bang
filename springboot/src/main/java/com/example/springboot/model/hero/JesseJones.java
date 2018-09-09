package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class JesseJones extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(JesseJones.class);

	@Override
	public void useSkill() {

	}

	public JesseJones() {
		this.name = "JesseJones";
		this.skillDescription = "Description " + name;
		this.id = "JesseJones";
		this.lifePoint = 4;
		this.setImage("Hero-JesseJones.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
