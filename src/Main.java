

class Main {
	
	public static void main(String[] args) {
		try {
			Server s = new Server();
			if(args.length==2) {
				s.playGame(args);
			}
			else {
				s.runGameLoop();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}