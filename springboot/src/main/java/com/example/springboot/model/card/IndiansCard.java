package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class IndiansCard extends Card{
	
	
	

	public IndiansCard() {
		super();
	}

	public IndiansCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "All other players discard a BANG! or lose 1 life point.";
		this.name = "INDIANS";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void run(Character character) {
		
		
	}

}
