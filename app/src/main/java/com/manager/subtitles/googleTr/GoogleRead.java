package com.manager.subtitles.googleTr;

import android.text.Html;

import com.manager.subtitles.model.GoogleSubFile;
import com.manager.subtitles.model.GoogleSubModel;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.model.SubTime;
import com.manager.subtitles.util.StringUtils;
import com.manager.subtitles.vtt.VttRead;

import java.util.ArrayList;

public class GoogleRead {

    private enum CursorStatus {
        NONE,
        CUE_PATH,
        EMPTY_LINE,
        CUE_ID,
        CUE_TEXT;
    }

    public static ArrayList<GoogleSubFile> parse(String filetext) {

        ArrayList<GoogleSubFile> listSubFile = new ArrayList<>();

        String textLine = "";
        CursorStatus cursorStatus = CursorStatus.NONE;
        GoogleSubModel googleSubModel = null;
        GoogleSubFile googleSubFile = null;
        String subModelText = "";
        filetext = filetext.trim();

        String[] ss = filetext.split("\n");

        for (int i= 0;i< ss.length;i++) {
            textLine= ss[i]+ "\n";

            textLine = textLine.trim();
            if (cursorStatus == CursorStatus.CUE_PATH||cursorStatus==CursorStatus.NONE)
            if (isNumTextpath(textLine)) {
                if (cursorStatus==CursorStatus.NONE) {
                    googleSubFile = new GoogleSubFile();
                    googleSubModel = new GoogleSubModel();
                    googleSubFile.fileid = NumToText(textLine);
                }
                if (cursorStatus == CursorStatus.CUE_PATH) {
                    listSubFile.add(googleSubFile);
                    googleSubFile = new GoogleSubFile();
                    googleSubModel = new GoogleSubModel();
                    googleSubFile.fileid = NumToText(textLine);
                    continue;
                }
                cursorStatus = CursorStatus.CUE_PATH;
                continue;
            }else cursorStatus = CursorStatus.EMPTY_LINE;


            if (cursorStatus == CursorStatus.EMPTY_LINE) {
                if (textLine.isEmpty()) {
                    continue;
                }
                cursorStatus = CursorStatus.CUE_ID;
                if (textLine.length() < 5) {
                    // First textLine is the subModel number
                    googleSubModel.num=Integer.valueOf(textLine);
                    continue;
                }
            }
            if (((cursorStatus == CursorStatus.CUE_ID || cursorStatus == CursorStatus.CUE_TEXT) && textLine.isEmpty())) {
                googleSubModel.text=subModelText;
                googleSubFile.googleModels.add(googleSubModel);
                googleSubModel = new GoogleSubModel();
                subModelText = "";
                cursorStatus = CursorStatus.CUE_PATH;
                continue;
            }
            // Add new text to subModel
            if (cursorStatus == CursorStatus.CUE_ID || cursorStatus ==  CursorStatus.CUE_TEXT) {
                // New line
                if (!subModelText.isEmpty()) {
                    subModelText += "\n";
                }
                subModelText += textLine;
                cursorStatus = CursorStatus.CUE_TEXT;
                // la fin
                if(!textLine.isEmpty() && i==ss.length-1){
                    googleSubModel.text=subModelText;
                    googleSubFile.googleModels.add(googleSubModel);
                    listSubFile.add(googleSubFile);
                }
                continue;
            }
        }
        return listSubFile;
    }

    public static String NumToText(String num) {
        String ss[] = num.split(" ");
        if (ss.length == 1)
            return null;
        String text ="";
        for (int i= 0;i<ss.length;i++){
            text += ((char) Integer.parseInt(ss[i]));
        }
        return text;
    }
    public static boolean isNumTextpath(String num)
    {

        try {
            if (NumToText(num)==null)
                return false;
        }catch (Exception ex) {
            return false;
        }
        return true;
    }


}
