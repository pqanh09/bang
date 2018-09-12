package com.example.springboot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.springboot.controller.WebSocketEventListener;
import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.role.RoleType;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.OldCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.response.UserResponse;
import com.example.springboot.utils.BangUtils;

@Service("commonService")
public class CommonService {
	private static final Logger logger = LoggerFactory.getLogger(CommonService.class);
	@Autowired
	private SimpMessageSendingOperations simpMessageSendingOperations;
	
	public SimpMessageSendingOperations getSimpMessageSendingOperations() {
		return simpMessageSendingOperations;
	}
	public void setSimpMessageSendingOperations(SimpMessageSendingOperations simpMessageSendingOperations) {
		this.simpMessageSendingOperations = simpMessageSendingOperations;
	}
	public void playerDead(String userName, boolean beKilled, Match match) {
		Character character = match.getCharacterMap().get(userName);
		character.setRoleImage(character.getRole().getImage());
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/character", new CharacterResponse(ResponseType.Dead, userName, character.getVO()));
//		BangUtils.notifyCharacter(simpMessageSendingOperations, character,userMap.get(userName));
		match.getPlayerTurnQueue().remove(userName);
		if(match.getPlayerTurnQueue().size() == 1) {
			String lastPlayer = match.getPlayerTurnQueue().peek();
			Character lastCharacter = match.getCharacterMap().get(lastPlayer);
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Winner, lastCharacter.getRole().getRoleType().toString()));
			return;
		}
		addToOldCardList(character.getCardsInFront(), match);
		addToOldCardList(character.getCardsInHand(), match);
		updateRangeMap(match);
		
		if(beKilled) {
			String killingPlayer = match.getPlayerTurnQueue().peek();
			String killerSessionId = match.getUserMap().get(killingPlayer);
			Character killerCharacter = match.getCharacterMap().get(killingPlayer);
			RoleType roleTypeDeathPlayer = character.getRole().getRoleType();
			
			if(RoleType.FUORILEGGE.equals(roleTypeDeathPlayer)) {
				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Gitf, killerCharacter.getUserName()));
				// get cards for character;
				List<Card> cards = getFromNewCardList(match, 3);
				killerCharacter.getCardsInHand().addAll(cards);
				killerCharacter.setNumCardsInHand(killerCharacter.getCardsInHand().size());
				//udpate character for user 
//				notifyPrivateCharacter(killingPlayer, killerCharacter);
				BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), killerCharacter, killerSessionId);
			} else if(RoleType.VICE.equals(roleTypeDeathPlayer) && RoleType.SCERIFFO.equals(killerCharacter.getRole().getRoleType())) {
				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.LoseCard, killerCharacter.getUserName()));
				addToOldCardList(killerCharacter.getCardsInFront(), match);
				addToOldCardList(killerCharacter.getCardsInHand(), match);
				//udpate character for user 
				BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), killerCharacter, killerSessionId);
			} else if(RoleType.SCERIFFO.equals(roleTypeDeathPlayer)) {
				List<String> remainPlayers = new ArrayList<>(match.getPlayerTurnQueue());
				boolean hasFuorilegge = false;
				for (String player : remainPlayers) {
					if(match.getCharacterMap().get(player).getRole().getRoleType().equals(RoleType.FUORILEGGE)) {
						hasFuorilegge = true;
						break;
					}
				}
				if(hasFuorilegge) {
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Winner, RoleType.FUORILEGGE.toString()));
				} else {
					logger.error("Not yet handled 93 CommonService ");
				}
				//TODO notify end
				match.getPlayerTurnQueue().clear();
				return;
			}
		}
	}

	

	public void updateRangeMap(Match match) {
		
		match.getRangeMap().clear();
		List<String> list = new ArrayList<>(match.getPlayerTurnQueue());
		for (int i = 0; i < list.size(); i++) {
			String begin = list.get(i);
			LinkedList<String> tmp = new LinkedList<>(match.getPlayerTurnQueue());
			while (!tmp.peek().equals(begin)) {
				tmp.add(tmp.poll());
			}
			int range = 1;
			tmp.poll();
			String end;
			while (!tmp.isEmpty()) {
				end = tmp.poll();
				if (!match.getRangeMap().containsKey(Pair.of(begin, end)) && !match.getRangeMap().containsKey(Pair.of(end, begin))) {
					match.getRangeMap().put(Pair.of(begin, end), range);
				} else {
					Pair<String, String> pair = match.getRangeMap().containsKey(Pair.of(begin, end)) ? Pair.of(begin, end)
							: Pair.of(end, begin);
					int oldRange = match.getRangeMap().get(pair);
					if (range < oldRange) {
						match.getRangeMap().put(pair, range);
					}
				}
				range++;
			}
		}
	}
	
	public void disconnecPlayer(Match match, String playerName) {
		match.getPlayerTurnQueue().remove(playerName);
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Leave, playerName));
		
	}
	public void addToOldCardList(Card card, Match match) {
		match.getOldCards().add(card);
		while (match.getOldCards().size() > 2) {
			match.getCardPool().add(match.getOldCards().poll());
		}
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/oldcard", new OldCardResponse(Arrays.asList(card)));
	}

	public void addToOldCardList(List<Card> cards, Match match) {
		match.getOldCards().addAll(cards);
		while (match.getOldCards().size() > 2) {
			match.getCardPool().add(match.getOldCards().poll());
		}
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/oldcard", new OldCardResponse(cards));
	}
	public List<Card> getFromNewCardList(Match match, int n) {
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			if (match.getNewCards().size() < 10) {
				Collections.shuffle(match.getCardPool());
				match.getNewCards().addAll(match.getCardPool());
				match.getCardPool().clear();
			}
			cards.add(match.getNewCards().pollFirst());
		}
		return cards;
	}

	public void createTurnNode(Match match) {
		String firstTurn = match.getPlayerTurnQueue().peekFirst();
		Character firstCharacter = match.getCharacterMap().get(firstTurn);
		match.setCurrentTurn(new TurnNode(simpMessageSendingOperations, match.getMatchId()));
		match.getCurrentTurn().resetTurnNode(firstCharacter);
	}
	public void endTurn(String userName, Match match) {
		if(match.getPlayerTurnQueue().peek().equals(userName)){
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/turn", new TurnResponse(ResponseType.EndTurn, userName));
			match.getPlayerTurnQueue().poll();
			callNextPlayerTurn(match);
			match.getPlayerTurnQueue().add(userName);
		} else {
			logger.error("Turn service endTurn ERROR @!@@@@@!");
		}
	}
	public void callNextPlayerTurn(Match match) {
		String nextPlayer = match.getPlayerTurnQueue().peek();
		if(StringUtils.isNotBlank(nextPlayer)) {
			Character nextCharacter = match.getCharacterMap().get(nextPlayer);
			match.getCurrentTurn().resetTurnNode(nextCharacter);
			match.getCurrentTurn().run();
		} else {
			logger.error("Turn service callNextPlayerTurn ERROR @!@@@@@!");
//			tableService.getsimpMessageSendingOperations().convertAndSend("/topic/turn", new TurnResponse(ResponseType.Winner, userName));
//			return;
		}
	}
	public void callTurn(Match match) {
		match.getCurrentTurn().getNextPlayer().poll();
		if (match.getCurrentTurn().getNextPlayer().peek() == null) {
			// request player in turn continue using card
			match.getCurrentTurn().requestPlayerUseCard();
		} else {
			match.getCurrentTurn().requestOtherPlayerUseCard(match);
		}
	}
	
}
