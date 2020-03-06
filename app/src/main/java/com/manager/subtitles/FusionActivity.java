package com.manager.subtitles;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.manager.subtitles.googleTr.GoogleRead;
import com.manager.subtitles.googleTr.GoogleWrite;
import com.manager.subtitles.model.GoogleSubFile;
import com.manager.subtitles.model.SubFile;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.sqlite.Sql;

import java.io.File;
import java.util.ArrayList;

public class FusionActivity  extends AppCompatActivity implements View.OnClickListener {
    TextView tpath1,tpathe2;
    Spinner s1,s2,s3;
    RadioGroup radioGroup;
    LinearLayout l1,l2;
    Sql db;
    ArrayAdapter<String> adapter;
    Boolean caneAdd =false;
    Button pathim,pathex,btnimport,btnexport;
    TextView t1,t2,t3;
    File file,fileex;

    public final String[] lang = new String[]{"EN", "AR", "FR"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fusionsub);
        db = Sql.Getnewinstans(this);

        radioGroup = findViewById(R.id.rgroup);
        l1= findViewById(R.id.L1);
        l2= findViewById(R.id.L2);
        s1 = findViewById(R.id.langSpinner1);
        s2 =findViewById(R.id.langSpinner2);
        s3 =findViewById(R.id.langSpinner);
        pathim = findViewById(R.id.pathim);
        pathex = findViewById(R.id.pathex);
        btnimport = findViewById(R.id.btnimport);
        btnexport = findViewById(R.id.btnexport);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        pathex.setOnClickListener(this);
        pathim.setOnClickListener(this);
        btnexport.setOnClickListener(this);
        btnimport.setOnClickListener(this);


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lang);
        s1.setAdapter(adapter);
        s2.setAdapter(adapter);
        s3.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.im:
                        l1.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.GONE);
                        break;
                    case R.id.ex:
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        caneAdd=  FileUtils.getRWpermition(this);
    }
    private void exportfileTr(){
        ArrayList<SubFile> subFiles =db.getAllFille(lang[s3.getSelectedItemPosition()]);
        String a =subFiles.get(0).path.split("/")[0];
        ArrayList<String> aa= GoogleWrite.write(subFiles,700);
        for (int i= 0; i<aa.size();i++) {
            FileUtils.saveFiles(a+"/sub"+i+".txt", aa.get(i), false);
        }
    }
    private void importfileTr(String filepath){
        String a= FileUtils.load(filepath);
        ArrayList<GoogleSubFile> googleSubFiles= GoogleRead.parse(a);
        db.SubGoogleToDb(googleSubFiles,lang[s1.getSelectedItemPosition()],lang[s2.getSelectedItemPosition()]);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100)
            if (resultCode== RESULT_OK){
                String ss = data.getData().getPath();
                t1.setText(FileUtils.getRealpathForFile(ss));
                file = new File(FileUtils.getRealpathForFile(ss));

            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pathim :
                Intent ii = FileUtils.ChooseFile();
                startActivityForResult(ii, 100);
                break;

            case R.id.btnexport :
                if (caneAdd)
                    exportfileTr();
                break;

            case R.id.btnimport :
                if (caneAdd)
                    importfileTr(file.getPath());
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    FileUtils.getRWpermition(this);
                } else {
                    Toast.makeText(this, "denaid", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
