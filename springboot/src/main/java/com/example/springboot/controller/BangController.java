package com.example.springboot.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.Match;
import com.example.springboot.model.Match.MatchStatus;
import com.example.springboot.model.MatchVO;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.model.role.Role;
import com.example.springboot.model.role.RoleType;
import com.example.springboot.request.Request;
import com.example.springboot.request.RequestType;
import com.example.springboot.response.HeroResponse;
import com.example.springboot.response.MatchResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.RoleResponse;
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.CardService;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.MatchService;
import com.example.springboot.service.RoleService;
import com.example.springboot.service.UserService;

@Controller
public class BangController {
	private static final Logger logger = LoggerFactory.getLogger(BangController.class);
	@Autowired
	RoleService roleService;
	@Autowired
	HeroService heroService;
	@Autowired
	CardService cardService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MatchService matchService;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	public BangController() {
		// TODO Auto-generated constructor stub
	}

//	private static final ObjectMapper objectMapper = new ObjectMapper();

	@MessageMapping("/game.execute")
	public void sendMessage(@Payload Request request, SimpMessageHeaderAccessor sha) {
//		actionService.initCmds();
//		String user = (String) sha.getSessionAttributes().get(Constants.HEADER_ACCESSOR_USER);
//		// TODO check username
//		if (user == null) {
//			user = tableService.getSessionIdMap().get(sha.getUser().getName());
//		}
//		request.setUser(user);
//		actionService.perform(request);
	}

	@MessageMapping("/user.create")
	public void addUser(@Payload Request request, SimpMessageHeaderAccessor sha) {
		if(!RequestType.Create.equals(request.getActionType())) {
			logger.error("Error "+request.getActionType()+"..........");
			return;
		}
		String userName = request.getId();
		String sessionId = sha.getUser().getName();
		if(userService.getUserMap().containsKey(userName)) {
			logger.error("Error Existed..........");
			messagingTemplate.convertAndSendToUser(sessionId, "/queue/user", new UserResponse(ResponseType.Existed, userName));
			return;
		} else {
			// Add username in web socket session
			sha.getSessionAttributes().put(Constants.HEADER_ACCESSOR_USER, userName);
			userService.getUserMap().put(userName, sessionId);
			userService.getSessionIdMap().put(sessionId, userName);
			UserResponse createResponse = new UserResponse(ResponseType.Create, userName);
			messagingTemplate.convertAndSendToUser(sessionId, "/queue/user", createResponse);
		}
	}
	
	@MessageMapping("/game.get")
	public void getMatch(SimpMessageHeaderAccessor sha) {
		String sessionId = sha.getUser().getName();
		List<MatchVO> matches = new ArrayList<MatchVO>();
		for (Entry<String, Match> entry : matchService.getMatchMap().entrySet()) {
			matches.add(new MatchVO(entry.getValue()));
		}
		MatchResponse createResponse = new MatchResponse(ResponseType.Get, matches);
		messagingTemplate.convertAndSendToUser(sessionId, "/queue/game", createResponse);
	}
	@MessageMapping("/game.create")
	public void createNewMatch(SimpMessageHeaderAccessor sha) {
		String sessionId = sha.getUser().getName();
		String userName = userService.getSessionIdMap().get(sessionId);
		if(matchService.getUserMap().containsKey(userName)) {
			return;
		} else {
			// create new match
			String matchId = userName + String.valueOf(Calendar.getInstance().getTime().getTime());
			Match match = new Match(matchId, userName, sessionId, messagingTemplate);
			matchService.getUserMap().put(userName, matchId);
			matchService.getMatchMap().put(matchId, match);
			MatchResponse createResponse = new MatchResponse(ResponseType.Create, matchId, true);
			messagingTemplate.convertAndSendToUser(sessionId, "/queue/game", createResponse);
		}
	}
	
	@MessageMapping("/game.join")
	public void joinMatch(@Payload Request request, SimpMessageHeaderAccessor sha) {
		if(!RequestType.Join.equals(request.getActionType())) {
			logger.error("Error joinMatch Join..........");
			//TODO
			return;
		}
		Match match = matchService.getMatchMap().get(request.getId());
		if(match == null) {
			logger.error("Error joinMatch not found match..........");
			//TODO
			return;
		}
		
		if(match.getStatus().equals(MatchStatus.playing)) {
			logger.error("Error joinMatch playing..........");
			//TODO
			return;
		}
		String sessionId = sha.getUser().getName();
		String userName = userService.getSessionIdMap().get(sessionId);
		match.addPlayer(userName, sessionId, messagingTemplate);
		
		matchService.getUserMap().put(userName, match.getMatchId());
		
		MatchResponse joinResponse = new MatchResponse(ResponseType.Join, match.getMatchId(), false);
		messagingTemplate.convertAndSendToUser(sessionId, "/queue/game", joinResponse);
		messagingTemplate.convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Join, userName));
	}
	
	@MessageMapping("/game.start")
	public void startGame(SimpMessageHeaderAccessor sha) {
		String sessionId = sha.getUser().getName();
		String userName = userService.getSessionIdMap().get(sessionId);
		String matchId = matchService.getUserMap().get(userName);
		
		Match match = matchService.getMatchMap().get(matchId);
		if(match == null) {
			logger.error("Error startGame not found match..........");
			//TODO
			return;
		}
		
		if(match.getStatus().equals(MatchStatus.playing)) {
			logger.error("Error startGame playing..........");
			//TODO
			return;
		}
		match.setStatus(MatchStatus.playing);
		
		int nRole = match.getPlayerTurnQueue().size();
		// get roles
		List<Role> roles = roleService.getRoles(nRole);
		// get heros for user pick
		int nHero = (heroService.getHeros().size()/nRole) * nRole;
		List<Hero> heros = heroService.getHerosByNumber(nHero);
		// get cards
		match.setNewCards(new LinkedList<>(cardService.getCards()));
		boolean foundSceriffo = false;
		List<String> playerNotYetInTurn = new ArrayList<>();
		int n = 0;
		for (String plName : match.getUserMap().keySet()) {
			String plSessionId = match.getUserMap().get(plName);
			SimpMessageSendingOperations simpMessageSendingOperations = match.getMessagingTemplateMap().get(plName);
			// create Character
			Character character = new Character(n + 1, plName);
			match.getCharacterMap().put(plName, character);
			match.getCharacterMap().put(plName, character);
			//
			Role role = roles.get(n);
			character.setRole(role);
			// send role for user
			simpMessageSendingOperations.convertAndSendToUser(plSessionId, "/queue/"+ matchId +"/role", new RoleResponse(plName, role));
			if (foundSceriffo) {
				match.getPlayerTurnQueue().add(plName);
			} else {
				if (RoleType.SCERIFFO.equals(role.getRoleType())) {
					character.setLifePoint(1);
					foundSceriffo = true;
					match.getPlayerTurnQueue().add(plName);
				} else {
					playerNotYetInTurn.add(plName);
				}
			}
			// send heros for user
			List<Hero> hrs = heros.subList(4 * n, 4 * (n + 1));
			simpMessageSendingOperations.convertAndSendToUser(plSessionId, "/queue/"+ matchId +"/hero", new HeroResponse(hrs));
			n++;
		}
		match.getPlayerTurnQueue().addAll(playerNotYetInTurn);
		match.updateRangeMap();
	}
	@MessageMapping("/game.useheroskill")
	public void useHeroSkill() {
		logger.error("useHeroSkill");
	}


}
