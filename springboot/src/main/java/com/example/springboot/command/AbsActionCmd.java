package com.example.springboot.command;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.service.CommonService;

public abstract class AbsActionCmd {
	protected CommonService commonService;
	protected SimpMessageSendingOperations simpMessageSendingOperations;
	public CommonService getCommonService() {
		return commonService;
	}
	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}
	public SimpMessageSendingOperations getSimpMessageSendingOperations() {
		return simpMessageSendingOperations;
	}
	public void setSimpMessageSendingOperations(SimpMessageSendingOperations simpMessageSendingOperations) {
		this.simpMessageSendingOperations = simpMessageSendingOperations;
	}
	public AbsActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super();
		this.commonService = commonService;
		this.simpMessageSendingOperations = simpMessageSendingOperations;
	}
}
