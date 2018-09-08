package com.example.springboot.model;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.model.role.RoleType;

public class CharacterVO {
	protected int id;
	protected String userName;
	protected Hero hero;
	protected List<Card> cardsInFront = new ArrayList<>();
	protected int numCardsInHand = 0;
	protected int gun = 1; 
	protected int lifePoint = 0; 
	protected int capacityLPoint = 0; 
	protected int viewOthers = 0;
	protected int othersView = 0;
	protected boolean barrel = false;
	protected RoleType roleType;
	protected boolean beJailed = false;
	protected boolean hasDynamite = false;

	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isBarrel() {
		return barrel;
	}
	public void setBarrel(boolean barrel) {
		this.barrel = barrel;
	}
	public int getCapacityLPoint() {
		return capacityLPoint;
	}
	public void setCapacityLPoint(int capacityLPoint) {
		this.capacityLPoint = capacityLPoint;
	}
	public boolean isBeJailed() {
		return beJailed;
	}
	public void setBeJailed(boolean beJailed) {
		this.beJailed = beJailed;
	}
	public boolean isHasDynamite() {
		return hasDynamite;
	}
	public void setHasDynamite(boolean hasDynamite) {
		this.hasDynamite = hasDynamite;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Hero getHero() {
		return hero;
	}
	public void setHero(Hero hero) {
		this.hero = hero;
		this.lifePoint += hero.getLifePoint();
		this.capacityLPoint = this.lifePoint;
	}
	public List<Card> getCardsInFront() {
		return cardsInFront;
	}
	public void setCardsInFront(List<Card> cardsInFront) {
		this.cardsInFront = cardsInFront;
	}
	public int getGun() {
		return gun;
	}
	public void setGun(int gun) {
		this.gun = gun;
	}
	public int getViewOthers() {
		return viewOthers;
	}
	public void setViewOthers(int viewOthers) {
		this.viewOthers = viewOthers;
	}
	public int getOthersView() {
		return othersView;
	}
	public void setOthersView(int othersView) {
		this.othersView = othersView;
	}
	public RoleType getRoleType() {
		return roleType;
	}
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	
	public int getNumCardsInHand() {
		return numCardsInHand;
	}
	public void setNumCardsInHand(int numCardsInHand) {
		this.numCardsInHand = numCardsInHand;
	}
	
	public int getLifePoint() {
		return lifePoint;
	}
	public void setLifePoint(int lifePoint) {
		this.lifePoint = lifePoint;
	}
	
	public CharacterVO(int id,String userName, Hero hero, List<Card> cardsInFront, int numCardsInHand, int gun, int lifePoint,
			int capacityLPoint, int viewOthers, int othersView, boolean barrel, RoleType roleType, boolean beJailed,
			boolean hasDynamite) {
		super();
		this.id = id;
		this.userName = userName;
		this.hero = hero;
		this.cardsInFront = cardsInFront;
		this.numCardsInHand = numCardsInHand;
		this.gun = gun;
		this.lifePoint = lifePoint;
		this.capacityLPoint = capacityLPoint;
		this.viewOthers = viewOthers;
		this.othersView = othersView;
		this.barrel = barrel;
		this.roleType = roleType;
		this.beJailed = beJailed;
		this.hasDynamite = hasDynamite;
	}
	public CharacterVO() {
		super();
	}
	
}
