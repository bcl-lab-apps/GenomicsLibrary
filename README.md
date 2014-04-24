This is an implementation of client end functions for 23andme and SMART Genomics API for Android. Users can use this library to retrieve, store and display data from the APIs mentioned previously.

Initialize client:
	ttamClient client=new ttamClient("your client id","your client secret");
	client.authenticate(current activity, "redirect url",  "scopes");
	
Implemented methods: 
getProfile()-returns profile information such as names, profile id etc.
getRisks()-get user's and the general population's disease risks
getSNPGenotypes()-returns a list of user's 

Write to Database: 
	dB database=new dB();
	dB.insert(coloumn 1, column 2);
	Display information as a list view:
	listDisplay lv= new listDisplay(your array of data)
	lv.display();

See the 23andme and SMART API for approporiate specification for scopes, redirect url etc. 

All requests to server should be made in an Asyntask thread:
	class PostRequest extends AsyncTask<String,Void,String>{
		requests to API
	}
