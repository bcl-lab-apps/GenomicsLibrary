package com.example.smartsdk;
/**
 * @author Xi(Stephen) Chen
 */
import java.util.ArrayList;

import org.json.JSONObject;

public class smartPatient {
	private String name;
	private String address;
	private String gender;
	private String p_id;
	private String e_address;
	private String ttam_id;
	private smartObservation observations;
	private smartSequencingLab SequencingLab;
	private ArrayList<smartSequence> sequences;
	
	public smartPatient(){	
		sequences=new ArrayList<smartSequence>();
		this.ttam_id="";
		this.observations= new smartObservation();
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
	
	public void setTtamId(String id){
		this.ttam_id=id;
	}
	
	public void setAnObservation(smartObservation obs){
		this.observations=obs;
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
	
	public String getGender(){
		return this.gender;
	}
	
	public String getTtamId(){
		return this.ttam_id;
	}
	
	public String getEmail(){
		return this.e_address;
	}
	
	public smartObservation getAnObservation(){
		return this.observations;
	}
	
	public void setSequencingLab(smartSequencingLab lab){
		this.SequencingLab=lab;
	}
	
	public smartSequencingLab getSequencingLab(){
		return this.SequencingLab;
	}
	
	public void addSequence(smartSequence sequence){
		this.sequences.add(sequence);
	}
	
	public ArrayList<smartSequence> getSequenes(){
		return this.sequences;
	}
	
	
	

}
