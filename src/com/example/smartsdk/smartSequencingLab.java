package com.example.smartsdk;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.PowerManager;

public class smartSequencingLab {
	private String sampleSource;
	private String sampleType;
	private String fileLocation;
	private String sequenceType;
	private String labName;
	private String organization;
	public smartSequencingLab(){
		
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
		this.downloadLabFile(pathAndFileName,this.fileLocation);
	}
	
	public void setSequenceType(String type){
		this.sequenceType=type;
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
	
	public void uploadLab(){
		
	}
	
	private class DownloadTask extends AsyncTask<String, Integer, String> {

	    private PowerManager.WakeLock mWakeLock;


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
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
