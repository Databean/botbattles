import java.util.*;
import javax.swing.*;

class GameAI {
	
	private GameData gd;
	
	public GameAI() {
		
	}
	
	public Act[] doMove(GameData gd) {
		this.gd = gd;
		
		ArrayList<Act> send = new ArrayList<Act>();
		
		for(Unit u : gd.getMyUnits()) {
			if(u.getType().equals("robot")) {
				send.add(Act.move(u,(int)Math.round(u.getX()+Math.random()*2-1),(int)Math.round(u.getY()+Math.random()*2-1)));
			} else if(u.getType().equals("base")) {
				send.add(Act.buildRobot(u,u.getX()+1,u.getY()));
			}
		}
		System.out.println("constant robotAttackDistance:"+gd.getConstants().robotAttackDistance);
		
		Act[] ret = send.toArray(new Act[0]);
		return ret;
		
	}
	
}