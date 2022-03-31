package com.kessi.quotey.textquotes.qdatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper_dbfile extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "quoteyDB.db";
    public final static String DB_PATH ="/data/data/com.kessi.quotey/databases/"; //Add your package name here
    private final Context myContext;

    // table name
    private static final String TABLE_QUOTES = "quotes_table";

    // Table Columns names
    private static final String FAVOURITE = "favourite";
    final String SELECT = "SELECT * FROM quotes_table";
        
    private ContentValues cValues;

    private SQLiteDatabase mDB;


    public DBHelper_dbfile(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.myContext = context;
    }

    //Create a empty database on the system
    public void createDatabase() throws IOException
    {
          boolean dbExist = checkDataBase();
          if(!dbExist)
          {
                this.getReadableDatabase();
                try
                {
                      this.close();    
                      copyDataBase();
                }
                catch (IOException e)
                {
                      throw new Error("Error copying database");
                }
          }
    }

    //Check database already exist or not
    private boolean checkDataBase()
    {
          boolean checkDB = false;
          try
          {
                String myPath = DB_PATH + DATABASE_NAME;
                File dbfile = new File(myPath);
                checkDB = dbfile.exists();
          }
          catch(SQLiteException e)
          {
          }
          return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException
    {
          String outFileName = DB_PATH + DATABASE_NAME;
          OutputStream myOutput = new FileOutputStream(outFileName);
          InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

          byte[] buffer = new byte[1024];
          int length;
          while ((length = myInput.read(buffer)) > 0)
          {
                myOutput.write(buffer, 0, length);
          }
          myInput.close();
          myOutput.flush();
          myOutput.close();
    }

    //delete database
    public void db_delete()
    {
          File file = new File(DB_PATH + DATABASE_NAME);
          if(file.exists())
          {
                file.delete();
                System.out.println("delete database file.");
          }
    }

    //Open database
    public void openDatabase() throws SQLException
    {
          String myPath = DB_PATH + DATABASE_NAME;
          mDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
          if(mDB != null)
        	  mDB.close();
          super.close();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    	  if (newVersion > oldVersion)
          {
                Log.v("Database Upgrade", "Database version higher than old.");
                db_delete();
          }
    }

    public Cursor getInfo(){
    	mDB = getReadableDatabase();
    	return mDB.rawQuery(SELECT, null);
    }
    
    public Cursor getFav(){
    	mDB = getReadableDatabase();
    	String FAV = "SELECT * FROM quotes_table WHERE favourite='yes'";
    	return mDB.rawQuery(FAV, null);
    	
    }
   

    public void addToFavourite(String fav,int id) {
    	 
    	mDB = getWritableDatabase();
 
        cValues = new ContentValues();
 
        cValues.put(FAVOURITE, fav);
        
//    Update data from database table
        mDB.update(TABLE_QUOTES, cValues,
                "id="+id+"", null);
 
       
    }

    public Cursor getCategories(){
        mDB = getReadableDatabase();
        String FAV = "SELECT * FROM quotes_categories";
        return mDB.rawQuery(FAV, null);
    }

    public Cursor getCategoryQuotes(String categoryName){
        mDB = getReadableDatabase();
        String total = "SELECT * FROM quotes_table WHERE category='"+categoryName+"'";
        return mDB.rawQuery(total, null);
    }

    public int getCategoryCount(String categoryName) {
        String countQuery = "SELECT * FROM quotes_table WHERE category='"+categoryName+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
