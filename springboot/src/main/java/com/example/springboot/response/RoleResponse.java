package com.example.springboot.response;

import com.example.springboot.model.role.Role;
/**
 * Response the role, which be set for player
 * 
 */
public class RoleResponse extends UserResponse{
	private Role role;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public RoleResponse(String userName, Role role) {
		super();
		this.responseType = ResponseType.Role;
		this.role = role;
		this.userName = userName;
	}

	public RoleResponse() {
		super();
	}
	

}
