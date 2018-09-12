package com.example.springboot.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.hero.Hero;
import com.example.springboot.model.role.Role;
import com.example.springboot.model.role.RoleType;
import com.example.springboot.request.Request;
import com.example.springboot.response.HeroResponse;
import com.example.springboot.response.Response;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.RoleResponse;
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.Dispatcher;
import com.example.springboot.service.CardService;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.RoleService;
//import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Controller
public class BangController2 {

//	@Autowired
//	TableService tableService;
//	@Autowired
//	RoleService roleService;
//	@Autowired
//	HeroService heroService;
//	@Autowired
//	CardService cardService;
//	@Autowired
//	ActionService actionService;
//	@Autowired
//	TurnService turnService;
//
//	public BangController2() {
//		// TODO Auto-generated constructor stub
//	}
//
////	private static final ObjectMapper objectMapper = new ObjectMapper();
//
//	@MessageMapping("/game.execute")
//	public void sendMessage(@Payload Request request, SimpMessageHeaderAccessor sha) {
//		actionService.initCmds();
//		String user = (String) sha.getSessionAttributes().get(Constants.HEADER_ACCESSOR_USER);
//		// TODO check username
//		if (user == null) {
//			user = tableService.getSessionIdMap().get(sha.getUser().getName());
//		}
//		request.setUser(user);
//		actionService.perform(request);
//	}
//
//	@MessageMapping("/game.join")
////	@SendTo("/topic/join")
//	public void addUser(@Payload Request request, SimpMessageHeaderAccessor sha) {
//		String userName = request.getId();
//		String sessionId = sha.getUser().getName();
//		System.out.println(sessionId);
//		if(tableService.getUserMap().containsKey(userName)) {
//			messagingTemplate.convertAndSendToUser(sessionId, "/queue/join", new UserResponse(ResponseType.Unknown, userName));
//		} else {
//			// Add username in web socket session
//			sha.getSessionAttributes().put(Constants.HEADER_ACCESSOR_USER, userName);
//			// TODO check userName(request.getId()) existed
//			tableService.getUserMap().put(userName, sha.getUser().getName());
//			tableService.getMessagingTemplateMap().put(userName, messagingTemplate);
//			tableService.getSessionIdMap().put(sha.getUser().getName(), userName);
//			UserResponse joinResponse = new UserResponse(ResponseType.Join, userName);
//			messagingTemplate.convertAndSendToUser(sessionId, "/queue/join", joinResponse);
//			messagingTemplate.convertAndSend("/topic/join", joinResponse);
//			
//			
//			if(tableService.getUserMap().size() >= 2) {
//				getHero();
//			}
//		}
//	}
//
//	@Autowired
//	private SimpMessageSendingOperations messagingTemplate;
//
//
//	@MessageMapping("/game.useheroskill")
//	public void useHeroSkill() {
//		System.out.println("useHeroSkill");
//
//	}
//
//	@MessageMapping("/game.getcard")
//	public void getCard(@Payload Request request, SimpMessageHeaderAccessor sha) {
//
//		System.out.println("getCard");
//		actionService.initCmds();
//		String user = (String) sha.getSessionAttributes().get(Constants.HEADER_ACCESSOR_USER);
//		// TODO check username
//		if (user == null) {
//			user = tableService.getSessionIdMap().get(sha.getUser().getName());
//		}
//		request.setUser(user);
//		actionService.perform(request);
//	}
//
//	@MessageMapping("/game.start")
//	public void getHero() {
//		int numberPlayer = 4;
//		// get roles
//		List<Role> roles = roleService.getRoles(numberPlayer);
//		// get heros for user pick
//		List<Hero> heros = heroService.getHerosByNumber(numberPlayer * 4);
//		// get cards
//		tableService.setNewCards(new LinkedList<>(cardService.getCards()));
//		boolean foundSceriffo = false;
//		List<String> playerNotYetInTurn = new ArrayList<>();
//		int n = 0;
//		for (String userName : tableService.getUserMap().keySet()) {
//			String sessionId = tableService.getUserMap().get(userName);
//			// create Character
//			Character character = new Character(n + 1, userName);
//			tableService.getCharacterMap().put(userName, character);
//			tableService.getCharacterMap().put(userName, character);
//			//
//			Role role = roles.get(n);
//			character.setRole(role);
//			// send role for user
//			messagingTemplate.convertAndSendToUser(sessionId, "/queue/role", new RoleResponse(userName, role));
////			BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
//			// notify for all user if found Sceriffo
//			if (foundSceriffo) {
//				tableService.getPlayerTurnQueue().add(userName);
//			} else {
//				if (RoleType.SCERIFFO.equals(role.getRoleType())) {
//					character.setLifePoint(1);
//					foundSceriffo = true;
//					tableService.getPlayerTurnQueue().add(userName);
//				} else {
//					playerNotYetInTurn.add(userName);
//				}
//			}
//			// send heros for user
//			List<Hero> hrs = heros.subList(4 * n, 4 * (n + 1));
//			messagingTemplate.convertAndSendToUser(sessionId, "/queue/hero", new HeroResponse(hrs));
//			n++;
//		}
//		tableService.getPlayerTurnQueue().addAll(playerNotYetInTurn);
//		tableService.updateRangeMap();
//
//	}
//    @MessageMapping("/game.start")
//    public void startGame(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor sha) {
//    	chatMessage.setContent("Hi " + chatMessage.getSender());
//    	messagingTemplate.convertAndSendToUser(sha.getUser().getName(),"/queue/reply", chatMessage);
//    }

}
