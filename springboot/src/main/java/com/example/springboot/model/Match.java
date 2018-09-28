package com.example.springboot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.model.role.Role;

public class Match {
	public enum MatchStatus {
		playing, waiting;
	}
	private static final Logger logger = LoggerFactory.getLogger(Match.class);
	private String matchId;
	private MatchStatus status = MatchStatus.waiting;
	private Map<String, Character> characterMap = new HashMap<>();
	private LinkedList<Card> newCards = new LinkedList<>();
	private LinkedList<Card> oldCards = new LinkedList<>();
	private boolean putInPool = false;
	private List<Card> cardPool = new ArrayList<>();
	private Map<String, String> userMap = new HashMap<>();
	private LinkedList<String> playerTurnQueue = new LinkedList<>();
	private Map<Pair<String, String>, Integer> rangeMap = new HashMap<>();
	private TurnNode currentTurn;
	private List<String> outlaws = new ArrayList<>();
	private List<String> sheriff  = new ArrayList<>();
	private List<String> renegades = new ArrayList<>();
	private List<String> deputies  = new ArrayList<>();
	//for VeraCuster
	private Hero veraCuster;
	private String veraCusterPlayer;
	
	public Match(String matchId, String userName, String sessionId, SimpMessageSendingOperations messagingTemplate) {
		this.userMap.put(userName, sessionId);
		this.playerTurnQueue.add(userName);
		this.matchId = matchId;
		this.status = MatchStatus.waiting;
		this.characterMap.put(userName, new Character(userName));
	}
	public boolean addPlayer(String userName, String sessionId, SimpMessageSendingOperations messagingTemplate) {
		if(userMap.containsKey(userName)) {
			return false;
		} else {
			this.userMap.put(userName, sessionId);
			this.playerTurnQueue.add(userName);
			this.characterMap.put(userName, new Character(userName));
			return true;
		}
	}
	
	
	
//	public void updateRangeMap() {
//		rangeMap.clear();
//		List<String> list = new ArrayList<>(playerTurnQueue);
//		for (int i = 0; i < list.size(); i++) {
//			String begin = list.get(i);
//			LinkedList<String> tmp = new LinkedList<>(playerTurnQueue);
//			while (!tmp.peek().equals(begin)) {
//				tmp.add(tmp.poll());
//			}
//			int range = 1;
//			tmp.poll();
//			String end;
//			while (!tmp.isEmpty()) {
//				end = tmp.poll();
//				if (!rangeMap.containsKey(Pair.of(begin, end)) && !rangeMap.containsKey(Pair.of(end, begin))) {
//					rangeMap.put(Pair.of(begin, end), range);
//				} else {
//					Pair<String, String> pair = rangeMap.containsKey(Pair.of(begin, end)) ? Pair.of(begin, end)
//							: Pair.of(end, begin);
//					int oldRange = rangeMap.get(pair);
//					if (range < oldRange) {
//						rangeMap.put(pair, range);
//					}
//				}
//				range++;
//			}
//		}
//	}
//	
//	public void disconnecPlayer(SimpMessageSendingOperations messagingTemplate, String playerName) {
//		playerTurnQueue.remove(playerName);
//		messagingTemplate.convertAndSend("/topic/"+matchId+"/server", new UserResponse(ResponseType.Leave, playerName));
//		
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getVeraCusterPlayer() {
		return veraCusterPlayer;
	}
	public void setVeraCusterPlayer(String veraCusterPlayer) {
		this.veraCusterPlayer = veraCusterPlayer;
	}
	public TurnNode getCurrentTurn() {
		return currentTurn;
	}
	public Hero getVeraCuster() {
		return veraCuster;
	}
	public void setVeraCuster(Hero veraCuster) {
		this.veraCuster = veraCuster;
	}
	public void setCurrentTurn(TurnNode currentTurn) {
		this.currentTurn = currentTurn;
	}
	
	public Map<Pair<String, String>, Integer> getRangeMap() {
		return rangeMap;
	}
	public void setRangeMap(Map<Pair<String, String>, Integer> rangeMap) {
		this.rangeMap = rangeMap;
	}
	public MatchStatus getStatus() {
		return status;
	}
	public void setStatus(MatchStatus status) {
		this.status = status;
	}
	public String getMatchId() {
		return matchId;
	}
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	public Map<String, Character> getCharacterMap() {
		return characterMap;
	}
	public void setCharacterMap(Map<String, Character> characterMap) {
		this.characterMap = characterMap;
	}
	public LinkedList<Card> getNewCards() {
		return newCards;
	}
	public void setNewCards(LinkedList<Card> newCards) {
		this.newCards = newCards;
	}
	public LinkedList<Card> getOldCards() {
		return oldCards;
	}
	public void setOldCards(LinkedList<Card> oldCards) {
		this.oldCards = oldCards;
	}
	public boolean isPutInPool() {
		return putInPool;
	}
	public void setPutInPool(boolean putInPool) {
		this.putInPool = putInPool;
	}
	public List<Card> getCardPool() {
		return cardPool;
	}
	public void setCardPool(List<Card> cardPool) {
		this.cardPool = cardPool;
	}
	public Map<String, String> getUserMap() {
		return userMap;
	}
	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}
	public LinkedList<String> getPlayerTurnQueue() {
		return playerTurnQueue;
	}
	public void setPlayerTurnQueue(LinkedList<String> playerTurnQueue) {
		this.playerTurnQueue = playerTurnQueue;
	}
	public List<String> getOutlaws() {
		return outlaws;
	}
	public void setOutlaws(List<String> outlaws) {
		this.outlaws = outlaws;
	}
	public List<String> getSheriff() {
		return sheriff;
	}
	public void setSheriff(List<String> sheriff) {
		this.sheriff = sheriff;
	}
	public List<String> getRenegades() {
		return renegades;
	}
	public void setRenegades(List<String> renegades) {
		this.renegades = renegades;
	}
	public List<String> getDeputies() {
		return deputies;
	}
	public void setDeputies(List<String> deputies) {
		this.deputies = deputies;
	}
	
	public void addRole(Role role, String userName) {
		switch (role.getRoleType()) {
		case SCERIFFO:
			sheriff.add(userName);
			break;
		case VICE:
			deputies.add(userName);		
				break;
		case FUORILEGGE:
			outlaws.add(userName);
			break;
		case RINNEGATO:
			renegades.add(userName);
			break;
		default:
			break;
		}
	}
	
}
