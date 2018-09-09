package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class PedroRamirez extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(PedroRamirez.class);

	@Override
	public void useSkill() {

	}

	public PedroRamirez() {
		this.name = "PedroRamirez";
		this.skillDescription = "Description " + name;
		this.id = "PedroRamirez";
		this.lifePoint = 4;
		this.setImage("Hero-PedroRamirez.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
