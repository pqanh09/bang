package com.example.springboot.response;

import java.util.HashMap;
import java.util.Map;

import com.example.springboot.model.hero.Hero;

public class HeroSkillResponse extends UserResponse {
	private Hero hero;
	private String targetUser;
	private Map<String, Object> others = new HashMap<>();
	public Hero getHero() {
		return hero;
	}
	public void setHero(Hero hero) {
		this.hero = hero;
	}
	public String getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	public Map<String, Object> getOthers() {
		return others;
	}
	public void setOthers(Map<String, Object> others) {
		this.others = others;
	}
	public HeroSkillResponse(ResponseType responseType, String userName, Hero hero, String targetUser,
			Map<String, Object> others) {
		super(responseType, userName);
		this.hero = hero;
		this.targetUser = targetUser;
		this.others = others;
	}
	

	public HeroSkillResponse(ResponseType responseType, String userName) {
		super(responseType, userName);
	}

}
