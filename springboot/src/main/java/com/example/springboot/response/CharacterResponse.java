package com.example.springboot.response;

import com.example.springboot.model.CharacterVO;

public class CharacterResponse extends UserResponse{
	private CharacterVO character;

	public CharacterVO getCharacter() {
		return character;
	}

	public void setCharacter(CharacterVO character) {
		this.character = character;
	}

	public CharacterResponse(ResponseType responseType, String userName, CharacterVO character) {
		super(responseType, userName);
		this.character = character;
	}

	public CharacterResponse() {
		super();
	}

	

	
}
