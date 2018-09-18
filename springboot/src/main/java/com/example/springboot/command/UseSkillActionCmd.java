package com.example.springboot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.hero.ChuckWengam;
import com.example.springboot.model.hero.ClausTheSaint;
import com.example.springboot.model.hero.DocHolyday;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.model.hero.JesseJones;
import com.example.springboot.model.hero.JoseDelgado;
import com.example.springboot.model.hero.KitCarlson;
import com.example.springboot.model.hero.LuckyDuke;
import com.example.springboot.model.hero.PatBrennan;
import com.example.springboot.model.hero.PedroRamirez;
import com.example.springboot.model.hero.SidKetchum;
import com.example.springboot.model.hero.UncleWill;
import com.example.springboot.model.hero.VeraCuster;
import com.example.springboot.request.Request;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.service.CommonService;

public class UseSkillActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(UseSkillActionCmd.class);

	public UseSkillActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		// get turn node
		String userName = request.getUser();
		String sessionId = match.getUserMap().get(userName);
		TurnNode turnNode = match.getCurrentTurn();
		Character character = turnNode.getCharacter();
		if(!character.getUserName().equals(userName)) {
			returnFalse(userName, sessionId, match);
			return;
		}
		Hero hero = character.getHero();
		if (hero instanceof ClausTheSaint || hero instanceof LuckyDuke || hero instanceof KitCarlson) {
			if(request.getStep() == 2) {
				hero.useSkill(match, character, commonService, 2, request.getOthers());
			} else {
				returnFalse(userName, sessionId, match);
			}
		}  else if (hero instanceof JesseJones || hero instanceof PatBrennan || hero instanceof SidKetchum || hero instanceof UncleWill || hero instanceof JoseDelgado || hero instanceof VeraCuster) {
			if(request.getStep() == 1) {
				hero.useSkill(match, character, commonService, 1, request.getOthers());
			} else if(request.getStep() == 2) {
				hero.useSkill(match, character, commonService, 2, request.getOthers());
			} else {
				returnFalse(userName, sessionId, match);
			}
		} else if (hero instanceof PedroRamirez) {
			if(request.getStep() == 1) {
				hero.useSkill(match, character, commonService, 1, request.getOthers());
			} else {
				returnFalse(userName, sessionId, match);
			}
		} else if (hero instanceof ChuckWengam) {
			if(request.getStep() == 1) {
				hero.useSkill(match, character, commonService, 1, request.getOthers());
			} else {
				returnFalse(userName, sessionId, match);
			}
		} else if (hero instanceof DocHolyday) {
			if(request.getStep() == 1) {
				hero.useSkill(match, character, commonService, 1, request.getOthers());
			} else if(request.getStep() == 2) {
				hero.useSkill(match, character, commonService, 2, request.getOthers());
			} else if(request.getStep() == 3) {
				hero.useSkill(match, character, commonService, 3, request.getOthers());
			} else {
				returnFalse(userName, sessionId, match);
			}
		} else if (hero instanceof VeraCuster) {
			logger.error("VeraCuster Error.............");
			returnFalse(userName, sessionId, match);
		} 
		else {
			returnFalse(userName, sessionId, match);
		}
	}
	private void returnFalse(String userName, String sessionId, Match match) {
		simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
				new SkillResponse(userName, false));
	}

	
}
