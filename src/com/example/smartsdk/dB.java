package com.example.smartsdk;

/**
 * @author Xi(Stephen) Chen
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dB {
	private final String DB_NAME="patients.db";
	private final int DB_VERSION=1;
	private final String TABLE= "patients";
	private final String C_ID= "_id";
	private final String C_CREATED_AT="created_at";
	private final String C_NAME="name";
	private final String C_GENDER="gender";
	private final String C_PID="patient_id";
	private final String C_ADDRESS="address";
	private final String C_SCORE= "risk_score";
	private final String C_SEQUENCEFILELOCATION="sequence_file_locatioan";
	private final String C_TTAM_ID= "ttam_id";
	private final String C_EMAIL="email";
	private final String[] COLUMNS={C_ID,C_CREATED_AT,C_NAME,C_GENDER,C_PID,C_ADDRESS,C_SEQUENCEFILELOCATION, C_EMAIL};
	 //public final String C_AVERAGE= "average_risk";
			
	Context context;
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	public dB(Context context){
		this.context=context;
		dbHelper= new DbHelper();
	}
	
	public dB(Context context, smartPatient patient){
		this.context=context;
		
	}
	/**
	 * 
	 * 
	 * @param patient an instance of smartPatient
	 * 
	 */
	public void insert(smartPatient patient){
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(C_CREATED_AT, new Date().getTime() );
		values.put(C_NAME, patient.getUserName());
		values.put(C_GENDER, patient.getGender());
		values.put(C_ADDRESS, patient.getUserAddress());
		if(!patient.getSequencingLab().getStoredFileLocation().isEmpty()){
			values.put(C_SEQUENCEFILELOCATION, patient.getSequencingLab().getStoredFileLocation());
		}
		if(!patient.getTtamId().isEmpty()){
			values.put(C_TTAM_ID, patient.getTtamId());
		}
		values.put(C_EMAIL, patient.getEmail());
		db.insert(TABLE, null, values);
		db.close();
	}
	/**
	 * 
	 * @param patientID patient's SMART ID as search parameter
	 * @return an instance of smartPatient in the db
	 */
	public smartPatient getPatientByID(String patientID){
		smartPatient patient=new smartPatient();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = 
	            db.query(TABLE, // a. table
	            COLUMNS, // b. column names
	            " patient_id = ?", // c. selections 
	            new String[] { String.valueOf(patientID) }, // d. selections args
	            null, // e. group by
	            null, // f. having
	            null, // g. order by
	            null); // h. limit
	 
	    // 3. if we got results get the first one
	    if (cursor != null)
	        cursor.moveToFirst();
	    patient.setName(cursor.getString(2));
	    patient.setID(cursor.getString(3));
	    patient.setGender(cursor.getString(4));
	    patient.setAddress(cursor.getString(5));
	    patient.setEmail(cursor.getString(6));
	    patient.setTtamId(cursor.getString(7));
		return patient;
	}
	/**
	 * 
	 * @return a list of all patients storeds
	 */
	public List<smartPatient> getAllPatients(){
		db = dbHelper.getReadableDatabase();
		LinkedList<smartPatient> patients= new LinkedList<smartPatient>();
		Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE, null);
		
		if (cursor.moveToFirst()) {
	           do {
	               smartPatient patient=new smartPatient();
	               patient.setName(cursor.getString(2));
	       	       patient.setID(cursor.getString(3));
	       	       patient.setGender(cursor.getString(4));
	       	       patient.setAddress(cursor.getString(5));
	       	       patient.setEmail(cursor.getString(6));
	       	       patient.setTtamId(cursor.getString(7));
	       	       patients.add(patient);
	           } while (cursor.moveToNext());
	       }
		db.close();
		return patients;
	}
	/**
	 * 
	 * @param patient an instance of the patient you want to update
	 * @return integer indicating number of rows changed
	 */
	
	public int updatePatient(smartPatient patient){
		String id=patient.getUserId();
		db = dbHelper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(C_CREATED_AT, new Date().getTime() );
		values.put(C_NAME, patient.getUserName());
		values.put(C_GENDER, patient.getGender());
		values.put(C_ADDRESS, patient.getUserAddress());
		if(!patient.getSequencingLab().getStoredFileLocation().isEmpty()){
			values.put(C_SEQUENCEFILELOCATION, patient.getSequencingLab().getStoredFileLocation());
		}
		if(!patient.getTtamId().isEmpty()){
			values.put(C_TTAM_ID, patient.getTtamId());
		}
		int i = db.update(TABLE, //table
	            values, // column/value
	            C_PID+" = ?", // selections
	            new String[] { String.valueOf(patient.getUserId()) }); 
		db.close();
		
		return i;
	}
	
	class DbHelper extends SQLiteOpenHelper{

		public DbHelper() {
			super(context, DB_NAME, null, DB_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

	 
			String sql=String.format("create table %s " + 
					"(%s int primary key, %s int, %s text, %s text, %s text, %s text ,%S text,%s text,%s text)", TABLE, C_ID, C_CREATED_AT, C_NAME, C_PID,C_GENDER,C_ADDRESS,C_SEQUENCEFILELOCATION, C_TTAM_ID,C_EMAIL);
			Log.d("SQL DATABASE", sql);
			db.execSQL(sql);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop if exists " + TABLE);
			onCreate(db);
		}
		
	}

}