package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.Card;

public class BartCassidy extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(BartCassidy.class);

	@Override
	public void useSkill() {

	}

	public BartCassidy() {
		this.name = "BartCassidy";
		this.skillDescription = "Description " + name;
		this.id = "BartCassidy";
		this.lifePoint = 4;
		this.setImage("Hero-BartCassidy.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

}
