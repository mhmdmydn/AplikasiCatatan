package del.app.dnote.database;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import java.util.ArrayList;
import del.app.dnote.model.NoteModel;
import android.database.Cursor;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DbHelper extends SQLiteOpenHelper {
	
	//database name
    public static final String DATABASE_NAME = "dbnotes";
	//table name
	public static final String TABLE_NAME = "tbl_notes";
    //database version
    public static final int DATABASE_VERSION = 1;
	
	public static final String COL_1 = "ID";
	public static final String COL_2 = "TITLE";
	public static final String COL_3 = "NOTES";
	public static final String COL_4 = "DATE";
    
	public DbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,NOTES TEXT,DATE INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);
	}
	
	//add the new note
    public void addNotes(String title, String des) {
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDate = new SimpleDateFormat("EEE, d MMM yyyy");
		String date = simpleDate.format(calendar.getTime());
		
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        values.put("Notes", des);
		values.put("Date", date);
        //inserting new row
        sqLiteDatabase.insert(TABLE_NAME, null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    //get the all notes
    public ArrayList<NoteModel> getNotes() {
        ArrayList<NoteModel> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setID(cursor.getString(0));
                noteModel.setTitle(cursor.getString(1));
                noteModel.setNotes(cursor.getString(2));
				noteModel.setDate(cursor.getString(3));
                noteModel.setExpand(false);
                arrayList.add(noteModel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

    //delete the note
    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(TABLE_NAME, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

    //update the note
    public void updateNote(String title, String des, String ID) {
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDate = new SimpleDateFormat("MMM dd, yyyy");
		String date = simpleDate.format(calendar.getTime());
		
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("Title", title);
        values.put("Notes", des);
		values.put("Date", date);
        //updating row
        sqLiteDatabase.update(TABLE_NAME, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }
    
}
