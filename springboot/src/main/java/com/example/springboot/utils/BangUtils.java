package com.example.springboot.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.CardType;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.model.card.*;

public class BangUtils {
	public static void sendRole() {
		
	}
	public static Card findCardInHand(Character character, String id) {
		for (Card card : character.getCardsInHand()) {
			if(card.getId().equals(id)) {
				return card;
			}
		}
		return null;
	}
	public static Card findCardInFront(Character character, String id) {
		for (Card card : character.getCardsInFront()) {
			if(card.getId().equals(id)) {
				return card;
			}
		}
		return null;
	}
	public static List<String> getOtherPlayer(Set<String> players, String user){
		List<String> result = new ArrayList<>();
		for (String urs : players) {
			if(!urs.equals(user)) {
				result.add(urs);
			}
		}
		return result;
	}
	public static LinkedList<String> getOtherPlayer(LinkedList<String> playerTurnQueue, String user){
		LinkedList<String> result = new LinkedList<>(playerTurnQueue);
		result.poll();
		return result;
	}
	public static void notifyCharacter(SimpMessageSendingOperations messagingTemplate, Character character, String sessionId) {
		messagingTemplate.convertAndSend("/topic/character", new CharacterResponse(ResponseType.Character, character.getUserName(), character.getVO()));
		messagingTemplate.convertAndSendToUser(sessionId, "/queue/character", new CharacterResponse(ResponseType.Character, character.getUserName(), character));
	}
	public static Card getCardInHand(Character character, String id) {
		Card result = null;
		for (Card card : character.getCardsInHand()) {
			if(card.getId().equals(id)) {
				result = card;
				break;
			}
		}
		if(result != null) {
			character.getCardsInHand().remove(result);
			character.setNumCardsInHand(character.getCardsInHand().size());
		}
		return result;
	}
	public static Card getCardInFront(Character character, String id) {
		Card result = null;
		for (Card card : character.getCardsInFront()) {
			if(card.getId().equals(id)) {
				result = card;
				break;
			}
		}
		if(result != null) {
			character.getCardsInFront().remove(result);
		}
		return result;
	}
	public static Card getCardByCardType(List<Card> list, CardType cardType) {
		Card result = null;
		for (Card card : list) {
			if(cardType.equals(card.getCardType())) {
				result = card; 
				break;
			}
		}
		return result;
	}
	public static boolean hasCard(Character character, Class classPath) {
		for (Card card : character.getCardsInFront()) {
			if(card.getClass().equals(classPath)) {
				return true;
			}
		}
		return false;
	}
	//return list of player can use Bang  or Panic card 
	public static List<String> checkRangeToUseCard(Map<Pair<String, String>, Integer> rangeMap, Character rootPlayer, Map<String, Character> characterMap, int rangeCard, LinkedList<String> playerTurnQueue) {
		List<String> userCanBeAffectList = new ArrayList<>();
		Character targetPlayer;
		for (Entry<String, Character> entry : characterMap.entrySet()) {
			targetPlayer = entry.getValue();
			if(targetPlayer.getUserName().equals(rootPlayer.getUserName()) || !playerTurnQueue.contains(entry.getKey())) {
				continue;
			}
			int range = (rangeMap.get(Pair.of(rootPlayer.getUserName(), targetPlayer.getUserName())) != null) ? rangeMap.get(Pair.of(rootPlayer.getUserName(), targetPlayer.getUserName())) : rangeMap.get(Pair.of(targetPlayer.getUserName(), rootPlayer.getUserName()));
			range += targetPlayer.getOthersView();
			if((rootPlayer.getViewOthers() + rangeCard) >= range) {
				userCanBeAffectList.add(targetPlayer.getUserName());
			}
		}
		return userCanBeAffectList;
	}
}
