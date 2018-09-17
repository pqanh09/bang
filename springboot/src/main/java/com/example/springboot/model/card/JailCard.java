package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class JailCard extends Card{
	
	
	

	public JailCard() {
		super();
	}

	public JailCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "JAIL";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		character.setBeJailed(true);
	}
	@Override
	public void remove(Character character) {
		character.setBeJailed(false);
	}

}
