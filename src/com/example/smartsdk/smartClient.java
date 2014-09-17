package com.example.smartsdk;
/**
 * @author Xi(Stephen) Chen
 */
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.DownloadManager.Request;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.smartsdk.ttamClient.PostRequest;

public class smartClient {
	private String clientId;
	private String clientSecret;
	private String redirectUrl;
	protected String accessToken="";
	private String code;
	protected String profileId;
	View page;
	WebView gWebView;
	ProgressDialog pageDialog;
	final String TAG="SMART Client";
	PostRequest request;
	final OnTokenComplete act;
	String baseurl;
/**
 * 
 * @param id	your client id
 * @param secret	your client secret
 * @param parentActivity	current activity
 * @param baseURL	URL of deployed SMART server
 */
	public smartClient(String id,String secret,OnTokenComplete parentActivity, String baseURL){
		this.clientId=id;
		this.clientSecret=secret;
		act=parentActivity;
		baseurl=baseURL;
		Global.serverUrl=baseURL;
	}
	//returns access token
	/**
	 * 
	 * @param activity 		Current activity you are on
	 * @param redirectUrl	redirect url that you set
	 * @param scope		Scopes of access
	 * 
	 */

	public void inflateView(Activity activity,String redirectUrl, String scope){
//		activity.setContentView(R.layout.web_overlay);
		final String rUrl=redirectUrl;
		
		LayoutInflater inflater = activity.getLayoutInflater();
		page=inflater.inflate(R.layout.web_overlay, null);
		final String scopes=scope;
		activity.setContentView(page);
		//setContentView(R.layout.web_overlay);
		gWebView = (WebView) page.findViewById(R.id.webview);
		//pageDialog=ProgressDialog.show(activity.getApplication().getBaseContext(), "", "connecting to 23andme...");
		gWebView.loadUrl("https://api.23andme.com/authorize/?redirect_uri="
				+ redirectUrl + "&response_type=code&client_id=" + this.clientId
				+ "&scope=" + scope );
		Log.d(TAG, "http://192.241.244.189/auth/authorize?response_type=code&client_id=IV9AMqP9&scope=read:sequence read:patient");
		
		gWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(url.startsWith("baseurl")){
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
						request= new PostRequest(act);
						request.execute(code, smartClient.this.clientId, smartClient.this.clientSecret, rUrl, scopes);
						Log.d(TAG, "Post execute");
						Log.d(TAG, "cancelled");
						
						
						// don't go to redirectUri
					}
				}
			}
		});
		
		
	}
	
	/**
	 * 	
	 * @return smartPatient patient patient class with varies data parameters
	 */
	public String getAllPatientsJSON(){
		String baseUrl="http://192.241.244.189/Patient/?_format=json";
		smartPatient user= new smartPatient();
		Log.d(TAG, "access token: "+ accessToken);
		HttpGet httpget = new HttpGet(
				baseUrl);
		HttpClient httpclient = new DefaultHttpClient();
		httpget.setHeader("Authorization", "Bearer "+ accessToken);
		String patientsString = "";
		try {
			HttpResponse getResponse = httpclient
					.execute(httpget);
			patientsString=  EntityUtils.toString(getResponse.getEntity());
			Log.d(TAG, "profile: "+ patientsString);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patientsString;
	}

/**
 * 
 * @param token access token 
 * @param id patient id number
 * @return patientString raw patient profile JSON in string format
 */
	public String getRawPatientJson(String token, String id){
		smartPatient patient= new smartPatient();
		String url= baseurl+"/Patient/"+ id;
		HttpGet httpget = new HttpGet(
				url);
		HttpClient httpclient = new DefaultHttpClient();
		httpget.setHeader("Authorization", "Bearer "+ token);
		String patientString = "";
		try {
			HttpResponse getResponse = httpclient
					.execute(httpget);
			patientString = EntityUtils.toString(getResponse.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patientString;
		
	}
	
/**
 * 
 * @param token 	access token
 * @param id 	patient id
 * @param patient	current smartPatient object
 * @return patient object with updated parameters
 */
	public smartPatient getPatient(String token, String id, smartPatient patient){
		String url= baseurl+"/Patient/"+ id;
		HttpGet httpget = new HttpGet(
				url);
		HttpClient httpclient = new DefaultHttpClient();
		httpget.setHeader("Authorization", "Bearer "+ token);
		try {
			HttpResponse getResponse = httpclient
					.execute(httpget);
			String patientString=  EntityUtils.toString(getResponse.getEntity());
			JSONObject profile= new JSONObject(patientString);
			JSONArray identifier= profile.getJSONArray("identifier");
			Log.d(TAG, "idenfifier array length: "+ identifier.length());	
			
			JSONArray names=profile.getJSONArray("name");
			String name=names.getJSONObject(0).getString("given")+names.getJSONObject(0).getString("family");
			Log.d(TAG, "name: "+ name);
			Log.d(TAG, "name array length: "+ names.length());
			JSONArray genders=profile.getJSONArray("gender");
			Log.d(TAG, "profile: "+ patientString);
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
		return patient;		
	} 
	/**
	 * 
	 * @param token access token
	 * @param id	patient's ID for SMART
	 * @param patient an instance of smartPatient
	 * @return instance of smartPatient with updated parameters
	 */
	public smartPatient getPatientTest(String token, String id, smartPatient patient){
		String url= baseurl+"/Patient/"+ id;
		HttpGet httpget = new HttpGet("https://fhir-open-api.smartplatforms.org/Patient/1032702"
				);
		HttpClient httpclient = new DefaultHttpClient();
		httpget.setHeader("Accept", "application/json");
		try {
			HttpResponse getResponse = httpclient
					.execute(httpget);
			String patientString=  EntityUtils.toString(getResponse.getEntity());
			JSONObject profile= new JSONObject(patientString);
			JSONArray identifier= profile.getJSONArray("identifier");
			Log.d(TAG, "idenfifier array length: "+ identifier.length());	
			String patientID=identifier.getJSONObject(0).getString("value");
			JSONArray names=profile.getJSONArray("name");
			String given=names.getJSONObject(0).getString("given");
			String family=names.getJSONObject(0).getString("family");
			Log.d(TAG, "parsed string: "+ JSONParser.parseBrackets(given));
			String name=JSONParser.parseBrackets(given)+JSONParser.parseBrackets(family);
			Log.d(TAG, "name: "+ name);
			Log.d(TAG, "name array length: "+ names.length());
			JSONObject genders=profile.getJSONObject("gender");
			Log.d(TAG, "profile: "+ patientString);
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
		return patient;		
	}
	/**
	 * 
	 * @param SNPs list of SNPs in chromosome coordinate format(chr1:3307-3308)
	 * @return Hashmap in format of <coordinate,genotype>
	 */
	
	public HashMap getSNPGenotypes(String token,String... SNPs){
		HashMap<String,String> genotypes= new HashMap<String, String>();
		String url=baseurl+"/sequence/";
		for(int i=0;i<SNPs.length;i++){
			String SNP=SNPs[i];
			HttpClient client= new DefaultHttpClient();
			
			
		}
		return genotypes;
	}
	/**
	 * 
	 * @param SNP	a single SNP coordinate, such as chr1:1223-1224
	 * @param token access token
	 * @param id patient id
	 * @param sequence an instance of smartSequence
	 * @return smartSequence with updated SNP genotypes, sample source, etc. Coordinate-genotype pairs can be obtained by calling smartSequence.getSequences()
	 */
	public smartSequence getSingleSNPGenotype(String SNP,String token,String id,smartSequence sequence){
		String genotype="";
		String url=baseurl+"/sequence/"+ "?patient=patient/"+id+"&coordinates="+ SNP+"&_format=json";
		HttpClient client= new DefaultHttpClient();
		HttpGet genotypeGet=new HttpGet(url);
		genotypeGet.setHeader("Authorization", "Bearer "+ token);		
		try {
			HttpResponse getResponse=client.execute(genotypeGet);
			getResponse.getEntity();
			JSONObject genotypeResponse=new JSONObject(EntityUtils.toString(getResponse.getEntity()));
			JSONArray coordinates=genotypeResponse.getJSONArray("coordinate");
			sequence.setReferenceGenome(coordinates.getJSONObject(0).getString("assembly"));
			String gtype=JSONParser.parseBrackets(genotypeResponse.getString("read")).replace(" ", "|");
			sequence.addSNP(SNP, gtype);
			sequence.setSequenceType(genotypeResponse.getString("type"));
			JSONArray sample= genotypeResponse.getJSONArray("sample");
			sequence.setSampleType(sample.getJSONObject(0).getString("type"));
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sequence;
	}
	
	private static boolean verifyLength(String SNP){
		boolean isSingle=true;
		if(Integer.valueOf(JSONParser.coordinateParse(SNP)[2])-Integer.valueOf(JSONParser.coordinateParse(SNP)[1])!=1){
			isSingle=false;
		}else{
			isSingle=true;
		}
		return isSingle;
	}
	/**
	public String getSingleSNPGenotypeTest(String SNP,String token,String id){
		String genotype="";
		String url=baseurl+"/sequence/"+ "?patient=patient/"+id+"&coordinates="+ SNP+"&_format=json";
		HttpClient client= new DefaultHttpClient();
		HttpGet genotypeGet=new HttpGet(url);
		genotypeGet.setHeader("Authorization", "Bearer "+ token);
		try {
			//HttpResponse getResponse=client.execute(genotypeGet);
			JSONObject genotypeResponse=new JSONObject("{\n  \"resourceType\": \"Sequence\",\n  \"text\": {\n    \"status\": \"generated\",\n    \"div\": \"<div>Example Sequece Resource showing patient John Doe\'s genotype of SNP rs421016</div>\"\n  },\n  \"patient\": {\n    \"reference\": \"Patient/example\"\n  },\n  \"type\": \"DNA\",\n  \"species\": {\n    \"coding\": [\n      {\n        \"system\": \"2.16.840.1.113883.6.96\",\n        \"code\": \"337915000\"\n      }\n    ],\n    \"text\": \"Homo sapiens\"\n  },\n  \"sample\": {\n    \"type\": \"germline\",\n    \"source\": {\n      \"coding\": [\n        {\n          \"system\": \"2.16.840.1.113883.6.96\",\n          \"code\": \"256897009\"\n        }\n      ],\n      \"text\": \"Saliva\"\n    }\n  },\n  \"coordinate\": {\n    \"assembly\": \"GRCh37\",\n    \"chromosome\": \"chr1\",\n    \"start\": 155205042,\n    \"end\": 155205043\n  },\n  \"baseQuality\": {\n    \"type\": \"overall\",\n    \"value\": \"39\"\n  },\n  \"mappingQuality\": 43,\n  \"read\": [\n    \"A\",\n    \"G\"\n  ]\n}");
			Log.d(TAG,"Genotypes: "+ JSONParser.parseBrackets(genotypeResponse.getString("read")));
			JSONArray coordinates=genotypeResponse.getJSONArray("coordinate");
			coordinates.getJSONObject(0).getString("assembly");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return genotype;
	}
	**/
	/**
	 * 
	 * @param id patient's profile ID
	 * @param token access token
	 * @param patient an instance of smartPatient;
	 * @return patient with a smartSequencingLabAttached
	 */
	
	public smartPatient getSequencingLab(String id, String token, smartPatient patient){
		String url=baseurl+"/Procedure?_profile=https://api.genomics.smartplatforms.org/profile/sequencinglab&subject=patient/"+id+"&_format=json";
		HttpClient client=new DefaultHttpClient();
		HttpGet labGet=new HttpGet(url);
		labGet.setHeader("Authorization", "Bearer "+ token);
		smartSequencingLab lab=new smartSequencingLab();
		try {
		
			HttpResponse response=client.execute(labGet);
			JSONObject seqLab=new JSONObject(EntityUtils.toString(response.getEntity()));
			lab.setLabName(seqLab.getString("name"));
			lab.setOrganization(seqLab.getString("organization"));
			lab.setReferenceGenome(seqLab.getString("assembly"));
			JSONArray specimen= seqLab.getJSONArray("specimen");
			lab.setSampleType(specimen.getJSONObject(0).getString("type"));
			lab.setSampleSource(specimen.getJSONArray(0).getJSONObject(0).getString("text"));
			lab.setFileLocation(seqLab.getString("file"));
			patient.setSequencingLab(lab);
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
		return patient;	
	}
	
	
	/**
	 * 
	 * @return the current client's access token
	 */
	public String getAccessToken(){
		return this.accessToken;		
	}
	/**
	 * 
	 * @param patient an instance of smartPatient
	 * @param token access token
	 * @return patient object with an observation attached
	 */
	
	public smartPatient getGeneticObservation(smartPatient patient, String token){
		smartObservation obs= new smartObservation();
		String URL=baseurl+"/Observation/"+ patient.getUserId()+"&_format=json";
		HttpClient client=new DefaultHttpClient();
		HttpGet obsGet=new HttpGet(URL);
		obsGet.setHeader("Authorization", "Bearer "+ token);
		try {
			HttpResponse response=client.execute(obsGet);
			JSONObject observation=new JSONObject(EntityUtils.toString(response.getEntity()));
			obs.setTraitAssessed(observation.getString("traitAssesed"));
			obs.setVariantCode(observation.getString("variant"));
			obs.setVariantGenotype(observation.getString("variantGenotype"));
			obs.setVariantInterpretation(observation.getString("variantInterpretation"));
			obs.setVariantComment(observation.getString("variantComment"));
			patient.setAnObservation(obs);
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
		return patient;
	}
	class PostRequest extends AsyncTask<String,Void,String>{


		public PostRequest(OnTokenComplete activity){
			
		}
		@Override
		protected String doInBackground(String... params) {
			//Log.d(TAG, "access token: "+ accessToken);
			List<String> bearer_tokens= new ArrayList<String>();
			if (code != null) {
				System.out.println(code);				
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						baseurl+"/token/");
				
				
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
						Log.d(TAG, "access token: "+ jsonString);
						String bearer_token = jObject
								.getString("access_token");
						if(bearer_token!=null){
							bearer_tokens.add(bearer_token);
						}
						Log.d(TAG, bearer_tokens.toString());
						/**
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
						 **/
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
			Log.d(TAG, "background thread, token: "+accessToken);
			

			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			act.OnTokenCompleted(accessToken);
		}
		
	}
	
	public String getServerUrl(){
		return this.baseurl;
	}
	
	
	
	
}
