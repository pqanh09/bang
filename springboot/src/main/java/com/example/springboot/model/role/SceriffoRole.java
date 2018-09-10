package com.example.springboot.model.role;

import com.example.springboot.model.hero.Hero;

public class SceriffoRole extends Role {

	public SceriffoRole(String name, String description, String image) {
		super(name, RoleType.SCERIFFO, description, image);
	}

	@Override
	public void mission() {
		// TODO Auto-generated method stub
	}

	@Override
	public void benefit(Hero hero) {
		hero.setLifePoint(hero.getLifePoint() + 1);
	}

}
