package com.manager.subtitles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.vtt.VttParser;
import com.manager.subtitles.vtt.VttRead;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView tt;
    ArrayList<SubModel> re;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tt=findViewById(R.id.ttte);
        VttRead parser =new VttRead();
        String a="";
        a=FileManager.load("122en");
        //FileManager.save("subarUtf8",a);
        try {
            re=parser.parse(a);
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
    }

}
