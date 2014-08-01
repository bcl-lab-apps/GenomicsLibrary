package com.example.smartsdk;

import java.util.HashMap;

public class smartSequence {
	private HashMap<String,String> sequences;
	private String sampleSource;
	private String sequenceType;
	private String referenceGenome;
	private String sampleType;
	public smartSequence(){
		sequences=new HashMap<String,String>();
	}
	public void addSNP(String coordnate, String genotype){
		sequences.put(coordnate, genotype);
	}
	
	public HashMap<String,String> getSequences(){
		return this.sequences;
	}
	
	public void setReferenceGenome(String genome){
		this.referenceGenome=genome;
	}
	
	public String getReferenceGenome(){
		return this.referenceGenome;
	}
	public void setSampleSource(String source){
		sampleSource=source;
	}
	public String getSampleSource(){
		return this.sampleSource;
	}
	
	public void setSequenceType(String type){
		
	}
	public String getSequenceType(){
		return this.sampleSource;
	}
	
	public void setSampleType(String type){
		this.sampleType=type;
	}
	/**
	 * 
	 * @return germline/somati/prenatal
	 */
	public String getSampleType(){
		return this.sampleType;
	}
	
	
}
