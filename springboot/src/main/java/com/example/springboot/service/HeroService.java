package com.example.springboot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springboot.model.hero.Hero;
import com.example.springboot.model.hero.VultureSam;

@Service("heroService")
public class HeroService {
	private List<Hero> heros = new ArrayList<>();
	public HeroService() {
		for(int i = 0; i < 30; i ++) {
			heros.add(new VultureSam("VultureSam"+String.valueOf(i)));
		}
		Collections.shuffle(heros);
	}
	public List<Hero> getHeros(int n) {
		Collections.shuffle(heros);
		return heros.subList(0, n);
	}
	public Hero getHero(String id) {
		Hero hero = null;
		for (Hero hr : heros) {
			if(hr.getId().equals(id)) {
				hero = hr;
			}
		}
		return hero;
	}
	
}
