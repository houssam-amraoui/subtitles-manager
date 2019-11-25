package com.manager.subtitles.vtt;

import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.model.SubTime;
import com.manager.subtitles.model.SubtitleCue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class VttWrite {

    public static String write(ArrayList<SubModel> subModelList) throws Exception {
        StringBuilder builder =new StringBuilder();
        try {
            // Write header
            builder.append("WEBVTT\n\n");

            // Write cues


            for (int i= 0;i<subModelList.size();i++) {
                SubModel sub=subModelList.get(i);

                if (sub.id != -1) {
                    // Write number of subtitle
                    builder.append(sub.id+"\n");
                }

                // Write Start time and end time
                String startToEnd = String.format("%s --> %s \n",
                        formatTimeCode(sub.timeStart),
                        formatTimeCode(sub.timeEnd));
                builder.append(startToEnd);
                // Write text
                String lastLine= "\n";
                if (i==subModelList.size()-1)
                    lastLine="";

                String text = String.format("%s"+lastLine, sub.getText());
                builder.append(text);
                // Write empty line
                builder.append(lastLine);
            }

        } catch (Exception e) {
            throw new Exception("Encoding error in input subtitle");
        }
        return builder.toString();
    }

    private static String formatTimeCode(SubTime time) {
        return String.format("%02d:%02d:%02d.%03d", time.heurs, time.min, time.secend, time.mlsecend);
    }

}
