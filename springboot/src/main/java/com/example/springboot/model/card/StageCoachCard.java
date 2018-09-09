package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class StageCoachCard extends Card{
	
	
	

	public StageCoachCard() {
		super();
	}

	public StageCoachCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "STAGE COACH";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void run(Character character) {
		
		
	}

}
