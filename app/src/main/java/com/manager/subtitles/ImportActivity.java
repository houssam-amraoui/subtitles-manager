package com.manager.subtitles;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.manager.subtitles.model.SubFile;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.sqlite.Sql;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ImportActivity extends AppCompatActivity implements View.OnClickListener {
    private final String[] lang = new String[]{"EN", "AR", "FR"};
    private final String[] type = new String[]{".vtt", ".srt"};

    Spinner s1,s2;
    Button b1,b2;
    TextView t1;
    ArrayAdapter<String> adapter;
    File[] subfile;
    File file;
    Sql db;
    CheckBox rm;
    private boolean caneAdd =false;

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

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

        db= Sql.Getnewinstans(this);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lang);
        s1.setAdapter(adapter);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,type);
        s2.setAdapter(adapter);

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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100)
        if (resultCode== RESULT_OK){
            String ss = data.getData().getPath();
            t1.setText(FileUtils.getRealpathFromTree(ss));
            file = new File(FileUtils.getRealpathFromTree(ss));
            getpermision();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    getpermision();
                } else {
                    Toast.makeText(this, "denaid", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    private void getpermision(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.mesage_permition))
                        .setIcon(R.drawable.ic_warning)
                        .setTitle(getResources().getString(R.string.mesage_permition_titel))
                        .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ImportActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                            }
                        }).setNegativeButton(getResources().getString(R.string.dinaid), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            }
        } else {
            caneAdd = true;
        }
    }
    private void addFileToDb(){
        ArrayList<SubFile> files = new ArrayList<>();
        List<File> fileList = FileUtils.getAllFilesFromDerectory(file);
        fileList = FileUtils.filter(fileList,type[s2.getSelectedItemPosition()]);
        for (File filee : fileList){
            SubFile ff = new SubFile();
            ff.name = filee.getName();
            ff.path= filee.getPath().replace(file.getPath(),file.getName());
            String ss = FileUtils.load(filee.getPath());
            try {
                ff.subModels=Convert.VttTextToSubModel(ss,lang[s1.getSelectedItemPosition()]);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            files.add(ff);
        }
        db.addAllFile(files);

    }






}
