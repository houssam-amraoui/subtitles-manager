package com.manager.subtitles.googleTr;

import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.util.StringUtils;
import com.manager.subtitles.vtt.VttRead;

import java.util.ArrayList;

public class GoogleRead {

    private enum CursorStatus {
        NONE,
        SIGNATURE,
        EMPTY_LINE,
        CUE_ID,
        CUE_TIMECODE,
        CUE_TEXT;
    }

    public static ArrayList<SubModel> parse(String filetext) throws Exception {
        // Create srt object
        ArrayList<SubModel> list = new ArrayList<SubModel>();

        String textLine = "";
        CursorStatus cursorStatus = CursorStatus.NONE;
        SubModel subModel = null;
        String subModelText = "";

        String[] ss = filetext.split("\n");

        for (int i= 0;i< ss.length;i++) {
            textLine= ss[i]+ "\n";

            textLine = textLine.trim();

            if (cursorStatus == CursorStatus.SIGNATURE || cursorStatus == CursorStatus.EMPTY_LINE) {
                if (textLine.isEmpty()) {
                    continue;
                }
                // New subModel
                subModel = new SubModel();
                cursorStatus = CursorStatus.CUE_ID;

                if (textLine.length() < 16 || !textLine.substring(13, 16).equals("-->")) {
                    // First textLine is the subModel number
                    subModel.setId(Integer.valueOf(textLine));
                    continue;
                }
            }

            if (cursorStatus == CursorStatus.CUE_ID) {
            // Enf of subModel
            }
            if (((cursorStatus == CursorStatus.CUE_TIMECODE || cursorStatus == CursorStatus.CUE_TEXT) && textLine.isEmpty())) {
                // End of subModel
                // Process multilines text in one time
                // A class or a style can be applied for more than one line
                subModel.lines.add(subModelText);
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
                    subModel.lines.add(subModelText);
                }
                continue;
            }
            throw new Exception(String.format("Unexpected line: %s", textLine));
        }
            return list;
        }
    }
