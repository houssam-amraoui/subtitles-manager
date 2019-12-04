package com.manager.subtitles.googleTr;

import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.model.SubTime;

import java.util.ArrayList;

public class GoogleWrite {

    public static String write(ArrayList<SubModel> subModelList) throws Exception {
        StringBuilder builder =new StringBuilder();
        try {
            // Write cues
            for (int i= 0;i<subModelList.size();i++) {
                SubModel sub=subModelList.get(i);

                if (sub.id != -1) {
                    // Write number of subtitle //
                    builder.append(sub.id+"\n");
                }
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

}
