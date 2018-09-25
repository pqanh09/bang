package com.example.springboot.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.SeanMallory;
import com.example.springboot.request.Request;
import com.example.springboot.response.OldCardResponse;
import com.example.springboot.response.RemoveCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class RemoveCardEndTurnActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(RemoveCardEndTurnActionCmd.class);
	

	public RemoveCardEndTurnActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		String userName = request.getUser();
		String sessionId = match.getUserMap().get(userName);
		//get Character
		Character character = match.getCharacterMap().get(userName);
		// get cards for removing;
		List<Card> cards = new ArrayList<>();
		
		List<String> cardIds =  (List<String>) request.getOthers().get("cards");
		int numberCard = 0;
		if(character.getHero() instanceof SeanMallory) {
			numberCard = character.getCardsInHand().size() - 10;
		} else {
			numberCard = character.getCardsInHand().size() - character.getLifePoint();
		}
		//auto
		if(cardIds == null || cardIds.isEmpty() || cardIds.size() < numberCard) {
			for (int i = 0; i < numberCard; i++) {
				cards.add(character.getCardsInHand().get(i));
			}
		} else {
			for (String cardId : cardIds) {
				Card  card = BangUtils.findCardInHand(character, cardId);
				if(card == null) {
					logger.error("Error when removing card {}", cardId);
					continue;
				}
				cards.add(card);
			}
		}
		//
		
		for (Card card : cards) {
			character.getCardsInHand().remove(card);
		}
		commonService.addToOldCardList(cards, match);
		character.setNumCardsInHand(character.getCardsInHand().size());
		commonService.notifyCharacter(match.getMatchId(), character, sessionId);
		
		OldCardResponse oldCardResponse = new OldCardResponse();
		Card card = match.getOldCards().peekLast();
		if(card != null) {
			oldCardResponse.getCards().add(card);
		}
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/oldcard", oldCardResponse);
		
		commonService.endTurn(userName, match);
	}

}
