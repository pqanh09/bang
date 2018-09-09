package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class RoseDoolan extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(RoseDoolan.class);

	@Override
	public void useSkill() {

	}

	public RoseDoolan() {
		this.name = "RoseDoolan";
		this.skillDescription = "Description " + name;
		this.id = "RoseDoolan";
		this.lifePoint = 4;
		this.setImage("Hero-RoseDoolan.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
