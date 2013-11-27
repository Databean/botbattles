import java.awt.*;
import javax.swing.*;

class GameDisplay extends JComponent {
	
	private Color base0 = new Color(155,0,0);
	private Color base1 = new Color(0,100,0);
	private Color robot0 = new Color(255,120,120);
	private Color robot1 = new Color(120,200,120);
	
	private GameMap gm;
	
	private static int scale = 3;
	
	public GameDisplay(GameMap gm) {
		this.gm = gm;
	}
	
	public void paint(Graphics g) {
		for(Unit u : gm.getUnits()) {
			if(u == null) { continue; }
			if(u.getOwner()==0) {
				if(u.getType().equals("robot")) {
					g.setColor(robot0);
				} else {
					g.setColor(base0);
				}
			} else {
				if(u.getType().equals("robot")) {
					g.setColor(robot1);
				} else {
					g.setColor(base1);
				}
			}
			g.fillRect(scale*u.getX(),scale*u.getY(),scale,scale);
		}
		for(int[] r : gm.getResources()) {
			g.setColor(Color.BLUE);
			g.fillRect(r[0]*scale,r[1]*scale,scale,scale);
		}
	}
	
}