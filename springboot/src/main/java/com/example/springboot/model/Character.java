package com.example.springboot.model;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.card.Card;
import com.example.springboot.model.role.Role;
import com.example.springboot.model.role.RoleType;

public class Character extends CharacterVO {

	public Character() {
	}
	public Character(int id, String userName) {
		this.userName = userName;
		this.id = id;
	}

	private Role role;
	private List<Card> cardsInHand = new ArrayList<>();

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
		if(RoleType.SCERIFFO.equals(role.getRoleType())) {
			this.roleType = RoleType.SCERIFFO; 
		}
	}

	public List<Card> getCardsInHand() {
		return cardsInHand;
	}

	public void setCardsInHand(List<Card> cardsInHand) {
		this.cardsInHand = cardsInHand;
		this.numCardsInHand = cardsInHand.size();
	}

	public CharacterVO getVO() {
		return new CharacterVO(this.id, this.userName, this.hero, this.cardsInFront, this.numCardsInHand, this.gun, this.lifePoint, this.capacityLPoint, this.viewOthers, this.othersView, this.barrel, this.roleType, this.beJailed, this.hasDynamite);
	}

}
