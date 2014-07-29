package com.example.smartsdk;

import java.util.ArrayList;

import org.json.JSONObject;

public class smartPatient {
	private String name;
	private String address;
	private String gender;
	private String p_id;
	private smartProdecure procedures;
	private String e_address;
	private smartObservation observations;
	private ArrayList<smartSequence> sequences;
	
	public smartPatient(){	
		sequences=new ArrayList<smartSequence>();
	}
	
	public void setName(String nm){
		this.name=nm;
	}
	
	public void setAddress(String adrs){
		this.address=adrs;
	}
	
	public void setGender(String gdr){
		this.gender=gdr;
	}
	
	public void setID(String id){
		this.p_id=id;
	}
	
	public void setEmail(String mail){
		this.e_address=mail;
	}
	
	public smartProdecure getProcedure(){
		return this.procedures;
	}
	
	
	public String getUserName(){		
		return this.name;
	}
	
	
	
	public String getUserId(){
		return this.p_id;	
	}
	
	public String getUserAddress(){
		return this.address;
	}
	
	public void addSequence(smartSequence sequence){
		this.sequences.add(sequence);
	}
	
	public ArrayList<smartSequence> getSequenes(){
		return this.sequences;
	}
	
	

}
