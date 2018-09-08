package com.example.springboot.model.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.card.Card;

public class VultureSam extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(VultureSam.class);

	@Override
	public void useSkill() {
		logger.info("using VultureSam Herro's Skill");

	}

	public VultureSam() {
		this.name = "VULTURESAM";
		this.skillDescription = "AAAAAAAAAAAA";
	}

	public VultureSam(String name) {
		this.name = name;
		this.skillDescription = "Description " + name;
		this.id = name;
		this.lifePoint = 4;
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

}
