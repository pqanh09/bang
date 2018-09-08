package com.example.springboot.model.card;
import java.util.Map;

import com.example.springboot.model.Character;
public class SaloonCard extends Card{
	
	
	

	public SaloonCard() {
		super();
	}

	public SaloonCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "SALOON";
	}

	@Override
	public void run(Character character) {
		int lifePoint = character.getLifePoint();
		if(lifePoint < character.getCapacityLPoint()) {
			character.setLifePoint(lifePoint + 1);
		}
	}


}
