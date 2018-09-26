package com.example.springboot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springboot.model.hero.ApacheKid;
import com.example.springboot.model.hero.BartCassidy;
import com.example.springboot.model.hero.BelleStar;
import com.example.springboot.model.hero.BillNoface;
import com.example.springboot.model.hero.BlackJack;
import com.example.springboot.model.hero.CalamityJanet;
import com.example.springboot.model.hero.ChuckWengam;
import com.example.springboot.model.hero.ClausTheSaint;
import com.example.springboot.model.hero.DocHolyday;
import com.example.springboot.model.hero.ElGringo;
import com.example.springboot.model.hero.ElenaFuente;
import com.example.springboot.model.hero.GregDigger;
import com.example.springboot.model.hero.HerbHunter;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.model.hero.JesseJones;
import com.example.springboot.model.hero.JohnnyKisch;
import com.example.springboot.model.hero.JoseDelgado;
import com.example.springboot.model.hero.Jourdonnais;
import com.example.springboot.model.hero.KitCarlson;
import com.example.springboot.model.hero.LuckyDuke;
import com.example.springboot.model.hero.MollyStark;
import com.example.springboot.model.hero.PatBrennan;
import com.example.springboot.model.hero.PaulRegret;
import com.example.springboot.model.hero.PedroRamirez;
import com.example.springboot.model.hero.PixiePete;
import com.example.springboot.model.hero.RoseDoolan;
import com.example.springboot.model.hero.SeanMallory;
import com.example.springboot.model.hero.SidKetchum;
import com.example.springboot.model.hero.SlabTheKiller;
import com.example.springboot.model.hero.SuzyLafayette;
import com.example.springboot.model.hero.TequilaJoe;
import com.example.springboot.model.hero.UncleWill;
import com.example.springboot.model.hero.VeraCuster;
import com.example.springboot.model.hero.VultureSam;
import com.example.springboot.model.hero.WillyTheKid;

@Service("heroService")
public class HeroService {
	private List<Hero> heros = new ArrayList<>();

	public HeroService() {
		// auto
//		heros.add(new ApacheKid());
//		heros.add(new BartCassidy());
//		heros.add(new BelleStar());
//		heros.add(new BillNoface());
//		heros.add(new BlackJack());
//		heros.add(new CalamityJanet());
//		heros.add(new ElenaFuente());
//		heros.add(new GregDigger());
//		heros.add(new HerbHunter());
//		heros.add(new JohnnyKisch());
//		heros.add(new Jourdonnais());
//		heros.add(new MollyStark());
//		heros.add(new PaulRegret());
//		heros.add(new PixiePete());
//		heros.add(new RoseDoolan());
		heros.add(new SeanMallory());
//		heros.add(new SlabTheKiller());
//		heros.add(new SuzyLafayette());
//		heros.add(new TequilaJoe());
//		heros.add(new VultureSam());
//		heros.add(new WillyTheKid());
//		heros.add(new ElGringo());
//		// manual
//		heros.add(new ChuckWengam());
//		heros.add(new PedroRamirez());
//		heros.add(new JesseJones());
//		heros.add(new PatBrennan());
//		heros.add(new SidKetchum());
		heros.add(new UncleWill());
		heros.add(new JoseDelgado());
//		heros.add(new DocHolyday());
//		// auto-manual
////		heros.add(new VeraCuster());
//		heros.add(new LuckyDuke());
//		heros.add(new KitCarlson());
//		heros.add(new ClausTheSaint());

		Collections.shuffle(heros);
	}

	public List<Hero> getHerosByNumber(int n) {
		Collections.shuffle(heros);
		return heros.subList(0, n);
	}

	public Hero getHero(String id) {
		Hero hero = null;
		for (Hero hr : heros) {
			if (hr.getId().equals(id)) {
				hero = hr;
			}
		}
		return hero;
	}

	public List<Hero> getHeros() {
		return heros;
	}

	public void setHeros(List<Hero> heros) {
		this.heros = heros;
	}

}
