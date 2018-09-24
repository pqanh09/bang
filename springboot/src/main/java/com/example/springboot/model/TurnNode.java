package com.example.springboot.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.card.BarrelCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.BelleStar;
import com.example.springboot.model.hero.Jourdonnais;
import com.example.springboot.model.hero.LuckyDuke;
import com.example.springboot.response.BarrelCardResponse;
import com.example.springboot.response.CardResponse;
import com.example.springboot.response.GetCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.CommonService;

public class TurnNode {
	private static final Logger logger = LoggerFactory.getLogger(TurnNode.class);
	private boolean alreadyCheckedJail = false;
	private boolean alreadyCheckedDynamite = false;
	private boolean alreadyGetCard = false;
	private boolean alreadyUseBangCard = false;
	private Character character;
	private ResponseType action;
	private LinkedList<String> nextPlayer = new LinkedList<>();
	private List<Card> cardTemp = new ArrayList<>();
	private List<String> playerUsedBarrel = new ArrayList<>();
	
	//for SlabTheKiller
	private List<String> playerUsedMissed = new ArrayList<>();
	//for UncleWill 
	private boolean uncleWill;
	//for DocHolyday
	private boolean docHolyday;
	//for JoseDelgado
	private int joseDelgado;
	
	private List<String> playerSkillBarrel = new ArrayList<>();
	private SimpMessageSendingOperations simpMessageSendingOperations;
	private String matchId;
	private CommonService commonService;
	
	private List<String> temp = new ArrayList<>();
	
	
	public List<String> getTemp() {
		return temp;
	}

	public void setTemp(List<String> temp) {
		this.temp = temp;
	}

	public List<String> getPlayerUsedMissed() {
		return playerUsedMissed;
	}

	public void setPlayerUsedMissed(List<String> playerUsedMissed) {
		this.playerUsedMissed = playerUsedMissed;
	}

	public List<String> getPlayerSkillBarrel() {
		return playerSkillBarrel;
	}

	public void setPlayerSkillBarrel(List<String> playerSkillBarrel) {
		this.playerSkillBarrel = playerSkillBarrel;
	}

	public SimpMessageSendingOperations getSimpMessageSendingOperations() {
		return simpMessageSendingOperations;
	}

	public void setSimpMessageSendingOperations(SimpMessageSendingOperations simpMessageSendingOperations) {
		this.simpMessageSendingOperations = simpMessageSendingOperations;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public List<String> getPlayerUsedBarrel() {
		return playerUsedBarrel;
	}

	public void setPlayerUsedBarrel(List<String> playerUsedBarrel) {
		this.playerUsedBarrel = playerUsedBarrel;
	}

	public List<Card> getCardTemp() {
		return cardTemp;
	}

	public void setCardTemp(List<Card> cardTemp) {
		this.cardTemp = cardTemp;
	}

	public ResponseType getAction() {
		return action;
	}

	public void setAction(ResponseType action) {
		this.action = action;
	}

	public LinkedList<String> getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(LinkedList<String> nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}
	public void resetTurnNode(Character character) {
		this.character = character;
		this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/turn", new TurnResponse(ResponseType.Turn, character.getUserName()));
		this.alreadyCheckedDynamite = false;
		this.alreadyCheckedJail = false;
		this.alreadyGetCard = false;
		this.alreadyUseBangCard = false;
		this.action = ResponseType.Unknown;
		this.nextPlayer.clear();
		this.cardTemp.clear();
		this.playerSkillBarrel.clear();
		this.playerUsedBarrel.clear();
		this.playerUsedMissed.clear();
		//for UncelWill
		this.uncleWill = false;
		//for DocHolyday
		this.docHolyday = false;
		//for JoseDelgado
		this.joseDelgado = 0;
		
		this.temp.clear();
	}

	public TurnNode(CommonService commonService, String matchId) {
		super();
		this.simpMessageSendingOperations = commonService.getSimpMessageSendingOperations();
		this.commonService = commonService;
		this.matchId = matchId;
	}
	public void requestPlayerUseCard() {
		cardTemp.clear();
		nextPlayer.clear();
		simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/cardaction", new UserResponse(ResponseType.UseCard, character.getUserName(), 20));
	}
	
	public void requestOtherPlayerUseCard(Match match) {
		if(ResponseType.Bang.equals(action) || ResponseType.Gatling.equals(action) || ResponseType.Duello.equals(action) || ResponseType.Indians.equals(action)) {
			// request player use cards
			String targetUser = nextPlayer.peek();
			if(targetUser != null) {
				Character targetCharater = match.getCharacterMap().get(targetUser);
				if((ResponseType.Bang.equals(action) || ResponseType.Gatling.equals(action)) && !playerSkillBarrel.contains(targetUser) && targetCharater.getHero() instanceof Jourdonnais){
					targetCharater.getHero().useSkill(match, targetCharater, commonService, 1, null);
//					if(result) {
//						match.getCurrentTurn().getPlayerUsedMissed().add(targetUser);
//					}
					playerSkillBarrel.add(targetUser);
					if (nextPlayer.peek() == null) {
						// request player in turn continue using card
						requestPlayerUseCard();
					} else {
						requestOtherPlayerUseCard(match);
					}
					return;
				}
				boolean hasBarrel = false;
				if((ResponseType.Bang.equals(action) || ResponseType.Gatling.equals(action)) && !playerUsedBarrel.contains(targetUser)) {
					
					for (Card card : targetCharater.getCardsInFront()) {
						if(card instanceof BarrelCard) {
							hasBarrel = true;
							break;
						}
					}
				}
				// request player use cards
				if(hasBarrel) {
					if((ResponseType.Bang.equals(action) || ResponseType.Gatling.equals(action)) && match.getCurrentTurn().getCharacter().getHero() instanceof BelleStar) {
						this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/action", new BarrelCardResponse(action, targetUser, false, 10));
					} else {
						this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/action", new BarrelCardResponse(action, targetUser, true, 10));
					}
				} else {
					this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/action", new CardResponse(action, targetUser, 5));
				}
			} else {
				logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 87");
			}	
		} else if(ResponseType.Panic.equals(action) || ResponseType.CatPalou.equals(action)) {
			String targetUser = nextPlayer.peek();
			Character targetCharacter = match.getCharacterMap().get(targetUser);
			List<Card> cards = targetCharacter.getCardsInFront();
			// request player use cards
			this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/action", new GetCardResponse(character.getUserName(), action, targetUser, cards, !targetCharacter.getCardsInHand().isEmpty(), 10));
		} else if(ResponseType.GeneralStore.equals(action)) {
			// request player use cards
			String targetUser = nextPlayer.peek();
			if(targetUser != null) {
				// request player use cards
				this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/action", new CardResponse(ResponseType.GeneralStore, targetUser, cardTemp, 10));
			} else {
				logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Null nextplayer");
			}	
		} else {
				logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Null nextplayer");
		}
	}

	public void run(Match match) {
		//Check Dynamite
		if(!this.alreadyCheckedDynamite && character.isHasDynamite()) {
			if(character.getHero() instanceof LuckyDuke) {
				character.getHero().useSkill(match, character, commonService, 1, null);
			} else {
				this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/cardaction", new UserResponse(ResponseType.DrawCardDynamite, character.getUserName(), 10));	
			}
			return;
		}
		this.alreadyCheckedDynamite = true;
		//Check jail
		if(!this.alreadyCheckedJail && character.isBeJailed()) {
			if(character.getHero() instanceof LuckyDuke) {
				character.getHero().useSkill(match, character, commonService, 1, null);
			} else {
				this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/cardaction", new UserResponse(ResponseType.DrawCardJail, character.getUserName(), 10));
			}
			return;
		}
		this.alreadyCheckedJail = true;
		//get cards
		if(!alreadyGetCard) {
			// request player get card
			// request player use cards
			cardTemp.clear();
			nextPlayer.clear();
			this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/cardaction", new UserResponse(ResponseType.GetCard, character.getUserName(), 10));
		} else {
			requestPlayerUseCard();
		}
	}
	

	public boolean isAlreadyCheckedJail() {
		return alreadyCheckedJail;
	}


	public void setAlreadyCheckedJail(boolean alreadyCheckedJail) {
		this.alreadyCheckedJail = alreadyCheckedJail;
	}


	public boolean isAlreadyCheckedDynamite() {
		return alreadyCheckedDynamite;
	}


	public void setAlreadyCheckedDynamite(boolean alreadyCheckedDynamite) {
		this.alreadyCheckedDynamite = alreadyCheckedDynamite;
	}


	public boolean isAlreadyGetCard() {
		return alreadyGetCard;
	}


	public void setAlreadyGetCard(boolean alreadyGetCard) {
		this.alreadyGetCard = alreadyGetCard;
	}

	public boolean isAlreadyUseBangCard() {
		return alreadyUseBangCard;
	}

	public void setAlreadyUseBangCard(boolean alreadyUseBangCard) {
		this.alreadyUseBangCard = alreadyUseBangCard;
	}

	public boolean isUncleWill() {
		return uncleWill;
	}

	public void setUncleWill(boolean uncleWill) {
		this.uncleWill = uncleWill;
	}

	public boolean isDocHolyday() {
		return docHolyday;
	}

	public void setDocHolyday(boolean docHolyday) {
		this.docHolyday = docHolyday;
	}

	public int getJoseDelgado() {
		return joseDelgado;
	}

	public void setJoseDelgado(int joseDelgado) {
		this.joseDelgado = joseDelgado;
	}

	
}
