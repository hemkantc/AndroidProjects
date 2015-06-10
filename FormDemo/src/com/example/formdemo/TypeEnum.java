package com.example.formdemo;

public enum TypeEnum {
	VARCHAR2("character",4000),
    NVARCHAR2("character",4000),
    LONG("character",0),
    CHAR("character",2000),
    NCHAR("character",2000),
    DATE("noncharacter",7),
    TIMESTAMP("noncharacter",13),
    NUMBER("noncharacter",21);
    
	private int maxsizeinbytes;
	private String istypeof;
	
	TypeEnum(String istypeof,int maxsizeinbytes){
		this.maxsizeinbytes = maxsizeinbytes;
		this.istypeof = istypeof;	
	}
    
	public String isTypeof() {
		return istypeof;
	}
	
	public int getMaxsizeinBytes() {
		return maxsizeinbytes;
	}
}
