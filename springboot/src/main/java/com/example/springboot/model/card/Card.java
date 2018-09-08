package com.example.springboot.model.card;

import com.example.springboot.model.Character;

public abstract class Card {
	
	public enum Suit {
		diamonds, clubs, hearts, spades;
	}
	public enum CardType {
		physical, magic, gun, viewothers, otherviews, barrel;
	}
	protected Suit suit = Suit.hearts;
	protected String name;
	protected String description;
	protected CardType cardType = CardType.physical;
	protected boolean affectAll = false;
	private String id;
	abstract public void run(Character character);
	
	public Suit getSuit() {
		return suit;
	}
	public void setSuit(Suit suit) {
		this.suit = suit;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public CardType getCardType() {
		return cardType;
	}
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}
	public boolean isAffectAll() {
		return affectAll;
	}
	public void setAffectAll(boolean affectAll) {
		this.affectAll = affectAll;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Card() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Card(Suit suit, String id) {
		super();
		this.suit = suit;
		this.id = id;
	} 
}
