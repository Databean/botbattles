

public class Act {
	
	private String type;
	private int x;
	private int y;
	private int unit;
	
	public Act(String _type,int _unit,int _x,int _y) {
		type = _type;
		unit = _unit;
		x = _x;
		y = _y;
	}
	
	public String getType() {
		return type;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getUnit() {
		return unit;
	}
	//for robots
	static Act move(Unit unit,int x,int y) { return new Act("move",unit.getId(),x,y); }
	static Act collect(Unit unit,int x,int y) { return new Act("collect",unit.getId(),x,y); }
	static Act attack(Unit unit,Unit attacking) { return new Act("attack",unit.getId(),attacking.getX(),attacking.getY()); }
	static Act buildBase(Unit unit) { return new Act("buildBase",unit.getId(),0,0); }
	//for bases
	static Act buildRobot(Unit unit,int x,int y) { return new Act("buildRobot",unit.getId(),x,y); }
	static Act reclaim(Unit unit) { return new Act("reclaim",unit.getId(),0,0); }
	
}