package com.example.springboot.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.CardType;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.ResponseType;

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
//	public static List<String> getOtherPlayer(Set<String> players, String user){
//		List<String> result = new ArrayList<>();
//		for (String urs : players) {
//			if(!urs.equals(user)) {
//				result.add(urs);
//			}
//		}
//		return result;
//	}
	public static LinkedList<String> getOtherPlayer(LinkedList<String> playerTurnQueue, String user){
		LinkedList<String> result = new LinkedList<>(playerTurnQueue);
		result.poll();
		return result;
	}
//	public static void notifyCharacter(SimpMessageSendingOperations messagingTemplate, String matchId, Character character, String sessionId) {
//		messagingTemplate.convertAndSend("/topic/"+matchId+"/character", new CharacterResponse(ResponseType.Character, character.getUserName(), character.getVO()));
//		messagingTemplate.convertAndSendToUser(sessionId, "/queue/"+matchId+"/character", new CharacterResponse(ResponseType.Character, character.getUserName(), character));
//	}
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
	public static Card getCardByCardType(List<Card> list, Class classPath) {
		Card result = null;
		for (Card card : list) {
			if(card.getClass().equals(classPath)) {
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
}
