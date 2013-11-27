import java.util.*;

class GameMap {
	
	private ArrayList<Unit> units;
	private int[][] resources; //[[x1,y1],[x2,y2],...]
	
	private Constants constants = new Constants();
	
	private int[][] board; //[x][y] gives a code: -1 for resources, 0 for empty, unit id otherwise

	private int unitId;
	
	private volatile Player[] players;
	
	public GameMap(Player[] _players) { //Randomly generate our gamemap
		players = _players;
		units = new ArrayList<Unit>();
		units.add(null);
		unitId = 1;
		
		board = new int[constants.boardWidth][constants.boardHeight];
		for(int i=0;i<board.length;i++) {
			for(int j=0;j<board[i].length;j++) {
				board[i][j]=0;
			}
		}
		
		resources = new int[constants.numResources][2];
		
		Unit leftStart = new Unit("base",0,unitId);
		board[constants.boardWidth/4][constants.boardHeight/2] = unitId;
		units.add(leftStart);
		leftStart.setX(constants.boardWidth/4); leftStart.setY(constants.boardHeight/2);
		leftStart.setCooldown(0);
		leftStart.setHealth(constants.baseStartingHealth);
		unitId++;
		
		Unit rightStart = new Unit("base",1,unitId);
		board[constants.boardWidth*3/4][constants.boardHeight/2] = unitId;
		units.add(rightStart);
		rightStart.setX(constants.boardWidth*3/4); rightStart.setY(constants.boardHeight/2);
		rightStart.setCooldown(0);
		rightStart.setHealth(constants.baseStartingHealth);
		unitId++;
		
		for(int i=0;i<constants.numResources;i++) {
			while(true) {
				int randX = (int)(Math.random()*constants.boardWidth);
				int randY = (int)(Math.random()*constants.boardHeight);
				if(board[randX][randY]==0) {
					resources[i][0] = randX;
					resources[i][1] = randY;
					board[randX][randY] = -1;
					break;
				}
			}
		}
	}
	
	public ArrayList<Unit> getUnits() {
		return units;
	}
	
	public int[][] getResources() {
		return resources;
	}
	
	public int[][] getBoard() {
		return board;
	}
	
	public void handleAction(Player acting,Act action) {
		if(action.getUnit()>=units.size()) {
			//System.out.println("Invalid action: unit ID out of range");
			return;
		}
		Unit u = units.get(action.getUnit());
		if(u==null) {
			//System.out.println("Invalid action: unit is null");
			return;
		}
		String actionType = action.getType();
		if(actionType==null) {
			//System.out.println("Invalid action: action type is null");
			return;
		}
		if(u.getCooldown()!=0) {
			//System.out.println("Invalid action: unit is on a cooldown");
			return;
		}
		if(players[u.getOwner()] != acting) {
			//System.out.println("Invalid action: Player acting on a unit they don't own");
			return;
		}
		if(action.getX()<0 || action.getX()>=constants.boardWidth || action.getY()<0 || action.getY() >= constants.boardHeight) {
			//System.out.println("Invalid action: goes off the map");
			return;
		}
		if(u.getType().equals("base")) {
			if(actionType.equals("buildRobot")) {
				if(board[action.getX()][action.getY()]!=0) {
					//System.out.println("Invalid action: cannot build robot there, something is already there");
					return;
				}
				if(players[u.getOwner()].getMetal()<constants.robotCost) {
					//System.out.println("Invalid action: player does not have enough metal to build a robot");
					return;
				}
				//build robot
				u.setCooldown(constants.buildRobotCooldown);
				Unit newU = new Unit("robot",u.getOwner(),unitId);
				board[action.getX()][action.getY()] = unitId;
				newU.setX(action.getX());
				newU.setY(action.getY());
				newU.setCooldown(1);
				newU.setHealth(constants.robotStartingHealth);
				units.add(newU);
				unitId++;
				players[u.getOwner()].subMetal(constants.robotCost);
			}
			else if(actionType.equals("reclaim")) {
				u.setType("robot");
				players[u.getOwner()].addMetal(constants.reclaimValue);
				u.setCooldown(constants.reclaimCooldown);
				u.setHealth(constants.robotStartingHealth);
			}
		} else if(u.getType().equals("robot")) {
			if(actionType.equals("move")) {
				if(Math.abs(action.getX()-u.getX())>1 || Math.abs(action.getY()-u.getY())>1) {
					//System.out.println("Robot cannot move there: too far away");
					return;
				}
				if(board[action.getX()][action.getY()]!=0) {
					//System.out.println("Robot cannot move there: something else is there");
					return;
				}
				//move
				board[u.getX()][u.getY()]=0;
				u.setX(action.getX());
				u.setY(action.getY());
				board[u.getX()][u.getY()]=u.getId();
				u.setCooldown(1);
			}
			else if(actionType.equals("collect")) {
				if(Math.abs(action.getX()-u.getX())>1 || Math.abs(action.getY()-u.getY())>1) {
					//System.out.println("Robot cannot collect there: too far away");
					return;
				}
				if(board[action.getX()][action.getY()]!=-1) {
					//System.out.println("Robot cannot collect there: not a resource");
					return;
				}
				//collect
				players[u.getOwner()].addMetal(constants.resourceGain);
				u.setCooldown(1);
			}
			else if(actionType.equals("attack")) {
				if(Math.sqrt(Math.pow(u.getX()-action.getX(),2)+Math.pow(u.getY()-action.getY(),2))>((double)constants.robotAttackDistance)+0.02) {
					//System.out.println("Robot cannot attack there: too far away");
					return;
				}
				if(board[action.getX()][action.getY()]<=0) {
					//System.out.println("Robot cannot attack there: nothing is there");
					return;
				}
				//attack
				Unit damaging = units.get(board[action.getX()][action.getY()]);
				if(damaging.getOwner()==u.getOwner()) {
					//System.out.println("Can't attack your own units");
					return;
				}
				damaging.setHealth(damaging.getHealth()-constants.robotAttackDamage);
				if(damaging.getHealth()<1) {
					board[action.getX()][action.getY()] = 0;
					units.set(damaging.getId(),null);
				}
				u.setCooldown(1);
			}
			else if(actionType.equals("buildBase")) {
				if(players[u.getOwner()].getMetal()<constants.baseCost) {
					//System.out.println("Invalid action: player does not have enough metal to build a base");
					return;
				}
				///turn into base
				u.setType("base");
				u.setCooldown(constants.buildBaseCooldown);
				players[u.getOwner()].subMetal(constants.baseCost);
				u.setHealth(constants.baseStartingHealth);
			}
		} else {
			//System.out.println("Unit has an invalid type somehow");
			return;
		}
	}
	
	public void decreaseCooldowns() {
		for(Unit u : units) {
			if(u != null) {
				u.decrCooldown();
			}
		}
	}
	
}