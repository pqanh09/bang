package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class BarrelCard extends Card{
	
	
	

	public BarrelCard() {
		super();
	}

	public BarrelCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.barrel;
		this.description = "";
		this.name = "BARREL";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		character.setBarrel(true);
	}
	@Override
	public void remove(Character character) {
		character.setBarrel(false);
	}

	

}
