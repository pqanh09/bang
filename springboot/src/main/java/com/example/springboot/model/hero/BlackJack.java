package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.Card;

public class BlackJack extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(BlackJack.class);

	@Override
	public void useSkill() {

	}

	public BlackJack() {
		this.name = "BlackJack";
		this.skillDescription = "Description " + name;
		this.id = "BlackJack";
		this.lifePoint = 4;
		this.setImage("Hero-BlackJack.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

}
