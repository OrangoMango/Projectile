package com.orangomango.projectile.multiplayer;

import java.net.*;
import java.io.*;

public class Client{
	private String host;
	private int port;
	private String username;
	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	
	public Client(String host, int port, String username){
		this.host = host;
		this.port = port;
		this.username = username;
		try {
			this.socket = new Socket(host, port);
			this.writer = new ObjectOutputStream(this.socket.getOutputStream());
			this.reader = new ObjectInputStream(this.socket.getInputStream());
			this.writer.writeObject(username);
		} catch (IOException ex){
			close();
		}
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void send(GameState gs){
		try {
			//System.out.println("Step 1.: "+gs.entities.get(0).getX());
			System.out.println("Writing");
			this.writer.reset();
			this.writer.writeObject(gs);
		} catch (IOException ex){
			ex.printStackTrace();
			System.exit(0);
			//close();
		}
	}
	
	public Object listen(){
		try {
			System.out.println("..Reading");
			Object gs = (Object)this.reader.readObject();
			System.out.println("..Done");
			//System.out.println("Step 2.: "+this.gameState.entities.get(0).getX());
			return gs;
		} catch (IOException|ClassNotFoundException ex){
			ex.printStackTrace();
			return null;
			//close()
		}
	}
	
	public void close(){
		try {
			if (this.socket != null) this.socket.close();
			if (this.reader != null) this.reader.close();
			if (this.writer != null) this.writer.close();
		} catch (IOException ex){
			ex.printStackTrace();
			System.exit(0);
		}
	}
}
