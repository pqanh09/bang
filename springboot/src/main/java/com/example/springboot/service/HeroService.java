package com.example.springboot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springboot.model.hero.BartCassidy;
import com.example.springboot.model.hero.BlackJack;
import com.example.springboot.model.hero.CalamityJanet;
import com.example.springboot.model.hero.ElGringo;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.model.hero.JesseJones;
import com.example.springboot.model.hero.JohnnyKisch;
import com.example.springboot.model.hero.Jourdonnais;
import com.example.springboot.model.hero.KitCarlson;
import com.example.springboot.model.hero.LuckyDuke;
import com.example.springboot.model.hero.PaulRegret;
import com.example.springboot.model.hero.PedroRamirez;
import com.example.springboot.model.hero.RoseDoolan;
import com.example.springboot.model.hero.SidKetchum;
import com.example.springboot.model.hero.SlabTheKiller;
import com.example.springboot.model.hero.SuzyLafayette;
import com.example.springboot.model.hero.UncleWill;
import com.example.springboot.model.hero.VultureSam;
import com.example.springboot.model.hero.WillyTheKid;

@Service("heroService")
public class HeroService {
	private List<Hero> heros = new ArrayList<>();
	public HeroService() {
		heros.add(new BartCassidy());
		heros.add(new BlackJack());
		heros.add(new CalamityJanet());
		heros.add(new ElGringo());
		heros.add(new JesseJones());

		heros.add(new Jourdonnais());
		heros.add(new KitCarlson());
		heros.add(new LuckyDuke());
		heros.add(new PaulRegret());
		heros.add(new PedroRamirez());
		heros.add(new RoseDoolan());
		heros.add(new SidKetchum());
		heros.add(new SlabTheKiller());
		heros.add(new SuzyLafayette());
		heros.add(new UncleWill());
		heros.add(new VultureSam());
		heros.add(new WillyTheKid());
//		heros.add(new JohnnyKisch());
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
