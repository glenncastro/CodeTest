package com.company.onlinestore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Basket {
	
	private static Logger logger = Logger.getLogger("com.company.onlinestore.Basket");
	
	private List<String> contents = new ArrayList<String>();
	
	public void add(String product) {
		contents.add(product);
	}
		
	public List<String> getContents() {
		return contents;
	}
	
	public void setContents(List<String> contents) {
		this.contents = contents;
	}
	
	//code that will send the notification to the client
	public void sendClientNotification() {
		logger.log(Level.WARNING, "Please enter correct Post Code");
	}
}
