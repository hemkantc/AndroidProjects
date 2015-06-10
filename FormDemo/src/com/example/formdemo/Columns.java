package com.example.formdemo;

public class Columns {
	
	private String type;
	private int size;
	
	Columns(String type,int size) {
		this.type = type;
		this.size = size;
	}
	public String getType() {
		return type;
	}
	
	public int getSize() {
		return size;
	}
	
}
