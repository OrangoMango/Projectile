package com.orangomango.projectile.multiplayer;

import java.io.*;
import java.net.*;

import com.orangomango.projectile.ui.profile.Logger;
import com.orangomango.projectile.Player;

public class ClientManager implements Runnable{
	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private String username;
	
	public ClientManager(Socket socket){
		this.socket = socket;
		try {
			this.writer = new ObjectOutputStream(this.socket.getOutputStream());
			this.reader = new ObjectInputStream(this.socket.getInputStream());
			this.username = (String)this.reader.readObject();
			Logger.info("Client "+this.username+" connected");
		} catch (IOException|ClassNotFoundException ex){
			close();
		}
	}
	
	@Override
	public void run(){
		new Thread(() -> {
			while (this.socket.isConnected()){
				try {
					GameState gs;
					Object o = this.reader.readObject();
					if (o instanceof GameState) {
						gs = (GameState)o;
					} else {
						Player g = (Player)o;
						Server.clients.get(0).writer.reset();
						Server.clients.get(0).writer.writeObject(g);
						continue;
					}
					//System.out.println("Got it");
					if (gs.getMessage() != null){
						if (gs.getMessage().equals("list")){
							this.writer.reset();
							this.writer.writeObject(Integer.valueOf(Server.clients.size()));
							continue;
						}
					}
					//System.out.println("Client receives gs: "+gs.entities.get(0).getX());
					for (ClientManager cm : Server.clients){
						if (cm != this){
							try {
								cm.writer.reset();
								cm.writer.writeObject(gs);
							} catch (IOException e){
								System.out.println("\n\n"+e.getMessage()+"\n\n");
							}
						}
					}
				} catch (IOException|ClassNotFoundException ex){
					//ex.printStackTrace();
					close();
					break;
				}
			}
		}).start();
	}
	
	private void close(){
		try {
			Server.clients.remove(this);
			if (this.socket != null) this.socket.close();
			if (this.reader != null) this.reader.close();
			if (this.writer != null) this.writer.close();
			Logger.info("Client "+this.username+" disconnected");
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
}
