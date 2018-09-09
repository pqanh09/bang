package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;

public class SlabTheKiller extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(SlabTheKiller.class);

	@Override
	public void useSkill() {

	}

	public SlabTheKiller() {
		this.name = "SlabTheKiller";
		this.skillDescription = "Description " + name;
		this.id = "SlabTheKiller";
		this.lifePoint = 4;
		this.setImage("Hero-SlabTheKiller.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}


}
