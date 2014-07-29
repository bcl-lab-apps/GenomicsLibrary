package com.example.smartsdk;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.smartsdk.R;
import com.example.smartsdk.smartClient.PostRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ttamClient {
	private String clientId;
	private String clientSecret;
	private String redirectUrl;
	protected String accessToken;
	private String code;
	protected String profileId;
	View page;
	WebView gWebView;
	ProgressDialog pageDialog;
	final String TAG="SMART Lib";
	PostRequest request;
	
	public ttamClient(String id,String secret){
		accessToken="";
		this.clientId=id;
		this.clientSecret=secret;
	}
	//returns access token
	public String inflateView(Activity activity,String redirectUrl, String scope){
//		activity.setContentView(R.layout.web_overlay);
		final String rUrl=redirectUrl;
		LayoutInflater inflater = activity.getLayoutInflater();
		page=inflater.inflate(R.layout.web_overlay, null);
		activity.setContentView(page);
		//setContentView(R.layout.web_overlay);
		gWebView = (WebView) page.findViewById(R.id.webview);
		//pageDialog=ProgressDialog.show(activity.getApplication().getBaseContext(), "", "connecting to 23andme...");
		gWebView.loadUrl("https://api.23andme.com/authorize/?redirect_uri="
				+ redirectUrl + "&response_type=code&client_id=" + this.clientId
				+ "&scope=" + scope ); 
//gWebView.loadUrl("https://google.);
		Log.d(TAG, "http://192.241.244.189/auth/authorize?response_type=code&client_id=IV9AMqP9&scope=read:sequence read:patient");
		gWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(url.startsWith("http://192.241.244.189")){
					Log.d(TAG, "got to authentication page");
				}
				if (url.startsWith(rUrl)) {
					System.out.println("got to override");
					if (url.indexOf("code=") != -1) {
						//if the query contains code
						String queryString = null;
						try {
							queryString = new URL(url).getQuery();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println(queryString);
						String[] params = queryString.split("&");
						for (String param : params) {
							if (param.startsWith("code=")) {
								code = param.substring(param.indexOf('=') + 1);
							}
						}
						gWebView.setVisibility(View.GONE);
						PostRequest request= new PostRequest();
						request.execute(code);
						Log.d(TAG, "Post execute");
						request.cancel(true);
						Log.d(TAG, "cancelled");
						// don't go to redirectUri
					}
				}
			}
		});
		
		//while(true){
		//	if(!accessToken.equals("")){
				Log.d(TAG, "not empty");
		//		break;
		//	}
		//}
		return accessToken;
	}
	
	public String authenticate(Activity activity,String redirectUrl, String scope){
		String token="";
		Log.d(TAG, "redirect url: "+ redirectUrl);
		final String rUrl=redirectUrl;
		this.accessToken=token;
		final String id=this.clientId;
		final String secret= this.clientSecret;
		final String scopes=scope;
		Log.d(TAG, "ID & secret: "+ id+ " "+ secret);
		LayoutInflater inflater = activity.getLayoutInflater();
		page=inflater.inflate(R.layout.web_overlay, null);
		activity.setContentView(page);
		//activity.
		//setContentView(R.layout.web_overlay);
		gWebView = (WebView) page.findViewById(R.id.webview);
		//pageDialog=ProgressDialog.show(activity.getApplication().getBaseContext(), "", "connecting to 23andme...");
		gWebView.loadUrl("https://api.23andme.com/authorize/?redirect_uri="
						+ rUrl + "&response_type=code&client_id=" + this.clientId
						+ "&scope=" + scopes); 
		//gWebView.loadUrl("https://google.com");
		Log.d(TAG, "http://192.241.244.189/auth/authorize?response_type=code&client_id=IV9AMqP9&scope=read:sequence read:patient");
		request=new PostRequest();
		gWebView.setWebViewClient(new WebViewClient(){
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(url.startsWith("https://api.23andme")){
					Log.d(TAG, "load webpage");
					Log.d(TAG, "got to authentication page");
				}
				if (url.startsWith(rUrl)) {
					System.out.println("got to override");
					if (url.indexOf("code=") != -1) {
						//if the query contains code
						String queryString = null;
						try {
							queryString = new URL(url).getQuery();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println(queryString);
						String[] params = queryString.split("&");
						for (String param : params) {
							if (param.startsWith("code=")) {
								code = param.substring(param.indexOf('=') + 1);
							}
						}
						gWebView.setVisibility(View.GONE);
						request.execute(code);
						//Log.d(TAG, "access token: "+ accessToken);
						gWebView.destroy();
						// don't go to redirectUri
					}
				}
			}
		});
		while(true){
			if(accessToken!=""){
				Log.d(TAG, "not empty: "+ accessToken);
				request.cancel(true);
				break;							
			}
		}
		
		return accessToken;
		
		//Intent rIntent=new Intent(activity.getApplicationContext(),redirectActivity.getClass());
		//startActivity(rIntent);
		
	}
	
	class PostRequest extends AsyncTask<String,Void,String>{
		


		@Override
		protected String doInBackground(String... params) {
			String code=params[0];
			List<String> bearer_tokens= new ArrayList<String>();
			if (code != null) {
				System.out.println(code);				

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://api.23andme.com/token/");
				

				try {
					// data pairs
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair(
							"client_id", params[1]));
					nameValuePairs.add(new BasicNameValuePair(
							"client_secret", params[2]));
					nameValuePairs.add(new BasicNameValuePair(
							"grant_type", "authorization_code"));
					nameValuePairs.add(new BasicNameValuePair(
							"redirect_uri", params[3]));
					nameValuePairs.add(new BasicNameValuePair(
							"code", code));
					nameValuePairs.add(new BasicNameValuePair(
							"scope", params[4]));

					httppost.setEntity(new UrlEncodedFormEntity(
							nameValuePairs));

					// Execute HTTP Post Request
					if(httppost != null){
						
						HttpResponse response = httpclient
								.execute(httppost);
						
						String jsonString = EntityUtils
								.toString(response.getEntity());
						JSONObject jObject = new JSONObject(jsonString);
						String bearer_token = jObject
								.getString("access_token");
						if(bearer_token!=null){
							bearer_tokens.add(bearer_token);
						}
						Log.d(TAG, bearer_tokens.toString());
						HttpGet httpget = new HttpGet(
								"https://api.23andme.com/1/names/");
						httpget.setHeader("Authorization", "Bearer "
								+ bearer_token);
						HttpResponse getResponse = httpclient
								.execute(httpget);
						String nameString=  EntityUtils.toString(getResponse.getEntity());
						JSONObject nameObject = new JSONObject(nameString);
						String nameId = nameObject.getString("id");
						profileId=nameId;
						
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					System.out.println("CPE" + e);
				} catch(SocketException ex)
			       {
			         Log.e("Error : " , "Error on soapPrimitiveData() " + ex.getMessage());
			           ex.printStackTrace();
			           //return "error occured";
			       } catch (JSONException e) {
					e.printStackTrace();
					//return "error occured";
				} catch (IllegalStateException e) {
					e.printStackTrace();
					//return "error occured";
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//return "error occured";
				}
			}
			accessToken=bearer_tokens.get(0);
			return "";
			
		}	
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
		
	}
	
	public JSONObject getUserId(){
		Log.d(TAG, this.accessToken);
		HttpGet httpget = new HttpGet(
				"https://api.23andme.com/1/names/");
		httpget.setHeader("Authorization", "Bearer "
				+ accessToken);
		HttpClient httpclient = new DefaultHttpClient();
		JSONObject nameObject = null;
		try {
			HttpResponse getResponse = httpclient
					.execute(httpget);
			getResponse = httpclient
					.execute(httpget);
			String nameString=  EntityUtils.toString(getResponse.getEntity());
			nameObject = new JSONObject(nameString);
			Log.d(TAG, "profile info: "+ nameObject.toString());
			String nameId = nameObject.getString("id");
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nameObject;
	}
	public Hashtable<String, String>[] getRisks(){
		
		HttpClient httpclient = new DefaultHttpClient();
		Hashtable<String, String> individual_risk=new Hashtable<String, String>();
		Hashtable<String, String> population_risk=new Hashtable<String, String>();
		HttpGet riskGet= new HttpGet("https://api.23andme.com/1/risks/" + profileId + "/");
		riskGet.setHeader("Authorization", "Bearer "
				+ accessToken);
		HttpResponse riskResponse;
		Hashtable<String,String>[] risksReturnArray = (Hashtable<String,String>[])new Hashtable<?,?>[2];
		try {
			riskResponse = httpclient.execute(riskGet);
			JSONObject risks= new JSONObject(EntityUtils.toString(riskResponse.getEntity()));
			Log.d(TAG, risks.toString());
			JSONArray riskArray = new JSONArray(risks.getString("risks"));
			Log.d("risk array", riskArray.toString());
			ArrayList<JSONObject> riskList= new ArrayList<JSONObject>();
			for(int x=0;x<riskArray.length();x++){ 
				JSONObject risk=riskArray.getJSONObject(x);
				individual_risk.put(risk.getString("description"), risk.getString("risk"));
				population_risk.put(risk.getString("description"), risk.getString("population_risk"));
			}	
			risksReturnArray[0]=individual_risk;
			risksReturnArray[1]=population_risk;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Get the response
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return risksReturnArray;
	}
	
	public List<String> getSNPGenotypes(String... SNPs){
		String requestString="https://api.23andme.com/1/genotypes/"+profileId+"/?locations=";
		for(int i=0;i<SNPs.length;i++){
			if(i<SNPs.length-1){
				requestString=requestString+SNPs[i]+"%20";				
			}
			else{
				requestString=requestString+SNPs[i];
			}
		}
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(
				requestString);
		httpget.setHeader("Authorization", "Bearer "+accessToken);	
		List<String> SNPGenotypes=new ArrayList<String>();
		try {
			HttpResponse snpResponse = httpclient.execute(httpget);
			JSONObject genotypes= new JSONObject(EntityUtils.toString(snpResponse.getEntity()));
			Log.d(TAG, genotypes.toString());
			for(int i=0;i<SNPs.length;i++){
				SNPGenotypes.add((String) genotypes.get(SNPs[i]));
			}
			Log.d(TAG, "Genotypes "+ SNPGenotypes.toString());
			
			    
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SNPGenotypes;
	}
	
	
	
}

