package com.example.springboot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.hero.SlabTheKiller;
import com.example.springboot.request.Request;
import com.example.springboot.response.BarrelCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.service.CommonService;

public class BarrelActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(BarrelActionCmd.class);

	public BarrelActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		String userName = request.getUser();
		SimpMessageSendingOperations simpMessageSendingOperations = commonService.getSimpMessageSendingOperations();
		// get cards for escaping the bang/gatlling;
		Card card = commonService.getFromNewCardList(match, 1).get(0);
		
		commonService.addToOldCardList(card, match);
		
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.UseBarrel, card, null));
		
		TurnNode turnNode = match.getCurrentTurn();
		turnNode.getPlayerUsedBarrel().add(userName);
		if(Suit.hearts.equals(card.getSuit())) {
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new BarrelCardResponse(ResponseType.UseBarrel, userName, true));
			if(ResponseType.Bang.equals(turnNode.getAction()) 
					&& turnNode.getCharacter().getHero() instanceof SlabTheKiller
					&& !turnNode.getPlayerUsedMissed().contains(userName)) {
				turnNode.getCharacter().getHero().useSkill(match, match.getCharacterMap().get(userName), commonService, 1, null);
			} else {
				turnNode.getNextPlayer().poll();
			}
		} else {
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new BarrelCardResponse(ResponseType.UseBarrel, userName, false));
		}
		if (turnNode.getNextPlayer().peek() == null) {
			// request player in turn continue using card
			turnNode.requestPlayerUseCard();
		} else {
			turnNode.requestOtherPlayerUseCard(match);
		}
	}


}
