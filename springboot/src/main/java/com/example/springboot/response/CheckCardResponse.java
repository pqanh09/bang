package com.example.springboot.response;

import java.util.ArrayList;
import java.util.List;

public class CheckCardResponse extends Response{
	private boolean canUse = false;
	private List<String> userCanBeAffectList = new ArrayList<>();
	private boolean mustChooseTarget;
	
	
	public boolean isMustChooseTarget() {
		return mustChooseTarget;
	}

	public void setMustChooseTarget(boolean mustChooseTarget) {
		this.mustChooseTarget = mustChooseTarget;
	}

	public boolean isCanUse() {
		return canUse;
	}

	public void setCanUse(boolean canUse) {
		this.canUse = canUse;
	}

	public List<String> getUserCanBeAffectList() {
		return userCanBeAffectList;
	}

	public void setUserCanBeAffectList(List<String> userCanBeAffectList) {
		this.userCanBeAffectList = userCanBeAffectList;
	}

	public CheckCardResponse(boolean canUse) {
		super();
		this.responseType = ResponseType.CheckCard;
		this.canUse = canUse;
	}

	public CheckCardResponse(boolean canUse, List<String> userCanBeAffectList, boolean mustChooseTarget) {
		super();
		this.canUse = canUse;
		this.userCanBeAffectList = userCanBeAffectList;
		this.mustChooseTarget = mustChooseTarget;
		this.responseType = ResponseType.CheckCard;
	}




	public CheckCardResponse() {
		super();
	}

	
}
