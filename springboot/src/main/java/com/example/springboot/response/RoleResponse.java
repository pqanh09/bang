package com.example.springboot.response;

import com.example.springboot.model.role.Role;
/**
 * Response the role, which be set for player
 * 
 */
public class RoleResponse extends Response{
	private Role role;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public RoleResponse(Role role) {
		super();
		this.responseType = ResponseType.RoleReceived;
		this.role = role;
	}

	public RoleResponse() {
		super();
	}
	

}
