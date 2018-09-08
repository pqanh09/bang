package com.example.springboot.model.role;

import com.example.springboot.model.hero.Hero;

public class ViceRole extends Role {

	public ViceRole(String name, String description) {
		super(name, RoleType.VICE, description);
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
