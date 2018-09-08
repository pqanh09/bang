package com.example.springboot.command;

import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;

public abstract class AbsActionCmd {
	protected TableService tableService;
	protected ShareService shareService;
	protected HeroService heroService;
	protected TurnService turnService;
	public AbsActionCmd(TableService tableService,
			HeroService heroService, ShareService shareService, TurnService turnService) {
		super();
		this.tableService = tableService;
		this.heroService = heroService;
		this.shareService = shareService;
		this.turnService = turnService;
	}
	
}
