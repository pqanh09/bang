package com.example.springboot.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.Hero;

public class HeroSkillResponse extends UseCardNotInTurnResponse {
	private Hero hero;
	private Map<String, Object> others = new HashMap<>();
	
	public Hero getHero() {
		return hero;
	}
	public void setHero(Hero hero) {
		this.hero = hero;
	}
	public Map<String, Object> getOthers() {
		return others;
	}
	public void setOthers(Map<String, Object> others) {
		this.others = others;
	}
	public HeroSkillResponse(String userName, Card card, Hero hero, Map<String, Object> others) {
		super(userName, card);
		this.hero = hero;
		this.others = others;
	}
	public HeroSkillResponse(String userName, List<Card> cards, String message, String serverMessage, Hero hero,
			Map<String, Object> others) {
		super(userName, cards, message, serverMessage);
		this.hero = hero;
		this.others = others;
	}
	public HeroSkillResponse(String userName, Card card, String targetUser, String message, String serverMessage,
			Hero hero, Map<String, Object> others) {
		super(userName, card, targetUser, message, serverMessage);
		this.hero = hero;
		this.others = others;
	}
	public HeroSkillResponse(String userName, String targetUser, String message, String serverMessage, Hero hero,
			Map<String, Object> others) {
		super(userName, targetUser, message, serverMessage);
		this.hero = hero;
		this.others = others;
	}
	public HeroSkillResponse(Hero hero, Map<String, Object> others) {
		super();
		this.hero = hero;
		this.others = others;
	}
	
	public HeroSkillResponse(String userName, List<Card> cards, String message, String serverMessage) {
		super();
		this.userName = userName;
		this.cards = cards;
		this.responseType = ResponseType.UseCardNotInTurn;
		this.message = message;
		this.serverMessage = serverMessage;
	}
	public HeroSkillResponse(String userName, Card card, String targetUser, String message, String serverMessage) {
		super();
		this.userName = userName;
		this.cards.add(card);
		this.targetUser = targetUser;
		this.responseType = ResponseType.UseCardNotInTurn;
		this.message = message;
		this.serverMessage = serverMessage;
	}
	public HeroSkillResponse(String userName, String targetUser, String message, String serverMessage, Hero hero) {
		super();
		this.userName = userName;
		this.targetUser = targetUser;
		this.responseType = ResponseType.UseCardNotInTurn;
		this.message = message;
		this.serverMessage = serverMessage;
		this.hero = hero;
	}
	
	public HeroSkillResponse(ResponseType responseType, String userName, Hero hero, String targetUser,
			Map<String, Object> others) {
		super(responseType, userName);
		this.hero = hero;
		this.targetUser = targetUser;
		this.others = others;
	}
	

	public HeroSkillResponse(ResponseType responseType, String userName, int countDown) {
		super(responseType, userName);
		this.countDown = countDown;
	}

}
