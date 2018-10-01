package com.example.springboot.command;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.request.Request;
import com.example.springboot.response.OldCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardNotInTurnResponse;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;
import com.example.springboot.utils.CardUtils;

public class JailActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(JailActionCmd.class);
	

	public JailActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		String userName = request.getUser();
		if(!match.getCurrentTurn().getCharacter().getUserName().equals(userName)) {
			logger.error("Error not support for " + userName + "at this time");
			return;
		}
		// get cards for escaping the jail;
		Card card = commonService.getFromNewCardList(match, 1).get(0);
		checkJail(match, card, commonService);
	}
	public static void checkJail(Match match, Card card, CommonService commonService) {
		//get Character
		Character character = match.getCurrentTurn().getCharacter();
		String userName = character.getUserName();
		String sessionId = match.getUserMap().get(userName);
		
		character.setBeJailed(false);
		// find jailCard
		Card jailCard = BangUtils.getCardByCardType(character.getCardsInFront(),JailCard.class);
		character.getCardsInFront().remove(jailCard);
		
		commonService.notifyCharacter(match.getMatchId(), character, sessionId);
		
		commonService.addToOldCardList(jailCard, match);
		commonService.addToOldCardList(card, match);
		match.getCurrentTurn().setAlreadyCheckedJail(true);
		
		OldCardResponse oldCardResponse = new OldCardResponse();
		oldCardResponse.getCards().add(card);
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/oldcard", oldCardResponse);
		
		List<Card> cards = new ArrayList<>();
//		cards.add(card);
		if(Suit.hearts.equals(card.getSuit())) {
			cards.add(CardUtils.escapeJailCard);
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/usedCardNotInTurn", new UseCardNotInTurnResponse(userName, cards, null, null));
			match.getCurrentTurn().run(match);
		} else {
			cards.add(CardUtils.loseTurn);
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/usedCardNotInTurn", new UseCardNotInTurnResponse(userName, cards, null, null));
			commonService.endTurn(userName, match);
		}
		
	}

}
