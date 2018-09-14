package com.example.springboot.model.card;
import com.example.springboot.model.Character;
import com.example.springboot.model.hero.RoseDoolan;
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
		if(character.getHero() instanceof RoseDoolan) {
			character.setViewOthers(2);
		} else {
			character.setViewOthers(1);
		}
	}

}
