import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Server {
	public static int NUM_PLAYERS = 2;
	public static int PLAYER_PORT= 1985;
	public static int VIEWER_PORT = 1984;
	
	private ArrayList<Player> players;
	private ArrayList<Viewer> viewers;
	private boolean s_running;
	private ServerSocket playerConnector;
	
	private class ViewerServer implements Runnable {
		private ServerSocket server;
		private boolean vs_running;
		
		public ViewerServer(int port) throws IOException {
			server = new ServerSocket(port);
		}
		
		public void run() {
			vs_running = true;
			while(vs_running) {
				try {
					Socket s = server.accept();
					viewers.add(new Viewer(s));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class Viewer {
		private Socket sock;
		private PrintWriter out;
		
		public Viewer(Socket s) throws IOException {
			sock = s;
			out = new PrintWriter(sock.getOutputStream());
		}
		
		public void write(String s) throws IOException {
			out.println(s);
		}
	}
	
	public Server() throws IOException {
		players = new ArrayList<Player>();
		viewers = new ArrayList<Viewer>();
		playerConnector = new ServerSocket(PLAYER_PORT);
		(new Thread(new ViewerServer(VIEWER_PORT))).start();
	}
	
	public void runGameLoop() {
		s_running = true;
		while(s_running) {
			try {
				String[] n = new String[2];
				n[0] = getRandomName();
				n[1] = getRandomName();
				playGame(n);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getRandomName() {
		File clientFolder = new File("clients");
		String[] files = clientFolder.list();
		int randIndex = (int)Math.floor(Math.random() * (double)files.length);
		return files[randIndex];
	}
	
	public Process startClient(String name,int authCode) throws Exception {
		System.out.println("./run "+name+" "+authCode);
		final Process ret = Runtime.getRuntime().exec("./run "+name+" "+authCode);
		(new Thread(new Runnable() {
			public void run() {
				try {
					BufferedReader i = new BufferedReader(new InputStreamReader(ret.getInputStream()));
					String line = "";
					while((line = i.readLine())!=null) {
						System.out.println(line);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		})).start();
		return ret;
	}
	
	public void playGame(String[] clients) throws Exception {
		players.clear();
		Process[] ps = new Process[2];
		ps[0] = null;
		ps[1] = null;
		for(int i=0;i<2;i++) {
			int authCode = (int)(Math.random() * 100000.0);
			Process p = null;
			try {
				p = startClient(clients[i],authCode);
				playerConnector.setSoTimeout(5000);
				Player newPlayer = new Player(playerConnector.accept(),authCode,i);
				players.add(newPlayer);
				ps[i]=p;
			} catch(Exception e) {
				System.out.println("Client failed to start!");
				if(p != null) {p.destroy();};
				if(ps[0] != p && ps[0] != null) {
					ps[0].destroy();
				}
				throw e;
			}
		}
		//System.out.println("Length: " +players.size());
		Player[] playersArr = {players.get(0), players.get(1)};
		GameMap gm = new GameMap(playersArr);
		JFrame j = new JFrame("game");
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setSize(600,600);
		j.setLayout(new BorderLayout());
		j.add(new GameDisplay(gm),BorderLayout.CENTER);
		j.setVisible(true);
		//play game
		for(int i=0;i<2000;i++) {
			System.out.println("turn "+i);
			
			Act[] acts;
			
			acts = playersArr[0].doMove(new GameData(gm,playersArr[0]));
			if(acts!=null) {
				for(Act a : acts) {
					gm.handleAction(playersArr[0],a);
				}
			}
			acts = playersArr[1].doMove(new GameData(gm,playersArr[1]));
			if(acts != null) {
				for(Act a : acts) {
					gm.handleAction(playersArr[1],a);
				}
			} 
			gm.decreaseCooldowns();
			j.repaint();
		}
		j.setVisible(false);
		
		ps[0].destroy();
		ps[1].destroy();
		playersArr[0].halt();
		playersArr[1].halt();
		
		int[] units = {0, 0};
		for(Unit u : gm.getUnits()) {
			if(u == null) {
				continue;
			}
			if(u.getOwner() == playersArr[0].getIndex()) {
				units[0]++;
			} else if(u.getOwner() == playersArr[1].getIndex()) {
				units[1]++;
			}
		}
		
		if(units[0] > units[1]) {
			System.out.println("Player 0 is the winner");
		} else {
			System.out.println("Player 1 is the winner");
		}
	}
	
}