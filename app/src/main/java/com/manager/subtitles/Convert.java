/*
 *  This file is part of the noOp organization .
 *
 *  (c) Cyrille Lebeaupin <clebeaupin@noop.fr>
 *
 *  For the full copyright and license information, please view the LICENSE
 *  file that was distributed with this source code.
 *
 */

package com.manager.subtitles;


import com.manager.subtitles.model.GoogleSubModel;
import com.manager.subtitles.model.SubFile;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.mp3.Mp3Read;
import com.manager.subtitles.mp3.Mp3Write;
import com.manager.subtitles.vtt.VttRead;
import com.manager.subtitles.vtt.VttRead_No_id;
import com.manager.subtitles.vtt.VttWrite;

import java.util.ArrayList;

public class Convert {


    public static ArrayList<SubModel> VttTextToSubModel(String txt,String lang) throws Exception {
        return VttRead.parse(txt,lang);
    }
    public static ArrayList<SubModel> Mp3TextToSubModel(String txt,String lang) throws Exception {
        return Mp3Read.parse(txt,lang);
    }
    public static ArrayList<SubModel> Vtt_no_id_TextToSubModel(String txt,String lang) throws Exception {
        return VttRead_No_id.parse(txt,lang);
    }
    public static String SubModelToVttText(ArrayList<SubModel> txt) throws Exception {
        return VttWrite.write(txt);
    }
    public static String SubModelToMp3Text(ArrayList<SubModel> txt) throws Exception {
        return Mp3Write.write(txt);
    }
    

}





