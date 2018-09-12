package com.example.springboot.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.springboot.command.*;
import com.example.springboot.model.Match;
import com.example.springboot.request.Request;
import com.example.springboot.request.RequestType;

@Service("dispatcher")
public class Dispatcher {

	@Autowired
	CommonService commonService;
	
	@Autowired
	HeroService heroService;
	
	@Autowired
	private SimpMessageSendingOperations simpMessageSendingOperations;
	
	public SimpMessageSendingOperations getSimpMessageSendingOperations() {
		return simpMessageSendingOperations;
	}
	public void setSimpMessageSendingOperations(SimpMessageSendingOperations simpMessageSendingOperations) {
		this.simpMessageSendingOperations = simpMessageSendingOperations;
	}
	
	private Map<RequestType, ActionCmd> actionMap = new HashMap<>();

	public Dispatcher() {

	}

	public void initCmds() {
		if (actionMap.isEmpty()) {
			actionMap.put(RequestType.UseBarrel,
					new BarrelActionCmd(commonService, simpMessageSendingOperations));
			actionMap.put(RequestType.CatPalou,
					new CatPalouActionCmd(commonService, simpMessageSendingOperations));
			
			actionMap.put(RequestType.PickHero,
					new PickHeroActionCmd(commonService, simpMessageSendingOperations, heroService));
			actionMap.put(RequestType.GetCard,
					new GetCardActionCmd(commonService, simpMessageSendingOperations));
			actionMap.put(RequestType.RemoveCard,
					new RemoveCardActionCmd(commonService, simpMessageSendingOperations));
			actionMap.put(RequestType.Panic,
					new PanicCardActionCmd(commonService, simpMessageSendingOperations));
			actionMap.put(RequestType.GeneralStore,
					new GenelralStoreActionCmd(commonService, simpMessageSendingOperations));
			;
			actionMap.put(RequestType.UseCard,
					new UseCardActionCmd(commonService, simpMessageSendingOperations));
			actionMap.put(RequestType.CheckCard,
					new CheckCardActionCmd(commonService, simpMessageSendingOperations));
			actionMap.put(RequestType.EndTurn,
					new EndTurnActionCmd(commonService, simpMessageSendingOperations));
			actionMap.put(RequestType.DrawCardJail,
					new JailActionCmd(commonService, simpMessageSendingOperations));
			actionMap.put(RequestType.DrawCardDynamite,
					new DynamiteActionCmd(commonService, simpMessageSendingOperations));
			
		}
	}

	public void perform(Request request, Match match) {
		ActionCmd actionCmd = actionMap.get(request.getActionType());
		if (actionCmd != null) {
			actionCmd.execute(request, match);
		} else {
			// TODO
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		}
	}
}
