package com.manager.subtitles.googleTr;

import com.manager.subtitles.model.SubFile;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.model.SubTime;

import java.util.ArrayList;

public class GoogleWrite {

    public static ArrayList<String> write(ArrayList<SubFile> subFileList , int nbline) {
        ArrayList<String> sss= new ArrayList<>();
        int nbadd =0;
        StringBuilder file =new StringBuilder();
        for (int ii =0; ii<subFileList.size();ii++) {
            SubFile subFile = subFileList.get(ii);
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
            nbadd += subFile.subModels.size();
            if(nbadd<nbline)
                file.append(builder.toString());
            else
            {
                sss.add(file.toString());
                file = new StringBuilder();
                file.append(builder.toString());
                nbadd = subFile.subModels.size();
            }
        }
        if (file.length()>1)
            sss.add(file.toString());

        return sss;
    }
    private static String TextToNum(String text) {
        String num ="";
        for (int i= 0;i<text.length();i++){
            num += ((int)text.charAt(i))+ " ";
        }
        return num;
    }

}
