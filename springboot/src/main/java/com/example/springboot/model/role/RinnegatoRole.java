package com.example.springboot.model.role;

import com.example.springboot.model.hero.Hero;

public class RinnegatoRole extends Role {

	public RinnegatoRole(String name, String description) {
		super(name, RoleType.RINNEGATO, description);
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
