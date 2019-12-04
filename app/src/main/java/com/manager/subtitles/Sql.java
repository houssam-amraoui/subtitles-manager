package com.manager.subtitles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.manager.subtitles.model.SubFile;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.model.SubTime;
import java.util.ArrayList;

public class Sql extends SQLiteOpenHelper {
    /*
    add comment
    add replay
    add like
    get comment
    get replay
    get like
     */
    private static Sql con = null;
    public static final String dbname= "subfile.db";
    // tabel
    public static final String File = "file";
    public static final String Subitem = "subitem";
    // tabel file
    public static final String Idfile = "idfile";
    public static final String Name = "name";
    public static final String Path = "path";
    // tabel subitem
    public static final String Idsubitem= "idsubitem"  ;
    public static final String Numsub= "numsub"  ;
    public static final String TimeStart= "timeStart"  ;
    public static final String TimeEnd= "timeEnd";
    public static final String Body= "body";
    public static final String Refidfile= Idfile;

    private Sql(Context context) {
        super(context, dbname, null, 1);
    }
    public static Sql Getnewinstans(Context context){
        if(con == null)
            con = new Sql(context);
        return con;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not Exists `file` (`idfile` INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(20) , `path` varchar ( 200 ));");
        db.execSQL("CREATE TABLE if not Exists `subitem` (`idsubitem` INTEGER PRIMARY KEY AUTOINCREMENT,`numsub` INTEGER , timeStart varchar(20) , timeEnd varchar(20) ,`body` Text,`idfile`int,FOREIGN KEY(`idfile`) REFERENCES `file`(`idfile`));");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public void DeletAll(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM imagel");
        database.execSQL("delete from replay");
        database.execSQL("DELETE FROM comment");
        database.execSQL("delete from userapp");
    }
    public void addAll(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("insert into userapp (nom,prenom,datene) values('houssam','amraoui','15/04/1999');\n" +
                "INSERT into comment(body,pdate,nblike,iduser) values ('hello comment','17/08/2019',1,1);\n" +
                "insert into replay values (1,'hello replay','17/08/2019',1,1);\n" +
                "insert into imagel values (1,'image.com',1);");
    }

    public void addFile(SubFile file){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Name,file.name);
        values.put(Path,file.path);
        database.insert(File,null,values);
        values.clear();
        addAllSubModel(file.subModels,getIdFileFromFile(file.name));
    }

    private void addAllSubModel(ArrayList<SubModel> subModels , int idfile){
        for (SubModel subModel:subModels) {
            addSubModel(subModel,idfile);
        }
    }

    private void addSubModel(SubModel subModel , int idfile) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Numsub,subModel.id);
        values.put(TimeStart,TimeWriter(subModel.timeStart));
        values.put(TimeEnd,TimeWriter(subModel.timeEnd));
        values.put(Body,subModel.getText());
        values.put(Refidfile,idfile);
        database.insert(File,null,values);
        values.clear();
    }
    private int getIdFileFromFile(String namefile){
        SQLiteDatabase database = getReadableDatabase();

        Cursor re = database.rawQuery("SELECT idfile FROM file where name='"+namefile+"' LIMIT 1",null);
        re.moveToFirst();
        return re.getInt(0);
    }

    public ArrayList<SubModel> getSubModel(String fileid){
        ArrayList<SubModel> subModels = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor re = database.rawQuery("SELECT * FROM subitem WHERE idfile ='"+fileid+"'",null);
        re.moveToFirst();
        while (!re.isAfterLast()){
            SubModel subModel=new SubModel();
            subModel.id= re.getInt(re.getColumnIndex(Idsubitem));
            subModel.timeStart= TimeReader(re.getString(re.getColumnIndex(TimeStart)));
            subModel.timeEnd=  TimeReader(re.getString(re.getColumnIndex(TimeStart)));
            subModel.setText(re.getString(re.getColumnIndex(Body)));
            subModels.add(subModel);
            re.moveToNext();
        }
        return subModels;
    }

    public int getTableCount(String tablename) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT COUNT (*) FROM "+tablename ,null);
        int count = 0;
        if(null != cursor)
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();
    db.close();
    return count;
    }

    private String TimeWriter(SubTime time) {
        return String.format("%02d:%02d:%02d.%03d", time.heurs, time.min, time.secend, time.mlsecend);
    }
    private SubTime TimeReader(String timeCodeString){
        int hour = Integer.parseInt(timeCodeString.substring(0, 2));
        int minute = Integer.parseInt(timeCodeString.substring(3, 5));
        int second = Integer.parseInt(timeCodeString.substring(6, 8));
        int millisecond = Integer.parseInt(timeCodeString.substring(9, 12));
        return new SubTime(hour, minute, second, millisecond);

    }
    public ArrayList<SubFile> getAllFille(){
        ArrayList<SubFile> subFiles = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor re = database.rawQuery("SELECT * FROM file",null);
        re.moveToFirst();

        while (!re.isAfterLast()){
            SubFile subFile=new SubFile();
            subFile.name= re.getString(re.getColumnIndex(Name));
            subFile.path= re.getString(re.getColumnIndex(Path));
            subFile.subModels= getSubModel(re.getString(re.getColumnIndex(Idfile)));
            subFiles.add(subFile);
            re.moveToNext();
        }
        return subFiles;
    }
}
