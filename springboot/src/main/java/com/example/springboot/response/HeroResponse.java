package com.example.springboot.response;

import java.util.List;

import com.example.springboot.model.hero.Hero;

/**
 * Response the list of heros, player must pick one hero to play
 * 
 */
public class HeroResponse extends Response{
	private List<Hero> heros;

	public HeroResponse(List<Hero> heros) {
		super();
		this.responseType = ResponseType.Hero;
		this.heros = heros;
	}

	public List<Hero> getHeros() {
		return heros;
	}


	public void setHeros(List<Hero> heros) {
		this.heros = heros;
	}


	public HeroResponse() {
		super();
	}
	

}
