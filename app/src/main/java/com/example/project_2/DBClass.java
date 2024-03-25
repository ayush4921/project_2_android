package com.example.project_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DBClass extends SQLiteOpenHelper { //SQLiteOpenHelper is an in-built class of android. database. sqlite. SQLiteDatabase package. It is a helper class to manage SQLite database creation and version.
    private static final String ID_COL = "id";
    private String TABLE_NAME = "Info";
    // below variable is for our course name column
    private static final String NAME_COL = "name";

    // below variable id for our course duration column.
    private static final String AGE_COL = "age";

    // gender
    private static final String GENDER_COL = "gender";

    // username
    private static final String USERNAME_COL = "username";

    // password
    private static final String PASSWORD_COL = "password";

    public DBClass(Context context,String DATABASE_NAME){
        super(context,DATABASE_NAME,null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "   //uniquely identifies the table.
                + NAME_COL + " TEXT,"    //column name and type.
                + AGE_COL + " INTEGER,"
                + GENDER_COL + " TEXT,"
                + USERNAME_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)";

        db.execSQL(query);    //execute the SQL query. Cannot be used for SELECT/INSERT/UPDATE/DELETE.
    }

    /*  public void deletetable(String table_name){
          SQLiteDatabase db = this.getWritableDatabase();

          db.execSQL("DELETE * FROM "+table_name);
      }*/
    public void addInfo(String name, String age, String gender, String username, String password) {

        //This class is used to store a set of values that the ContentResolver (handles content provided to the app) can process.
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();  //writing into the database.
        values.put(ID_COL, 1);    //add column name and values into the ContentValues object.
        values.put(NAME_COL, name);
        values.put(AGE_COL, Integer.parseInt(age));
        values.put(GENDER_COL, gender);
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, password);
        db.insert(TABLE_NAME, null, values); //insert into the DB table.
        db.close();
    }

    public String selectQuery(String fieldname) {
        String fieldvalue;
        String query = "SELECT " + fieldname + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();   //selection is a read command.

        //Cursor provides random read-write access to the result set returned by a database query.
        Cursor extract = db.rawQuery(query, null);  //rawQuery is used to build SQL query. Used only for read queries.
        extract.moveToLast();  //move cursor to the last record.

        //i is column id. Here, it doesn't matter as we are only selecting the specific fieldname (input argument).
        return extract.getString(0);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    public byte[] messageDigest(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(s.getBytes(StandardCharsets.UTF_8));
    }
    public String hashPassword(String password) {
        String hashPwd = "";
        try {
            byte [] hashedPwd = messageDigest(password);
            hashPwd = Base64.encodeToString(hashedPwd, 0);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
        return hashPwd;
    }


    public boolean isValidLogin(String username, String password) {
        String hashedPassword = hashPassword(password);

        SQLiteDatabase db = this.getReadableDatabase();
        // Then, use the hashed password in the query instead of the plain text password
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE username=? AND password=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, hashedPassword});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}

