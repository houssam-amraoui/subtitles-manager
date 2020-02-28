package com.manager.subtitles.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.manager.subtitles.model.GoogleSubFile;
import com.manager.subtitles.model.GoogleSubModel;
import com.manager.subtitles.model.SubFile;
import com.manager.subtitles.model.SubFileOnly;
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
    public static final String sublang="lang";
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
        db.execSQL("CREATE TABLE if not Exists `subitem` (`idsubitem` INTEGER PRIMARY KEY AUTOINCREMENT,`numsub` INTEGER , timeStart varchar(20) , timeEnd varchar(20) ,`body` Text ,`lang` varchar(20),`idfile`int,FOREIGN KEY(`idfile`) REFERENCES `file`(`idfile`));");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public void DeletAll(){
        SQLiteDatabase database = getWritableDatabase();
      //  database.execSQL("drop Table file");
     //   database.execSQL("drop Table subitem");
        database.execSQL("DELETE FROM file");
        database.execSQL("DELETE FROM subitem");
    }
   /* public void addAll(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("INSERT into  file (name,path) VALUES \n" +
                "(\"houssam\",\"c://sdfsdfdf\"),\n" +
                "(\"kchima\",\"c://sdfsddfdfdf\"),\n" +
                "(\"amraoui\",\"c://sdfssddfsfdf\");\n");
                database.execSQL("insert INTO subitem (numsub, timeStart,timeEnd,body,lang,idfile) VALUES\n" +
                "(1,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",1),\n" +
                "(2,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",1),\n" +
                "(3,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",1),\n" +
                "(4,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",1),\n" +
                "(1,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",2),\n" +
                "(2,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",2),\n" +
                "(3,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",2),\n" +
                "(4,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",2),\n" +
                "(5,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",2),\n" +
                "(1,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",3),\n" +
                "(2,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",3),\n" +
                "(3,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",3),\n" +
                "(4,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",3),\n" +
                "(5,\"00:00:00.000\",\"00:00:00.000\",\"holloknjdb sdifsidj\",\"EN\",3);" );
    }
*/
    public void addAllFile(ArrayList<SubFile> subFiles,boolean isFileExist){

        for (SubFile subFile:subFiles) {
            addFile(subFile, isFileExist);
        }
    }

    public void addFile(SubFile file,boolean isFileExist){
        if(!isFileExist) {
            SQLiteDatabase database = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Name, file.name);
            values.put(Path, file.path);
            database.insert(File, null, values);
            values.clear();
        }
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
        values.put(Numsub,subModel.num);
        values.put(sublang,subModel.lang);
        values.put(TimeStart,TimeWriter(subModel.timeStart));
        values.put(TimeEnd,TimeWriter(subModel.timeEnd));
        values.put(Body,subModel.getText());
        values.put(Refidfile,idfile);
        database.insert(Subitem,null,values);
        values.clear();
    }
    public int getIdFileFromFile(String namefile){
        SQLiteDatabase database = getReadableDatabase();
        Cursor re = database.rawQuery("SELECT idfile FROM file where name='"+namefile+"' LIMIT 1",null);
        re.moveToFirst();
        return re.getInt(re.getColumnIndex(Idfile));
    }

    public ArrayList<SubModel> getSubModel(String fileid ,String lang){
        ArrayList<SubModel> subModels = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor re = database.rawQuery("SELECT * FROM subitem WHERE idfile ='"+fileid+"' and lang ='"+lang+"'",null);
       // Cursor re = database.rawQuery("SELECT * FROM subitem",null);
        int rr = re.getCount();
        re.moveToFirst();
        while (!re.isAfterLast()){
            SubModel subModel=new SubModel();
            subModel.id= re.getInt(re.getColumnIndex(Idsubitem));
            subModel.num = re.getInt(re.getColumnIndex(Numsub));
            subModel.lang = re.getString(re.getColumnIndex(sublang));
            subModel.timeStart= TimeReader(re.getString(re.getColumnIndex(TimeStart)));
            subModel.timeEnd=  TimeReader(re.getString(re.getColumnIndex(TimeEnd)));
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

    public SubFile getFilleWhithPath(String path , String lang){
        SQLiteDatabase database = getReadableDatabase();
        Cursor re = database.rawQuery("SELECT * FROM file where path = '"+path+"' LIMIT 1",null);
        re.moveToFirst();
            SubFile subFile=new SubFile();
            subFile.name = re.getString(re.getColumnIndex(Name));
            subFile.path = re.getString(re.getColumnIndex(Path));
            subFile.subModels = getSubModel(re.getString(re.getColumnIndex(Idfile)),lang);
        return subFile;
    }

    public ArrayList<SubFile> getAllFille( String lang){
        ArrayList<SubFile> subFiles = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor re = database.rawQuery("SELECT * FROM file",null);
        re.moveToFirst();

        while (!re.isAfterLast()){
            SubFile subFile=new SubFile();
            subFile.name= re.getString(re.getColumnIndex(Name));
            subFile.path= re.getString(re.getColumnIndex(Path));
            subFile.subModels= getSubModel(re.getString(re.getColumnIndex(Idfile)),lang);
            subFiles.add(subFile);
            re.moveToNext();
        }
        return subFiles;
    }
    public ArrayList<SubFileOnly> getFilleEnly(){
        ArrayList<SubFileOnly> subFiles = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor re = database.rawQuery("SELECT * FROM file",null);
        re.moveToFirst();

        while (!re.isAfterLast()){
            SubFileOnly subFile=new SubFileOnly();
            subFile.iddb= re.getInt(re.getColumnIndex(Idfile))+"";
            subFile.name= re.getString(re.getColumnIndex(Name));
            subFile.path= re.getString(re.getColumnIndex(Path));
            subFiles.add(subFile);
            re.moveToNext();
        }
        return subFiles;
    }

    public void SubGoogleToDb(ArrayList<GoogleSubFile> txt, String fromlang, String tolang) {
        ArrayList<SubFile> subFiles =new ArrayList<>();
        for (GoogleSubFile sub : txt)
        {
            SubFile subFile = getFilleWhithPath(sub.filepath,fromlang);
            for (int i=0;i<sub.googleModels.size();i++){
            GoogleSubModel model =sub.googleModels.get(i);
            subFile.subModels.get(i).setText(model.text);
            subFile.subModels.get(i).lang=tolang;
            }
            subFiles.add(subFile);
        }
        addAllFile(subFiles,true);

    }
    public boolean isLangeExist(String lang){

        return false;
    }


}
