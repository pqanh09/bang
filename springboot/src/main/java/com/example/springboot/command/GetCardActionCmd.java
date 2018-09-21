package com.example.springboot.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.BillNoface;
import com.example.springboot.model.hero.BlackJack;
import com.example.springboot.model.hero.ClausTheSaint;
import com.example.springboot.model.hero.KitCarlson;
import com.example.springboot.model.hero.PixiePete;
import com.example.springboot.request.Request;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class GetCardActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(GetCardActionCmd.class);
	

	public GetCardActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		String userName = request.getUser();
		if(!match.getCurrentTurn().getCharacter().getUserName().equals(userName)) {
			return;
		}
		String sessionId = match.getUserMap().get(userName);
		//get Character
		Character character = match.getCharacterMap().get(userName);
		// get cards for character;
		if(character.getHero() instanceof BlackJack) {
			// skill hero  BlackJack
			character.getHero().useSkill(match, character, commonService, 1, null);
		} else if(character.getHero() instanceof PixiePete) {
			// skill hero  PixiePete
			character.getHero().useSkill(match, character, commonService, 1, null);
		} else if(character.getHero() instanceof BillNoface) {
			// skill hero  BillNoface
			character.getHero().useSkill(match, character, commonService, 1, null);
		} else if(character.getHero() instanceof ClausTheSaint) {
			// skill hero  ClausTheSaint
			character.getHero().useSkill(match, character, commonService, 1, null);
			return;
		} else if(character.getHero() instanceof KitCarlson) {
			// skill hero  KitCarlson
			character.getHero().useSkill(match, character, commonService, 1, null);
			return;
		} else {
			List<Card> cards = commonService.getFromNewCardList(match, Constants.DEFAULT_CARD);
			character.getCardsInHand().addAll(cards);
			character.setNumCardsInHand(character.getCardsInHand().size());
		}
		
		//udpate character for user 
		commonService.notifyCharacter(match.getMatchId(), character, sessionId);
		// update alreadyGetCard = true
		match.getCurrentTurn().setAlreadyGetCard(true);
		match.getCurrentTurn().requestPlayerUseCard();
	}

}
