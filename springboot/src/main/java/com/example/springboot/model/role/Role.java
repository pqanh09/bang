package com.example.springboot.model.role;

import com.example.springboot.model.hero.Hero;

public abstract class Role {
	protected String name;
	protected String description;
	protected String image;
	protected RoleType roleType = RoleType.UNKNOWN;

	abstract public void mission();

	abstract public void benefit(Hero hero);

	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public Role(String name, RoleType roleType, String description, String image) {
		super();
		this.name = name;
		this.description = description;
		this.roleType = roleType;
		this.image = image;
	}

	@Override
	public String toString() {
		return "Role [name=" + name + ", description=" + description + "]";
	}

}
