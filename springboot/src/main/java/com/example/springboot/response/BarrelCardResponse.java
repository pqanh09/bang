package com.example.springboot.response;

public class BarrelCardResponse extends CardResponse {

	private boolean canUseBarrel = false;
	
	public boolean isCanUseBarrel() {
		return canUseBarrel;
	}

	public void setCanUseBarrel(boolean canUseBarrel) {
		this.canUseBarrel = canUseBarrel;
	}
	
	public BarrelCardResponse(ResponseType responseType, String userName, boolean canUseBarrel, int countDown) {
		this.responseType = responseType;
		this.userName = userName;
		this.canUseBarrel = canUseBarrel;
		this.countDown = countDown;
	}
	
	public BarrelCardResponse() {
		super();
	}

}
