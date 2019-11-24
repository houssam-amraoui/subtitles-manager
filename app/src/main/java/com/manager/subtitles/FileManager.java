package com.manager.subtitles;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

public class FileManager {

    public FileManager(){
    }
    public static void save(String filename,String text) {
        File myFile = new File("/sdcard/download/"+filename+".txt");
        try {
            if(!myFile.exists())
                myFile.createNewFile();
            } catch (IOException e) {
            }

        Date date=new Date(System.currentTimeMillis());
        String textdate= date.toLocaleString()+"  :"+text+"\n";
        FileOutputStream fos = null;

        try {
/*
        FileWriter fw = new FileWriter(myFile,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(textdate);
        bw.close();
*/
    FileOutputStream fOut = new FileOutputStream(myFile,true);
    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
    myOutWriter.write(textdate);
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
            File myFile = new File("/sdcard/download/"+filename+".srt");
            try {
                if(!myFile.exists())
                    myFile.createNewFile();
            } catch (IOException e) {
            }



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
                return sb.toString()+ " en :  "+isr.getEncoding();
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
    }


