import java.util.*;

class GameData {
	private ArrayList<Unit> myUnits;
	private ArrayList<Unit> enemyUnits;
	private int[][] resources;
	
	private int playerId;
	private int metal;
	
	private Constants constants;
	
	public GameData(GameMap m,Player p) {
		constants = new Constants();
		myUnits = new ArrayList<Unit>();
		enemyUnits = new ArrayList<Unit>();
		playerId = p.getIndex();
		metal = p.getMetal();
		
		ArrayList<Unit> allUnits = m.getUnits();
		for(Unit u : allUnits) {
			if(u != null && u.getOwner()==p.getIndex()) {
				myUnits.add(u);
			}
		}
		TreeSet<Integer> enemyScanned = new TreeSet<Integer>();
		TreeSet<Integer> resourceScanned = new TreeSet<Integer>();
		int[][] board = m.getBoard();
		for(Unit u : myUnits) {
			double dist = 0;
			int d = 0;
			if(u.getType().equals("base")) {
				dist = ((double)constants.baseRadarDistance) + 0.1;
				d = constants.baseRadarDistance;
			} else if(u.getType().equals("robot")) {
				dist = ((double)constants.robotRadarDistance) + 0.1;
				d = constants.robotRadarDistance;
			} else {
				//System.out.println("unknown unit type");
				continue;
			}
			for(int x=u.getX()-d;x<=u.getX()+d;x++) {
				for(int y=u.getY()-d;y<=u.getY()+d;y++) {
					if(x>0 && y > 0 && x<constants.boardWidth && y < constants.boardHeight && Math.sqrt(Math.pow(u.getX()-x,2)+Math.pow(u.getY()-y,2))<dist) {
						if(board[x][y]>0) {
							if(allUnits.get(board[x][y]).getOwner()!=playerId) {
								enemyScanned.add(board[x][y]);
							}
						} else if(board[x][y]==-1) {
							resourceScanned.add((x << 10) + y);
						}
					}
				}
			}
		}
		
		for(Integer i : enemyScanned) {
			enemyUnits.add(allUnits.get(i));
		}
		
		resources = new int[resourceScanned.size()][2];
		int j=0;
		for(Integer i : resourceScanned) {
			resources[j][0] = i >> 10;
			resources[j][1] = i - (resources[j][0] << 10);
			j++;
		}
		
	}
	
	
	
}