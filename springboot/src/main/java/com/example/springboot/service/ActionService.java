package com.example.springboot.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.command.*;
import com.example.springboot.request.Request;
import com.example.springboot.request.RequestType;

@Service("actionService")
public class ActionService {
	@Autowired
	TableService tableService;
	@Autowired
	RoleService roleService;
	@Autowired
	HeroService heroService;
	@Autowired
	CardService cardService;
	@Autowired
	ShareService shareService;
	@Autowired
	TurnService turnService;

	private Map<RequestType, ActionCmd> actionMap = new HashMap<>();

	public ActionService() {

	}

	public void initCmds() {
		if (actionMap.isEmpty()) {
			actionMap.put(RequestType.PickHero,
					new PickHeroActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.GetCard,
					new GetCardActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.RemoveCard,
					new RemoveCardActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.Panic,
					new PanicCardActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.GeneralStore,
					new GenelralStoreActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.CatPalou,
					new CatPalouActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.UseCard,
					new UseCardActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.CheckCard,
					new CheckCardActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.EndTurn,
					new EndTurnActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.DrawCardJail,
					new JailActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.DrawCardDynamite,
					new DynamiteActionCmd(tableService, heroService, shareService, turnService));
			actionMap.put(RequestType.UseBarrel,
					new BarrelActionCmd(tableService, heroService, shareService, turnService));
		}
	}

	public void perform(Request request) {
		ActionCmd actionCmd = actionMap.get(request.getActionType());
		if (actionCmd != null) {
			actionCmd.execute(request);
		} else {
			// TODO
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		}
	}
}
