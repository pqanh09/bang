package com.example.springboot.command;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BarrelCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.hero.SlabTheKiller;
import com.example.springboot.request.Request;
import com.example.springboot.response.BarrelCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UseCardNotInTurnResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;
import com.example.springboot.utils.CardUtils;

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
		
//		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard", new UseCardResponse(userName, ResponseType.UseBarrel, card, null));
		
		TurnNode turnNode = match.getCurrentTurn();
		turnNode.getPlayerUsedBarrel().add(userName);
		List<Card> cards = new ArrayList<>();
		cards.add(BangUtils.getCardByCardType(match.getCharacterMap().get(userName).getCardsInFront(),BarrelCard.class));
		cards.add(card);
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardNotInTurn", new UseCardNotInTurnResponse(userName, cards));
		
		if(Suit.hearts.equals(card.getSuit())) {
			if(ResponseType.Bang.equals(turnNode.getAction()) 
					&& turnNode.getCharacter().getHero() instanceof SlabTheKiller
					&& !turnNode.getPlayerUsedMissed().contains(userName)) {
				turnNode.getCharacter().getHero().useSkill(match, match.getCharacterMap().get(userName), commonService, 1, null);
			} else {
				turnNode.getNextPlayer().poll();
			}
		}
		if (turnNode.getNextPlayer().peek() == null) {
			// request player in turn continue using card
			turnNode.requestPlayerUseCard();
		} else {
			turnNode.requestOtherPlayerUseCard(match);
		}
	}


}
