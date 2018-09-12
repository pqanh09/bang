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
//		if(numberPlayer < 4) return roles;
		SceriffoRole sceriffoRole = new SceriffoRole("SCERIFFO", "SceriffoRole", "/data/image/role/Sceriffo.jpg");
		FuorileggeRole fuorileggeRole = new FuorileggeRole("FUORILEGGE", "FuorileggeRole", "/data/image/role/Fuorilegge.jpg");
		ViceRole viceRole = new ViceRole("VICE", "ViceRole", "/data/image/role/Vice.jpg");
		RinnegatoRole rinnegatoRole = new RinnegatoRole("RINNEGATO", "RinnegatoRole", "/data/image/role/Rinnegato.jpg");
		switch (numberPlayer) {
		case 2:
			roles.add(sceriffoRole);
			roles.add(fuorileggeRole);
			break;
		case 3:
			roles.add(sceriffoRole);
			roles.add(rinnegatoRole);
			roles.add(fuorileggeRole);
			break;
		case 4:
			roles.add(sceriffoRole);
			roles.add(rinnegatoRole);
			roles.add(fuorileggeRole);
			roles.add(fuorileggeRole);
			break;
		case 5:
			roles.add(sceriffoRole);
			roles.add(rinnegatoRole);
			roles.add(fuorileggeRole);
			roles.add(fuorileggeRole);
			roles.add(viceRole);
			break;
		case 6:
			roles.add(sceriffoRole);
			roles.add(rinnegatoRole);
			roles.add(fuorileggeRole);
			roles.add(fuorileggeRole);
			roles.add(rinnegatoRole);
			roles.add(viceRole);
			break;
		case 7:
			roles.add(sceriffoRole);
			roles.add(rinnegatoRole);
			roles.add(fuorileggeRole);
			roles.add(fuorileggeRole);
			roles.add(rinnegatoRole);
			roles.add(viceRole);
			roles.add(viceRole);
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
