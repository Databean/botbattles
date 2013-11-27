import java.net.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;

public class Player {
	private Socket sock;
	private BufferedReader in;
	private PrintWriter out;
	private Gson gson;
	//private JsonReader jreader;
	//private JsonWriter jwriter;
	
	private ArrayList<Unit> myUnits;
	
	private int index;
	
	private int metal;
	
	public Player(Socket s,int authCode,int indx) throws IOException {
		sock = s;
		sock.setSoTimeout(1000);
		index = indx;
		out = new PrintWriter(sock.getOutputStream());
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		String st = in.readLine();
		System.out.println("Read:"+st);
		int firstRead = Integer.valueOf(st);
		if(firstRead != authCode) {
			throw new RuntimeException("Player did not give correct authcode");
		}
		metal = 500;
		
		gson = new Gson();
		//jreader = new JsonReader(new InputStreamReader(sock.getInputStream(),"UTF-8"));
		//jwriter = new JsonWriter(new OutputStreamWriter(sock.getOutputStream(),"UTF-8"));
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getMetal() {
		return metal;
	}
	
	public void setMetal(int _metal) {
		metal = _metal;
	}
	
	public void addMetal(int add) {
		metal += add;
	}
	
	public void subMetal(int sub) {
		metal -= sub;
	}
	
	public void halt() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Act[] doMove(GameData gd) {
		String toWrite = gson.toJson(gd);
		out.println(toWrite);
		//System.out.println(toWrite);
		out.flush();
		try {
			String read = in.readLine();
			if(read != null) {
				Act[] ret = gson.fromJson(read,Act[].class);
				//System.out.println(read);
				return ret;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}