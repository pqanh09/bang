package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class SchofieldCard extends Card{
	
	
	

	public SchofieldCard() {
		super();
	}

	public SchofieldCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.gun;
		this.description = "";
		this.name = "SCHOFIELD";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		character.setGun(2);
	}
	@Override
	public void remove(Character character) {
		character.setGun(1);
	}

}
