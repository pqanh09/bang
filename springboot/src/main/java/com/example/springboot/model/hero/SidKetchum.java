package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class SidKetchum extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(SidKetchum.class);

	@Override
	public void useSkill() {

	}

	public SidKetchum() {
		this.name = "SidKetchum";
		this.skillDescription = "Description " + name;
		this.id = "SidKetchum";
		this.lifePoint = 4;
		this.setImage("Hero-SidKetchum.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
