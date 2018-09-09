package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class BeerCard extends Card{
	
	
	

	public BeerCard() {
		super();
	}

	public BeerCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "BEER";
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
