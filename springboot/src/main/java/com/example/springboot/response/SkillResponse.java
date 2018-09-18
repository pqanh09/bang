package com.example.springboot.response;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.Hero;

public class SkillResponse extends Response {
	private boolean status = false;
	private int step;
	private List<String> players = new ArrayList<>();
	private List<Card> card = new ArrayList<>();
	private Hero hero;

	
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

	public List<Card> getCard() {
		return card;
	}

	public void setCard(List<Card> card) {
		this.card = card;
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

	public SkillResponse(boolean status, int step, List<String> players, List<Card> card, Hero hero) {
		super();
		this.status = status;
		this.step = step;
		this.players = players;
		this.card = card;
		this.hero = hero;
	}
	public SkillResponse(String userName, boolean status) {
		super();
		this.status = status;
		this.responseType = ResponseType.Skill;
	}

}
