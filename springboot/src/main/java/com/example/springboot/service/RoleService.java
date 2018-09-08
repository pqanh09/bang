package com.example.springboot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springboot.model.role.FuorileggeRole;
import com.example.springboot.model.role.RinnegatoRole;
import com.example.springboot.model.role.Role;
import com.example.springboot.model.role.SceriffoRole;
import com.example.springboot.model.role.ViceRole;

@Service("roleService")
public class RoleService {
	public RoleService() {

	}

	public List<Role> getRoles(int numberPlayer) {
		List<Role> roles = new ArrayList<>();
		if(numberPlayer < 4) return roles;
		roles.add(new SceriffoRole("SCERIFFO", "SceriffoRole"));
		roles.add(new ViceRole("VICE", "ViceRole"));
		roles.add(new FuorileggeRole("FUORILEGGE", "FuorileggeRole"));
		roles.add(new RinnegatoRole("RINNEGATO", "RinnegatoRole"));
		switch (numberPlayer) {
		case 5:
			
			break;
		case 6:

			break;
		case 7:

			break;
		case 8:

			break;
		default:
			break;
		}
		if(roles.size() >=4) {
			Collections.shuffle(roles);
		}
		return roles;

	}
}
