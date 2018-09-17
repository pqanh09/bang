package com.example.springboot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.hero.*;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.request.Request;
import com.example.springboot.service.CommonService;

public class CheckSkillActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(CheckSkillActionCmd.class);

	public CheckSkillActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		// get turn node
		String userName = request.getUser();
		TurnNode turnNode = match.getCurrentTurn();
		Hero hero = turnNode.getCharacter().getHero();
		if(hero instanceof ClausTheSaint) {
			
		} else if(hero instanceof LuckyDuke) {
			
		} else if(hero instanceof JesseJones) {
			
		} else if(hero instanceof KitCarlson) {
			
		} else if(hero instanceof PatBrennan) {
			
		} else if(hero instanceof PedroRamirez) {
			
		} else if(hero instanceof ChuckWengam) {
			
		} else if(hero instanceof SidKetchum) {
			
		} else if(hero instanceof UncleWill) {
			
		} else if(hero instanceof JoseDelgado) {
			
		} else if(hero instanceof DocHolyday) {
			
		} else if(hero instanceof VeraCuster) {
			
		} else {
			
		}
	}


}
