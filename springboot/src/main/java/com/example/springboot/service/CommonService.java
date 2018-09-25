package com.example.springboot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.hero.ApacheKid;
import com.example.springboot.model.hero.BelleStar;
import com.example.springboot.model.hero.GregDigger;
import com.example.springboot.model.hero.HerbHunter;
import com.example.springboot.model.hero.SuzyLafayette;
import com.example.springboot.model.hero.VultureSam;
import com.example.springboot.model.role.RoleType;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.OldCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.response.UseCardResponse;
import com.example.springboot.response.UserResponse;

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
	private boolean usingSkillOfHeroWhenAPlayerDead(String deadPlayer, Match match, Character deadCharacter) {
		boolean addToOldCardList = false;
		List<Character> vultureSams = new ArrayList<>();
		for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
			Character character = entry.getValue();
			if(!deadPlayer.equals(character.getUserName()) && (character.getHero() instanceof VultureSam || character.getHero() instanceof HerbHunter || character.getHero() instanceof GregDigger)) {
				if(character.getHero() instanceof VultureSam) {
					vultureSams.add(character);
				} else {
					character.getHero().useSkill(match, character, this, 1, null);
					notifyCharacter(match.getMatchId(), character, match.getUserMap().get(character.getUserName()));
				}
				
			}
		}
		if(!vultureSams.isEmpty()) {
			List<Card> cards = new ArrayList<>();
			cards.addAll(deadCharacter.getCardsInFront());
			cards.addAll(deadCharacter.getCardsInHand());
			if(cards.isEmpty()) {
				return false;
			} else {
				addToOldCardList = true;
				Collections.shuffle(cards);
				Collections.shuffle(cards);
				Collections.shuffle(cards);
			}
			int numCards = cards.size()/vultureSams.size();
			Map<String, Object> others = new HashMap<String, Object>();
			//first hero
			Character fisrtCharacter = vultureSams.get(0);
			others.put("cards", cards.subList(0, numCards));
			fisrtCharacter.getHero().useSkill(match, fisrtCharacter, this, 1, others);
			notifyCharacter(match.getMatchId(), fisrtCharacter , match.getUserMap().get(fisrtCharacter.getUserName()));
			//second hero
			if(vultureSams.size() > 1) {
				Character secondCharacter = vultureSams.get(1);
				others.put("cards", cards.subList(numCards, cards.size()));
				secondCharacter.getHero().useSkill(match, secondCharacter, this, 1, others);
				notifyCharacter(match.getMatchId(), secondCharacter , match.getUserMap().get(secondCharacter.getUserName()));
			}
		}
		return addToOldCardList;
	}
	private boolean checkEndGame(Match match, Character character) {
		boolean endGame = false;
		if(match.getPlayerTurnQueue().size() == 1) {
			String lastPlayer = match.getPlayerTurnQueue().peek();
			Character lastCharacter = match.getCharacterMap().get(lastPlayer);
			character.setRoleImage(character.getRole().getImage());
			notifyCharacter(match.getMatchId(), character, match.getUserMap().get(character.getUserName()));
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Winner, lastCharacter.getRole().getRoleType().toString()));
			endGame = true;
		} else {
			if(RoleType.SCERIFFO.equals(character.getRole().getRoleType())) {
				List<String> remains = new ArrayList<>(match.getPlayerTurnQueue()); 
				for (String player : remains) {
					Character plCharacter = match.getCharacterMap().get(player);
					plCharacter.setRoleImage(plCharacter.getRole().getImage());
					notifyCharacter(match.getMatchId(), plCharacter, match.getUserMap().get(plCharacter.getUserName()));
				}
				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Winner, RoleType.FUORILEGGE.toString()));
				endGame = true;
			}
		}
		return endGame;
	}
	public void playerDead(String userName, boolean beKilled, Match match) {
		match.getPlayerTurnQueue().remove(userName);
		Character character = match.getCharacterMap().get(userName);
		if(character == null) {
			return;
		}
		if(character.getRole() != null) {
			character.setRoleImage(character.getRole().getImage());
		}
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/character", new CharacterResponse(ResponseType.Dead, userName, character.getVO()));
//		notifyCharacter(character,userMap.get(userName));
		
		if(checkEndGame(match, character)) {
			return;
		}
		//
		if(!usingSkillOfHeroWhenAPlayerDead(userName, match, character)) {
			addToOldCardList(character.getCardsInFront(), match);
			addToOldCardList(character.getCardsInHand(), match);
		};
			
		//	
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
				notifyCharacter(match.getMatchId(), killerCharacter, killerSessionId);
			} else if(RoleType.VICE.equals(roleTypeDeathPlayer) && RoleType.SCERIFFO.equals(killerCharacter.getRole().getRoleType())) {
				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.LoseCard, killerCharacter.getUserName()));
				addToOldCardList(killerCharacter.getCardsInFront(), match);
				addToOldCardList(killerCharacter.getCardsInHand(), match);
				//udpate character for user 
				notifyCharacter(match.getMatchId(), killerCharacter, killerSessionId);
			} else if(RoleType.SCERIFFO.equals(roleTypeDeathPlayer)) {
				killerCharacter.setRoleImage(killerCharacter.getRole().getImage());
				notifyCharacter(match.getMatchId(), killerCharacter, killerSessionId);
				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Winner, RoleType.FUORILEGGE.toString()));
				match.getPlayerTurnQueue().clear();
				return;
			}
		}
	}
	public void notifyCharacter(String matchId, Character character, String sessionId) {
		simpMessageSendingOperations.convertAndSend("/topic/"+matchId+"/character", new CharacterResponse(ResponseType.Character, character.getUserName(), character.getVO()));
		simpMessageSendingOperations.convertAndSendToUser(sessionId, "/queue/"+matchId+"/character", new CharacterResponse(ResponseType.Character, character.getUserName(), character));
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
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Leave, playerName));
   		playerDead(playerName, false, match);
		
//		match.getPlayerTurnQueue().remove(playerName);
//		Character playerCharacter = match.getCharacterMap().get(playerName);
//		playerCharacter.setRoleImage(playerCharacter.getRole().getImage());
		
	}
	public void addToOldCardList(Card card, Match match) {
		match.getOldCards().add(card);
		while (match.getOldCards().size() > 2) {
			match.getCardPool().add(match.getOldCards().poll());
		}
//		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/oldcard", new OldCardResponse(Arrays.asList(card)));
	}

	public void addToOldCardList(List<Card> cards, Match match) {
		if(!cards.isEmpty()) {
			match.getOldCards().addAll(cards);
			while (match.getOldCards().size() > 2) {
				match.getCardPool().add(match.getOldCards().poll());
			}
//			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/oldcard", new OldCardResponse(cards));
		}
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

	public boolean createTurnNode(Match match) {
		String firstTurn = match.getPlayerTurnQueue().peekFirst();
		Character firstCharacter = match.getCharacterMap().get(firstTurn);
		match.setCurrentTurn(new TurnNode(this, match.getMatchId()));
		match.getCurrentTurn().resetTurnNode(firstCharacter);
		return useSkillOfVeraCuster(firstCharacter, match);
		
	}
	private boolean useSkillOfVeraCuster(Character character, Match match) {
		boolean canStart = true;
		if(match.getVeraCuster() != null && character.getUserName().equals(match.getVeraCusterPlayer())) {
			match.getVeraCuster().useSkill(match, match.getCurrentTurn().getCharacter(),  this, 1, null);
			//wait to copy hero's skill
			canStart =  false;
			
		}
		return canStart;
	}
	public void endTurn(String userName, Match match) {
		if(match.getPlayerTurnQueue().peek().equals(userName)){
			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/turn", new TurnResponse(ResponseType.EndTurn, userName));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("endTurn: ", e);
			}
			match.getPlayerTurnQueue().poll();
			callNextPlayerTurn(match, userName);
		} else {
			logger.error("Turn service endTurn ERROR @!@@@@@!");
		}
	}
	public void callNextPlayerTurn(Match match, String oldPlayer) {
		String nextPlayer = match.getPlayerTurnQueue().peek();
		if(StringUtils.isNotBlank(nextPlayer)) {
			Character nextCharacter = match.getCharacterMap().get(nextPlayer);
			match.getCurrentTurn().resetTurnNode(nextCharacter);
			match.getPlayerTurnQueue().add(oldPlayer);
			if(useSkillOfVeraCuster(nextCharacter, match)) {
				match.getCurrentTurn().run(match);
			}
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
	public  Card getCardInHand(Character character, String id) {
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
	public  Card getCardInHand(Match match, Character character, String id, String targetUser) {
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
//			if(StringUtils.isNoneBlank(targetUser)) {
//				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard",
//						new UseCardResponse(character.getUserName(), result, targetUser));
//			} else {
//				simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard",
//						new UseCardResponse(character.getUserName(), result, null));
//			}
			if(character.getHero() instanceof SuzyLafayette && character.getCardsInHand().isEmpty()) {
				character.getHero().useSkill(match, character, this, 1, null);
			}
			notifyCharacter(match.getMatchId(), character, match.getUserMap().get(character.getUserName()));
		}
		return result;
	}
	public Card getCardInFront(Character character, String id) {
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
	public void useSkillOfApacheKid(Match match, List<String> userCanBeAffectList, Card card, boolean notify) {
		if(!Suit.diamonds.equals(card.getSuit())) {
			return;
		}
		List<String> apacheKids = new ArrayList<>();
		for (String player : userCanBeAffectList) {
			if(match.getCharacterMap().get(player).getHero() instanceof ApacheKid) {
				apacheKids.add(player);
				if(notify) {
					simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, player, match.getCharacterMap().get(player).getHero(), null, null));
				}
			}
		}
		userCanBeAffectList.removeAll(apacheKids);
	}
	public List<String> checkRangeToUseCard(Match match, Character rootPlayer,  int rangeCard) {
		List<String> userCanBeAffectList = new ArrayList<>();
		Character targetPlayer;
		for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
			targetPlayer = entry.getValue();
			if(targetPlayer.getUserName().equals(rootPlayer.getUserName()) || !match.getPlayerTurnQueue().contains(entry.getKey())) {
				continue;
			}
			int range = (match.getRangeMap().get(Pair.of(rootPlayer.getUserName(), targetPlayer.getUserName())) != null) ? match.getRangeMap().get(Pair.of(rootPlayer.getUserName(), targetPlayer.getUserName())) : match.getRangeMap().get(Pair.of(targetPlayer.getUserName(), rootPlayer.getUserName()));
			if(!(rootPlayer.getHero() instanceof BelleStar)) {
				range += targetPlayer.getOthersView();
			}
			if((rootPlayer.getViewOthers() + rangeCard) >= range) {
				userCanBeAffectList.add(targetPlayer.getUserName());
			}
		}
		return userCanBeAffectList;
	}
}
