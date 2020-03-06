package com.manager.subtitles.vtt;

import android.text.Html;

import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.model.SubTime;
import com.manager.subtitles.util.StringUtils;

import java.util.ArrayList;

public class VttRead_No_id {

    private enum CursorStatus {
        NONE,
        SIGNATURE,
        EMPTY_LINE,
        CUE_ID,
        CUE_TIMECODE,
        CUE_TEXT;
    }

    public static ArrayList<SubModel> parse(String filetext,String lang) throws Exception {

        ArrayList<SubModel> list = new ArrayList<>();

        boolean isSubBeging= false;
        String textLine = "";
        CursorStatus cursorStatus = CursorStatus.NONE;
        SubModel subModel = null;
        String subModelText = "";
        filetext = filetext.trim();

        String[] ss = filetext.split("\n");

        for (int i= 0;i< ss.length;i++) {
            textLine= ss[i]+ "\n";

            textLine = textLine.trim();

            // Remove BOM
            if (cursorStatus == CursorStatus.NONE) {
                textLine = StringUtils.removeBOM(textLine);
            }

            if (cursorStatus == CursorStatus.NONE && textLine.equals("WEBVTT")) {
                cursorStatus = CursorStatus.SIGNATURE;
                continue;
            }

            if (cursorStatus == CursorStatus.SIGNATURE || cursorStatus == CursorStatus.EMPTY_LINE) {
                if (textLine.isEmpty()) {
                    isSubBeging=true;
                    continue;
                }else if(!isSubBeging) continue;

                subModel = new SubModel();
                cursorStatus = CursorStatus.CUE_ID;

                    // First textLine is the subModel number
                    subModel.setNum(list.size());

            }

            // 00:01:21.456 --> 00:01:23.417
            // 01:21.456 --> 01:23.417
            if (cursorStatus == CursorStatus.CUE_ID) {

                if (textLine.length() == 23 || textLine.substring(10, 13).equals("-->")) {
                    subModel.setTimeStart(parseTimeCode(textLine.substring(0, 9),false));
                    subModel.setTimeEnd(parseTimeCode(textLine.substring(14),false));
                    cursorStatus = CursorStatus.CUE_TIMECODE;
                    continue;
                }else
                if (textLine.length() == 29 || textLine.substring(13, 16).equals("-->")) {
                    subModel.setTimeStart(parseTimeCode(textLine.substring(0, 12),true));
                    subModel.setTimeEnd(parseTimeCode(textLine.substring(17),true));
                    cursorStatus = CursorStatus.CUE_TIMECODE;
                    continue;
                }else
                    throw new Exception(String.format("Timecode textLine is badly formated: %s", textLine));
            }
            if (cursorStatus == CursorStatus.CUE_TIMECODE && textLine.isEmpty()) {
            }

            if (((cursorStatus == CursorStatus.CUE_TIMECODE || cursorStatus == CursorStatus.CUE_TEXT) && textLine.isEmpty())) {
                subModelText = Html.fromHtml(subModelText).toString();
                subModel.lines=subModelText;

                subModel.lang=lang;
                list.add(subModel);
                subModel = null;
                subModelText = "";
                cursorStatus = CursorStatus.EMPTY_LINE;
                continue;
            }

            // Add new text to subModel
            if (cursorStatus == CursorStatus.CUE_TIMECODE || cursorStatus ==  CursorStatus.CUE_TEXT) {
                // New line
                if (!subModelText.isEmpty()) {
                    subModelText += "\n";
                }

                subModelText += textLine;

                cursorStatus = CursorStatus.CUE_TEXT;
                if(!textLine.isEmpty() && i==ss.length-1){
                    subModelText = Html.fromHtml(subModelText).toString();
                    subModel.lines=subModelText;
                    subModel.lang=lang;
                    list.add(subModel);
                    subModel = null;
                    subModelText = "";
                }
                continue;
            }
            throw new Exception(String.format("Unexpected line: %s", textLine));
        }
        return list;
    }
// 00:01:21.456 --> 00:01:23.417
// 01:21.456 --> 01:23.417
    private static SubTime parseTimeCode(String timeCodeString, boolean withHour) throws Exception {
        if (withHour){
        try {
            int hour = Integer.parseInt(timeCodeString.substring(0, 2));
            int minute = Integer.parseInt(timeCodeString.substring(3, 5));
            int second = Integer.parseInt(timeCodeString.substring(6, 8));
            int millisecond = Integer.parseInt(timeCodeString.substring(9, 12));
            return new SubTime(hour, minute, second, millisecond);
        } catch (NumberFormatException e) {
            throw new Exception(String.format(
                    "Unable to parse time code: %s", timeCodeString));
        }
    }else {
            try {
                int hour = Integer.parseInt("00");
                int minute = Integer.parseInt(timeCodeString.substring(0, 2));
                int second = Integer.parseInt(timeCodeString.substring(3, 5));
                int millisecond = Integer.parseInt(timeCodeString.substring(6, 9));
                return new SubTime(hour, minute, second, millisecond);
            } catch (NumberFormatException e) {
                throw new Exception(String.format(
                        "Unable to parse time code: %s", timeCodeString));
            }
        }
    }

}

