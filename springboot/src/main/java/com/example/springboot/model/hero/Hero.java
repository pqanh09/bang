package com.example.springboot.model.hero;

import java.util.Map;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.service.CommonService;

public abstract class Hero {
	protected String name;
	protected boolean passiveSkill = false;
	protected boolean activeSkill = false;
	protected boolean canUseSkill = false;
	protected String skillDescription;
	protected int lifePoint;
	protected String id;
	protected String image = "/data/image/hero/";
	abstract public void useSkill();
	abstract public boolean useSkill(Card card);
	abstract public boolean useSkill(Match match, String userName, Character character, CommonService commonService, Map<String, Object> others);
	
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
	public boolean isPassiveSkill() {
		return passiveSkill;
	}
	public void setPassiveSkill(boolean passiveSkill) {
		this.passiveSkill = passiveSkill;
	}
	public boolean isActiveSkill() {
		return activeSkill;
	}
	public void setActiveSkill(boolean activeSkill) {
		this.activeSkill = activeSkill;
	}
	public boolean isCanUseSkill() {
		return canUseSkill;
	}
	public void setCanUseSkill(boolean canUseSkill) {
		this.canUseSkill = canUseSkill;
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
	@Override
	public String toString() {
		return "Hero [name=" + name + ", passiveSkill=" + passiveSkill + ", activeSkill=" + activeSkill
				+ ", canUseSkill=" + canUseSkill + ", skillDescription=" + skillDescription + ", lifePoint=" + lifePoint
				+ ", id=" + id + "]";
	}
	
	
	
}
