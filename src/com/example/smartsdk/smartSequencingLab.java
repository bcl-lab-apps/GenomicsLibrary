package com.example.smartsdk;
/**
 * @author Xi(Stephen) Chen
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.os.AsyncTask;
import android.os.PowerManager;

public class smartSequencingLab {
	private String sampleSource;
	private String sampleType;
	private String fileLocation;
	private String sequenceType;
	private String labName;
	private String organization;
	private String storedFileLocation;
	private String referenceGenome;
	
	public smartSequencingLab(){
		
	}
	
	public void setReferenceGenome(String genome){
		this.referenceGenome=genome;
	}
	
	public String getReferenceGenome(){
		return this.referenceGenome;
	}
	
	public void setSampleType(String type){
		this.sampleType=type;
	}
	
	public String getSampleType(){
		return this.sampleType;
	}
	
	public void setSampleSource(String source){
		this.sampleSource=source;
	}
	
	public String getSampleSource(){
		return this.sampleSource;
	}
	
	public void setFileLocation(String location){
		this.fileLocation=location;
	}
	
	public String getFileLocation(){
		return this.fileLocation;
	}
	
	public void downloadLabFile(String pathAndFileName, String urlLocation){
		new DownloadTask().execute(pathAndFileName,urlLocation);
	}
	
	public void downloadLabFile(String pathAndFileName){
		this.storedFileLocation=pathAndFileName;
		this.downloadLabFile(pathAndFileName,this.fileLocation);
	}
	
	public void setSequenceType(String type){
		this.sequenceType=type;
	}
	public String getStoredFileLocation(){
		return this.storedFileLocation;
	}
	                                      
	public String getSequenceType(){
		return this.sequenceType;
	}
	
	public void setLabName(String name){
		this.labName=name;
	}
	
	public String getLabName(){
		return this.labName;
	}
	
	public void setOrganization(String org){
		this.organization=org;
	}
	
	public String getOrganization(){
		return this.organization;
	}
	
	public void uploadLab(String filePath,String fileName, String fileFormat ){
		new uploadTask().execute(filePath,fileName,fileFormat);
	}
	
	private class DownloadTask extends AsyncTask<String, Integer, String> {

	    @Override
	    protected String doInBackground(String... params) {
	        InputStream input = null;
	        OutputStream output = null;
	        HttpURLConnection connection = null;
	        try {
	        	String path=params[0];
	            URL url = new URL(params[1]);
	            connection = (HttpURLConnection) url.openConnection();
	            connection.connect();

	            // expect HTTP 200 OK, so we don't mistakenly save error report
	            // instead of the file
	            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	                return "Server returned HTTP " + connection.getResponseCode()
	                        + " " + connection.getResponseMessage();
	            }

	            // this will be useful to display download percentage
	            // might be -1: server did not report the length
	            int fileLength = connection.getContentLength();

	            // download the file
	            input = connection.getInputStream();
	            output = new FileOutputStream(path);

	            byte data[] = new byte[4096];
	            long total = 0;
	            int count;
	            while ((count = input.read(data)) != -1) {
	                // allow canceling with back button
	                if (isCancelled()) {
	                    input.close();
	                    return null;
	                }
	                total += count;
	                
	                output.write(data, 0, count);
	            }
	        } catch (Exception e) {
	            return e.toString();
	        } finally {
	            try {
	                if (output != null)
	                    output.close();
	                if (input != null)
	                    input.close();
	            } catch (IOException ignored) {
	            }

	            if (connection != null)
	                connection.disconnect();
	        }
	        return null;
	    }
	}
	
	private class uploadTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create(); 
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {
				HttpPost httppost = new HttpPost(Global.serverUrl);
				builder.addPart("title", new StringBody(params[1], Charset.forName("UTF-8")));
				File myFile = new File(params[0]);
				FileBody fileBody = new FileBody(myFile, params[2]);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			
			return null;
		}
		
		
	}
}
