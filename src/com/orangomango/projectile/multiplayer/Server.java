package com.orangomango.projectile.multiplayer;

import java.io.*;
import java.net.*;
import java.util.*;

import com.orangomango.projectile.ui.profile.Logger;

public class Server{
	private String host;
	private int port;
	private int players;
	private int gameMode;
	public static ServerSocket server;
	public static ArrayList<ClientManager> clients = new ArrayList<>();
	
	public Server(String host, int port, int players, int gameMode){
		try {
			this.host = host;
			this.port = port;
			this.players = players;
			this.gameMode = gameMode;
			server = new ServerSocket(port, 10, InetAddress.getByName(host));
			Logger.info("Server started at "+host+":"+port);
			listen();
		} catch (Exception ex){
			close();
		}
	}
	
	private void close(){
		try {
			if (this.server != null){
				this.server.close();
				Logger.info("Server closed using close()");
			}
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
	
	private void listen(){
		new Thread(() -> {
			while (!this.server.isClosed()){
				try {
					Socket client = this.server.accept();
					ClientManager cm = new ClientManager(client);
					clients.add(cm);
					new Thread(cm).start();
				} catch (IOException ex){
					close();
				}
			}
		}).start();
	}
}
