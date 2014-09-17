package com.example.smartsdk;
/**
 * @author Xi(Stephen) Chen
 */
public class smartObservation {
	private String trait;
	private String variantCode;
	private String variantComment;
	private String variantGenotype;
	private String variantInterpretation;
	
	public void setTraitAssessed(String trait){
		this.trait=trait;
	}
	
	public String getTraitAssesed(){
		return this.trait;
	}
	
	public void setVariantCode(String code){
		this.variantCode=code;
	}
	
	public String getVariantCode(){
		return this.variantCode;
	}
	
	public void setVariantComment(String comment){
		this.variantComment=comment;
	}
	
	public String getVariantaComment(){
		return this.variantComment;
	}
	
	public void setVariantGenotype(String genotype){
		this.variantGenotype=genotype;
	}
	
	public String getVariantGenotype(){
		return this.variantGenotype;
	}
	
	public void setVariantInterpretation(String interpretation){
		this.variantInterpretation=interpretation;
	}
	
	public String getVariantInterpretation(){
		return this.variantInterpretation;
	}
	
}
