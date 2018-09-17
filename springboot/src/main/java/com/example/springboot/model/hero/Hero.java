package com.example.springboot.model.hero;

import java.util.Map;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.service.CommonService;

public abstract class Hero {
	protected String name;
	protected boolean autoUseSkill = true;
	protected String skillDescription;
	protected int lifePoint;
	protected String id;
	protected String image = "/data/image/hero/";
	abstract public void useSkill();
	abstract public boolean useSkill(Card card);
	abstract public boolean useSkill(Match match, Character character, CommonService commonService, int step, Map<String, Object> others);
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = this.image + image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSkillDescription() {
		return skillDescription;
	}
	public void setSkillDescription(String skillDescription) {
		this.skillDescription = skillDescription;
	}
	public int getLifePoint() {
		return lifePoint;
	}
	public void setLifePoint(int lifePoint) {
		this.lifePoint = lifePoint;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isAutoUseSkill() {
		return autoUseSkill;
	}
	public void setAutoUseSkill(boolean autoUseSkill) {
		this.autoUseSkill = autoUseSkill;
	}
	
	
}
