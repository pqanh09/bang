package com.example.springboot.model.role;

import com.example.springboot.model.hero.Hero;

public class FuorileggeRole extends Role {

	public FuorileggeRole(String name, String description, String image) {
		super(name, RoleType.FUORILEGGE, description, image);
	}

	@Override
	public void mission() {
		// TODO Auto-generated method stub

	}

	@Override
	public void benefit(Hero hero) {
		// TODO Auto-generated method stub

	}

}
