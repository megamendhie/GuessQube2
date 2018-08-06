package com.swiftqube.guessqube.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.swiftqube.guessqube.people.People;

public class DbProvider {
	
	private static final String _ID = "_id";
	private static final String NAME = "name";
	private static final String CATEGORY = "social_id";
	private static String Table, cat, faceName;
	private  static int length;
	People people = new People();

	static Context context;
	SQLiteDatabase database;
	DbHelper helper;
	
	public DbProvider(Context ctx){
		context = ctx;
		helper = new DbHelper(context);
		database = helper.openDataBase();
	}
	
	public void setTable(String table){
		Table = table;
	}
	
	public void setLength(String table){
		Cursor catCursor = database.query(table, new String[] {_ID}, CATEGORY + "= \""+cat +"\"", null, null, null, null);
		if(catCursor!=null){
			catCursor.moveToFirst();
			length = catCursor.getCount();
			catCursor.close();
		}
	}

	public  int getLength(){
		return length;
	}

	public int getTotal(String table){
		int i=0;
		Cursor cursor;
		if (table.equals("")){
			cursor= database.query(Table, new String[] {NAME}, null, null, null, null, null);
		}
		else{
			cursor= database.query(table, new String[] {NAME}, null, null, null, null, null);
		}
		if(cursor!=null){
			cursor.moveToFirst();
			i = cursor.getCount();
			cursor.close();
		}
		return i;
	}
	
	public String getName(int row, String table){
		Cursor cursor;
		String name = "", t = "";
		switch (people.getIndex()){
			case 3:
				cursor = database.query(table, new String[] {NAME, CATEGORY}, _ID +"="+row, null, null, null, null);
				t = table;
				break;
			default:
				cursor = database.query(Table, new String[] {NAME, CATEGORY}, _ID +"="+row, null, null, null, null);
				Log.i("Afo udoha idad", String.valueOf(row) + "jj: " + table);
				Log.i("Afo udoha idad", String.valueOf(cursor.getCount()));
				t = Table;
				break;
		}
		if(cursor!=null){
			cursor.moveToFirst();
			name = cursor.getString(0);
			cat = cursor.getString(1);
			cursor.close();
            setLength(t);
		}
		else{
			Log.i("Uyo", "Cursor of getName is null");
		}
		 return name;
	}

	public String getNames(String table, int row){
		Cursor catCursor;
		String name = "";
		switch (people.getIndex()){
			case 3:
				catCursor = database.query(table, new String[] {NAME}, CATEGORY + "= \""+cat +"\"", null, null, null, null);
				break;
			default:
				catCursor = database.query(Table, new String[] {NAME}, CATEGORY + "= \""+cat +"\"", null, null, null, null);
				break;
		}
		if(catCursor!=null){
			catCursor.moveToFirst();
			catCursor.moveToPosition(row-1);
			name = catCursor.getString(0);
			catCursor.close();
		}
		else{
			Log.i("Uyo", "Cursor of getName is null");
		}
		return name;
	}

	public int getID(int row){
		int ID = 0;
		Cursor catCursor = database.query(Table, new String[] {_ID, NAME}, CATEGORY + "= \""+cat +"\"", null, null, null, null);
		if(catCursor!=null){
			catCursor.moveToFirst();
			catCursor.moveToPosition(row-1);
			ID = catCursor.getInt(0);
			faceName = catCursor.getString(1);
			catCursor.close();
		}
		else{
			Log.i("Uyo", "Cursor of getName is null");
		}
		return ID;
	}

	public String getFaceName(){
		return  faceName;
	}
}
