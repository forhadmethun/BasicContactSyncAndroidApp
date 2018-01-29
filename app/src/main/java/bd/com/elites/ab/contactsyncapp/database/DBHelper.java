package bd.com.elites.ab.contactsyncapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import bd.com.elites.ab.contactsyncapp.MainActivity;
import bd.com.elites.ab.contactsyncapp.utils.Message;

/**
 * Created by forhad on 13-08-2016.
 */
public class DBHelper  extends SQLiteOpenHelper {
    private static final String DB_NAME = "contactdatabase";
    private static String TABLE_NAME = "contacttable";
    private static final String _ID  = "_id";
    private static final String NAME  = "Name";
    private static final String PHONE  = "Phone";

    private Context context;

    private static final  String query = "CREATE TABLE "+TABLE_NAME+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255), "+PHONE  +" VARCHAR(255)) ;";
    private static final  String drop = "DROP TABLE IF EXISTS " +TABLE_NAME ;// ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255));";



    static int DB_VERSION = 1;
    public DBHelper(Context context) {
        super(context, DB_NAME,null , DB_VERSION);
        this.context = context;
        // Message.message(context,"constructor  called()");
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        try{

            db.execSQL(query);
            //  Message.message(context,"onCreate called()");
        }catch (SQLException e){
            Message.message(context,""+  e);
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(drop);
            onCreate(db);
            // Message.message(context, "onUpgrade() called ");
        }catch (SQLException e){
            Message.message(context,""+  e);
            e.printStackTrace();
        }


    }
    public  long insert(String name, String phone,SQLiteDatabase db)
    {
        db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(PHONE,phone);
        c.put(NAME,name);
        long id =  db.insert(TABLE_NAME,null,c);


        db.close();
        return id;
        //SQLiteDatabase db =
    }

    public ArrayList<String> getAppContactDetail() {

        //final String TABLE_NAME = "name of table";

        String selectQuery = "SELECT  Name FROM " + TABLE_NAME;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        String[] data      = null;
        ArrayList<String>  as = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                // get the data into array, or class variable
                String bookName = cursor.getString(cursor.getColumnIndex("Name"));

                as.add(bookName);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        //data = as.toArray(new String[as.size()]);

        return as;
    }

    public ArrayList<String> getAppPhoneDetail() {

        //final String TABLE_NAME = "name of table";

        String selectQuery = "SELECT  Phone FROM " + TABLE_NAME;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        String[] data      = null;
        ArrayList<String>  as = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                // get the data into array, or class variable
                String bookName = cursor.getString(cursor.getColumnIndex("Phone"));

                as.add(bookName);

            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        data = as.toArray(new String[as.size()]);

        return as;

    }

    public Cursor selectOneItem(String name, String phone) {
        Log.e("select","name: " + name + " --- phone: " + phone);


        if (name.contains("'")) {
            name = name.replaceAll("'", "''");
        }
        //String difficulty= DatabaseUtils.sqlEscapeString(name);
        String strQuery = String.format(
                "SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'",TABLE_NAME ,NAME,name, PHONE, phone);
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cur = db.rawQuery(strQuery , null);
        // Log.e("cur","" + (cur ? null : "null"));
        if(cur==null){
            Log.e("cur", "null retured for " + name + "-------" + phone);
        }
        else{
            Log.e("cur","->>>> Cursor count <<<<---"+cur.getCount());
        }
        db.close();
        return cur;
    }

    /*
    public void insertOrUpdate(String name, String phone)
    {
        String query = String.format(
                "SELECT * FROM %s WHERE %s = %s AND %s = %s",TABLE_NAME ,NAME,name, PHONE, phone);
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        Cursor cursor=db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            update(modelobj);
        }
        else
        {
            insert(name,phone,db);
        }
    }
    */
    public void update(String name, String phone,SQLiteDatabase db){
        db = this.getWritableDatabase();
        // ContentValues values = new ContentValues();
        ContentValues c = new ContentValues();
        c.put(PHONE,phone);
        c.put(NAME,name);
        /*
        values.put(Model_Task_List.KEY_username, student.getUserName());
        values.put(Model_Task_List.KEY_subject,student.getSubject());
        values.put(Model_Task_List.KEY_task, student.getTaskStatus());
        values.put(Model_Task_List.KEY_taskid,student.getTaskID());

        */
        if (name.contains("'")) {
            name = name.replaceAll("'", "''");
        }
        //Cursor cursor = selectOneItem(name,phone);
        //long KEY_taskid  = cursor.getLong(cursor.getColumnIndex("_id"));
        String query = "UPDATE "+TABLE_NAME+" SET Phone= '"+phone+"' ,Name= '"+name+"' WHERE Name = '"+name + "'";

        //db.update(Model_Task_List.KEY_table, values, Model_Task_List.KEY_taskid + "= ?", new String[]{String.valueOf(student.getTaskID())});

        db.execSQL(query);
        db.close();
        //Message.message(,"...Contact updated....");
        Log.e("update","Contact updated... inside database " + "name: " + name + "  phone: " + phone);

    }
    /*
    public void delete(String name, String phone, SQLiteDatabase db)
    {
        db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(PHONE,phone);
        c.put(NAME,name);
        if (name.contains("'")) {
            name = name.replaceAll("'", "''");
        }



    }
    */

    public boolean deleteTitle(String name,SQLiteDatabase db)
    {
        name = "'" +name +  "'";
        db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, NAME + "=" + name, null) > 0;
    }







}









