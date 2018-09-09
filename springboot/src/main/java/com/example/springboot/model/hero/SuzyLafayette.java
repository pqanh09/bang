package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class SuzyLafayette extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(SuzyLafayette.class);

	@Override
	public void useSkill() {

	}

	public SuzyLafayette() {
		this.name = "SuzyLafayette";
		this.skillDescription = "Description " + name;
		this.id = "SuzyLafayette";
		this.lifePoint = 4;
		this.setImage("Hero-SuzyLafayette.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
