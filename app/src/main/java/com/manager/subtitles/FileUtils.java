package com.manager.subtitles;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileUtils {

    public FileUtils(){
    }

    public static ArrayList<File> filter(List<File> ss,String type){
        ArrayList<File> fff = new ArrayList<>();
        for (File file : ss)
        {
         if (file.getPath().endsWith(type))
             fff.add(file);
        }
        return fff;
    }

    public static void saveFiles(String filename,String text , boolean isAppand) {
        File myFile = new File(Environment.getExternalStorageDirectory()+"/Sub/"+filename);
        try {
            if(!myFile.exists()){

                myFile.getParentFile().mkdirs();
                myFile.createNewFile();

            }
            } catch (IOException e) {
            e.printStackTrace();
            }

        FileOutputStream fos = null;

        try {
/*
        FileWriter fw = new FileWriter(myFile,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(textdate);
        bw.close();
*/
    FileOutputStream fOut = new FileOutputStream(myFile,isAppand);
    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
    myOutWriter.write(text);
    myOutWriter.close();
    fOut.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
            } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String load(String filename) {
        File myFile = new File(filename);
        try {
            if(!myFile.exists())
                myFile.createNewFile();
        } catch (IOException e) { }
        FileInputStream fis = null;

            try {

                fis = new FileInputStream(myFile);
                // InputStreamReader isr = new InputStreamReader(fis,"Windows-1256");
                InputStreamReader isr = new InputStreamReader(fis);

                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }
                return sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "nothin";
        }

    public static Intent ChooseDirectory(){
      Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
      intent.addCategory(Intent.CATEGORY_DEFAULT);
      return intent;
    }
    public static Intent ChooseFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }
    public static String getRealpathFromTree(String path){
        String phpath=Environment.getExternalStorageDirectory().getPath();
        if(path.contains("/tree/primary:"))
            path= phpath+"/"+path.replace("/tree/primary:","");
        else{
            String [] temp = path.split("/");
            temp = temp[2].split(":");
            String temp1 = temp[0] ;
            phpath = phpath.replace("emulated/0",temp1+"/");
            path = phpath + path.replace("/tree/"+temp1+":","");

        }
        return path;
    }
    public static String getRealpathForFile(String path){
        String phpath=Environment.getExternalStorageDirectory().getPath();
        if(path.contains("/document/primary:"))
            path= phpath+"/"+path.replace("/document/primary:","");
        else{
            String [] temp = path.split("/");
            temp = temp[2].split(":");
            String temp1 = temp[0] ;
            phpath = phpath.replace("emulated/0",temp1+"/");
            path = phpath + path.replace("/document/"+temp1+":","");

        }
        return path;
    }
    public static List<File> getAllFilesFromDerectory(File parent) {
        List<File> directories = new ArrayList<>();
        List<File> files = new ArrayList<>();
        //adding initial parent whose files we want to fetch
        directories.add(parent);
        while (directories.size() != 0) {
            File f = directories.remove(0);
            if (f.isDirectory()) {
                if (f.list().length > 0) {
                    //directory filter to filter out directories if any
                    List<File> directoryList = Arrays.asList(f.listFiles(directoryFilter));
                    //file filter to filter out files if any
                    List<File> fileList = Arrays.asList(f.listFiles(fileFilter));
                    //adding directories to directory list
                    directories.addAll(directoryList);
                    //adding files to file list
                    files.addAll(fileList);
                }
            }
        }
        return files;
    }
    public File findnamepath(File source){

        String name=source.getName();
        String downlodpath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        String path=downlodpath+"/"+name;
        File dest =new File(path);
        for (int i=0;dest.exists();i++){
            path=downlodpath+"/"+i+name;
            dest=new File(path);
        }
        return dest;
    }
    public int getIdexOf(File[] files,String name){
        Uri uri=Uri.parse(name);
        for(int i=0;i<files.length;i++){
            if(files[i].getPath().endsWith(uri.getLastPathSegment())){
                return i;
            }
        }
        return 0;
    }
    private static FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile();
        }
    };
    private static FileFilter directoryFilter = new FileFilter(){
        @Override
        public boolean accept(File pathname){ return pathname.isDirectory();
        }};

    public static boolean getRWpermition(final AppCompatActivity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(activity.getResources().getString(R.string.mesage_permition))
                        .setIcon(R.drawable.ic_warning)
                        .setTitle(activity.getResources().getString(R.string.mesage_permition_titel))
                        .setPositiveButton(activity.getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                            }
                        }).setNegativeButton(activity.getResources().getString(R.string.dinaid), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            }
        } else {
            return true;
        }
        return false;
    }



}


