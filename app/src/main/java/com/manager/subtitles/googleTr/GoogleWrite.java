package com.manager.subtitles.googleTr;

import com.manager.subtitles.model.SubFile;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.model.SubTime;

import java.util.ArrayList;

public class GoogleWrite {

    public static String write(ArrayList<SubFile> subFileList) {
        StringBuilder file =new StringBuilder();
        for (SubFile subFile: subFileList) {

            StringBuilder builder =new StringBuilder();

            builder.append(TextToNum(subFile.path)+"\n\n");

            for (int i= 0;i<subFile.subModels.size();i++) {
                SubModel sub = subFile.subModels.get(i);
                if (sub.id != -1) {
                    builder.append(sub.num+"\n");
                }

                String text = String.format("%s\n\n", sub.getText());
                builder.append(text);

            }

                file.append(builder.toString());

        }
        return file.toString();
    }
    private static String TextToNum(String text) {
        String num ="";
        for (int i= 0;i<text.length();i++){
            num += ((int)text.charAt(i))+ " ";
        }
        return num;
    }

}
