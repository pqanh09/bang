package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class ScopeCard extends Card{
	
	
	

	public ScopeCard() {
		super();
	}

	public ScopeCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.viewothers;
		this.description = "";
		this.name = "SCOPE";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void run(Character character) {
		character.setViewOthers(1);
	}

}
