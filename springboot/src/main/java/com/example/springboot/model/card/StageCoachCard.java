package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class StageCoachCard extends Card{
	
	
	

	public StageCoachCard() {
		super();
	}

	public StageCoachCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "STAGE COACH";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
