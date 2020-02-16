package com.manager.subtitles;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.manager.subtitles.model.GoogleSubFile;
import com.manager.subtitles.model.GoogleSubModel;
import com.manager.subtitles.model.SubFile;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.sqlite.Sql;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImportActivity extends AppCompatActivity implements View.OnClickListener {
    public final String[] lang = new String[]{"EN", "AR", "FR"};
    private final String[] type = new String[]{".vtt", ".srt"};

    Spinner s1,s2;
    Button b1,b2,btnExport;
    TextView t1;
    ArrayAdapter<String> adapter;
    File[] subfile;
    File file;
    Sql db;
    CheckBox rm,cbfr,cbar,cben;
    private boolean caneAdd =false;
    TextView tpath1,tpathe2;
    RadioGroup radioGroup;
    LinearLayout l1,l2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importfile);
        s1 = findViewById(R.id.langSpinner);
        s2 =findViewById(R.id.typeSpinner);
        b1 = findViewById(R.id.btnAdd);
        b2 = findViewById(R.id.btnAddAll);
        t1 = findViewById(R.id.textView);
        rm = findViewById(R.id.rmcheck);

        btnExport = findViewById(R.id.btnExportAll);
        cbar = findViewById(R.id.cbar);
        cbfr = findViewById(R.id.cbfr);
        cben = findViewById(R.id.cben);

        radioGroup = findViewById(R.id.rgroup);
        l1= findViewById(R.id.L1);
        l2= findViewById(R.id.L2);

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

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        btnExport.setOnClickListener(this);

        db= Sql.Getnewinstans(this);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lang);
        s1.setAdapter(adapter);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,type);
        s2.setAdapter(adapter);
        caneAdd=  FileUtils.getRWpermition(this);
        //s1.getSelectedItemPosition()
    }

    @Override
    public void onClick(View v) {
        // remouve all parente path
        switch (v.getId()) {
            case R.id.btnAdd :
            Intent ii = FileUtils.ChooseDirectory();
            startActivityForResult(ii, 100);
            break;

            case R.id.btnAddAll :
                if (rm.isChecked())
                    db.DeletAll();

                if (caneAdd)
                addFileToDb();
                break;

            case R.id.btnExportAll :
                if (caneAdd){
                    ArrayList<Integer> in = new ArrayList<>();

                    if(cbar.isChecked()&&cbfr.isChecked()&&cben.isChecked()) {
                        return;
                    }
                    if(!(cbar.isChecked()||cbfr.isChecked()||cben.isChecked())) {
                        return;
                    }
                    if(cben.isChecked()) {
                        in.add(0);
                    }
                    if(cbar.isChecked()) {
                        in.add(1);
                    }
                    if(cbfr.isChecked()) {
                        in.add(2);
                    }
                    if (in.size()==2)
                        exportFileFromDb(in.get(0),in.get(1));
                    else
                        exportFileFromDb(in.get(0));

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100)
        if (resultCode== RESULT_OK){
            String ss = data.getData().getPath();
            t1.setText(FileUtils.getRealpathFromTree(ss));
            file = new File(FileUtils.getRealpathFromTree(ss));

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

    private void addFileToDb(){
        ArrayList<SubFile> files = new ArrayList<>();
        List<File> fileList = FileUtils.getAllFilesFromDerectory(file);
        fileList = FileUtils.filter(fileList,type[s2.getSelectedItemPosition()]);
        for (File filee : fileList){
            SubFile ff = new SubFile();
            ff.name = filee.getName();
            ff.path = filee.getPath().replace(file.getPath(),file.getName());
            String ss = FileUtils.load(filee.getPath());
            try {
                ff.subModels=Convert.VttTextToSubModel(ss,lang[s1.getSelectedItemPosition()]);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            files.add(ff);
        }
        db.addAllFile(files,false);
    }

    private void exportFileFromDb(int nlang) {
        ArrayList<SubFile> files = db.getAllFille(lang[nlang]);
        for(SubFile subFile: files) {
            String a= null;
            try {
                a = Convert.SubModelToVttText(subFile.subModels);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            FileUtils.saveFiles(subFile.path,a,false);
        }

    }

    private void exportFileFromDb(int nlang, int nlang2) {
        // file2 -> file
        ArrayList<SubFile> files = db.getAllFille(lang[nlang]);
        ArrayList<SubFile> files2 = db.getAllFille(lang[nlang2]);
        if (files.size() != files2.size())
            return;

        ArrayList<SubFile> subFiles =new ArrayList<>();

        for (int j=0;j<files.size();j++)
        {
            SubFile subFile = files.get(j);
            SubFile subFile2 = files2.get(j);
            for (int i=0;i<subFile.subModels.size();i++){
                SubModel model =subFile2.subModels.get(i);
                subFile.subModels.get(i).appendText(model.lines);
            }
            subFiles.add(subFile);
        }


        for(SubFile subFile: subFiles) {
            String a= null;
            try {
                a = Convert.SubModelToVttText(subFile.subModels);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            FileUtils.saveFiles(subFile.path,a,false);
        }

    }
}
