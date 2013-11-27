import java.util.*;

class GameData {
	private Unit[] myUnits;
	private Unit[] enemyUnits;
	private int[][] resources; // r[][0] = x, r[][1] = y
	
	private int playerId;
	private int metal;
	
	private Constants constants;
	
	public GameData() {
		
	}
	
	public Unit[] getMyUnits() {
		return myUnits;
	}
	
	public Unit[] getEnemyUnits() {
		return enemyUnits;
	}
	
	public int getId() {
		return playerId;
	}
	
	public int getMetal() {
		return metal;
	}
	
	public Constants getConstants() {
		return constants;
	}
	
}