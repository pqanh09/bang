package com.example.springboot.response;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.Hero;

public class SkillResponse extends UserResponse {
	private boolean status = false;
	private int step;
	private List<String> players = new ArrayList<>();
	private List<Card> cards = new ArrayList<>();
	private Hero hero;
	private String targetUser;
	
	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}


	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public SkillResponse() {
		super();
	}

	public SkillResponse(String userName, boolean status, int step, List<String> players, List<Card> cards, Hero hero, String targetUser) {
		super();
		this.status = status;
		this.step = step;
		this.players = players;
		this.cards = cards;
		this.hero = hero;
		this.responseType = ResponseType.Skill;
		this.userName = userName;
		this.targetUser = targetUser;
	}
	public SkillResponse(String userName, boolean status) {
		super();
		this.status = status;
		this.responseType = ResponseType.Skill;
	}

}
