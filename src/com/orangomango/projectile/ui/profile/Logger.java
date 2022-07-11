package com.orangomango.projectile.ui.profile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.orangomango.projectile.MainApplication;

public class Logger{
	private static File logFile;
	
	static {
		logFile  = new File(MainApplication.prefixPath+File.separator+"game_log.log");
	}
	
	private static void addLine(String line){
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date now = new Date();
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
			for (String lineP : line.split("\n")){
				writer.write(String.format("[%s] %s\n", formatter.format(now), lineP));
			}
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void info(String text){
		addLine("INFO: "+text);
	}
	
	public static void warning(String text){
		addLine("WARNING: "+text);
	}
	
	public static void error(String text){
		addLine("ERROR: "+text);
	}
}
