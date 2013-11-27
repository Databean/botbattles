import java.io.*;
import java.net.*;
import java.util.*;

class Unit {
	
	private int x;
	private int y;
	private int owner;
	private int health;
	private int cooldown;
	private String type;
	private int id;
	
	public Unit(String type,int owner,int id) {
		this.type = type;
		this.owner = owner;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int newX) {
		x = newX;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int newY) {
		y = newY;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int newHealth) {
		health = newHealth;
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
	public void setCooldown(int newCooldown) {
		cooldown = newCooldown;
	}
	
	public void decrCooldown() {
		if(cooldown>0) {
			cooldown--;
		}
	}
	
	public int getOwner() {
		return owner;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String s) {
		type = s;
	}
	
	/*
	public JSONObject getJson() {
		JSONObject ret = new JSONObject();
		ret.put("x",x);
		ret.put("y",y);
		ret.put("owner",owner);
		ret.put("health",health);
		ret.put("cooldown",cooldown);
		ret.put("type",type);
	}*/
	
}