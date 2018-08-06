package com.swiftqube.guessqube.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbHelper extends SQLiteOpenHelper{

	private static final String db_path = "/data/data/com.swiftqube.guessqube/databases/";
	private static final String db_name = "synergy.db";
	private static  Context ctx;
	File dbFile;
	public static SQLiteDatabase SynergyDB;
	private static final int dbVersion = 2;
	SharedPreferences prefs;


	public DbHelper(Context context) {
		super(context, db_name, null, dbVersion);
		ctx = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		openDataBase();
	}

	public void createDataBase() throws IOException{
		if(checkDatabase()) {
			int version = prefs.getInt("VersionNumber", 1);
			if (version != dbVersion) {
				if(!dbFile.delete()){
					Log.i("Uyo", "Database cannot be deleted");
				}
			}
		}

		if(checkDatabase()) {
			Log.i("Uyo", "Data already exists");
			this.getWritableDatabase();
			}
		else{
			this.getReadableDatabase();
			try{
				copyDatabase();}
			catch(IOException e){
				throw new Error("Error copying data");
			}
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("VersionNumber", dbVersion).apply();
		}
	}


	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	private boolean checkDatabase(){
		dbFile = ctx.getDatabasePath(db_name);
		return  dbFile.exists();
	}

	private void copyDatabase()throws IOException{
		InputStream myInput = ctx.getAssets().open(db_name);

		String outFileName = db_path + db_name;

		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public SQLiteDatabase openDataBase(){

		//Open the database
		String myPath = db_path + db_name;
		if(SynergyDB == null){
			try {
				createDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			}
			SynergyDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		}
		return SynergyDB;
	}

	@Override
	public synchronized void close(){
		if(SynergyDB != null){
			SynergyDB.close();
		}
		super.close();
	}
}