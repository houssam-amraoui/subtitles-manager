package com.manager.subtitles;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.manager.subtitles.googleTr.GoogleRead;
import com.manager.subtitles.googleTr.GoogleWrite;
import com.manager.subtitles.model.GoogleSubFile;
import com.manager.subtitles.model.SubFile;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.sqlite.Sql;
import com.manager.subtitles.vtt.VttRead;

import java.util.ArrayList;

public class testActivity extends AppCompatActivity {
    Sql db;
    TextView tt;
    ArrayList<SubFile> subModels;
    ArrayList<GoogleSubFile> sss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testview);
        tt = findViewById(R.id.tex);
        db = Sql.Getnewinstans(this);

    //    subModels =db.getAllFille();

    //    String aa= GoogleWrite.write(subModels);
    //    FileUtils.saveFiles("subtest",aa,false);

       String go = FileUtils.load(Environment.getExternalStorageDirectory()+"/sub/subtesta.txt");
       sss= GoogleRead.parse(go);
       tt.setText(sss.toArray().toString());

       // tt.setText(aa);
    }
    /*  VttRead parser =new VttRead();
        String a="";
        a=FileUtils.load("/storage/emulated/0/Download/hola/salam/3. A Note On.vtt");
        //FileUtils.save("subarUtf8",a);
        try {
            subModels=VttRead.parse(a,"EN");


            tt.setText(get1(a));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get1(String text){
        String[] ss = text.split("\n");
        String result ="";
        for (int i= 0;i< ss.length;i++)
            result+=i+". "+ss[i]+ "\n";
        return result;
    }*/
}
