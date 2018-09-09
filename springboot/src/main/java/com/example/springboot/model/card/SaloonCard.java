package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class SaloonCard extends Card{
	
	
	

	public SaloonCard() {
		super();
	}

	public SaloonCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "SALOON";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void run(Character character) {
		int lifePoint = character.getLifePoint();
		if(lifePoint < character.getCapacityLPoint()) {
			character.setLifePoint(lifePoint + 1);
		}
	}


}
