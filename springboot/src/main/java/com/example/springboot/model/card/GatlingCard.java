package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class GatlingCard extends Card{
	
	
	

	public GatlingCard() {
		super();
	}

	public GatlingCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "GATLING";
		this.setImage(image);
		this.number = number;
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		
		
	}

}
