package com.example.springboot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.springboot.model.Match;
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

@Service("roomService")
public class RoomService {
	private Map<String, String> userRoomMap = new HashMap<>();
	private Map<String, Match> matchMap = new HashMap<>();
	
	
	public RoomService() {
	}
	
}
