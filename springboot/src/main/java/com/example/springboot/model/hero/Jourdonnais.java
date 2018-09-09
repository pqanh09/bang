package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class Jourdonnais extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(Jourdonnais.class);

	@Override
	public void useSkill() {

	}

	public Jourdonnais() {
		this.name = "Jourdonnais";
		this.skillDescription = "Description " + name;
		this.id = "Jourdonnais";
		this.lifePoint = 4;
		this.setImage("Hero-Jourdonnais.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
