import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import com.google.gson.*;

class Main {
	
	static final int GAME_PORT = 1985;
	
	public static void main(String[] args) {
		GameAI ai = new GameAI();
		System.out.println("Code:"+args[0]);
		Gson gson = new Gson();
		try {
			Socket s = new Socket("localhost",GAME_PORT);
			BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter w = new PrintWriter(s.getOutputStream());
			w.println(args[0]);
			w.flush();
			while(true) {
				String line = r.readLine();
				if(line==null) {
					break;
				}
				GameData gd = gson.fromJson(line,GameData.class);
				
				Act[] send = null;
				
				try {
					send = ai.doMove(gd);
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(null,e.toString());
				}
				
				String write = gson.toJson(send);
				w.println(write);
				w.flush();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}