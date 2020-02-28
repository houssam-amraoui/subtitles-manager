package com.manager.subtitles.mp3;

import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.model.SubTime;

import java.util.ArrayList;

public class Mp3Write {

        public static String write(ArrayList<SubModel> subModelList) throws Exception {
            StringBuilder builder =new StringBuilder();
            try {

                for (int i= 0;i<subModelList.size();i++) {
                    SubModel sub=subModelList.get(i);

                    // Write Start time and end time
                    String startToEnd = String.format("%s", formatTimeCode(sub.timeStart));
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
            return String.format("[%02d:%02d.%02d]", time.min, time.secend, time.mlsecend);
        }
    }

