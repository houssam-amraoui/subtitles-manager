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
import com.manager.subtitles.vtt.VttRead;
import com.manager.subtitles.vtt.VttWrite;

import java.util.ArrayList;

public class Convert {


    public static ArrayList<SubModel> VttTextToSubModel(String txt,String lang) throws Exception {
        return VttRead.parse(txt,lang);
    }
     String SubModelToVttText(ArrayList<SubModel> txt) throws Exception {
        return VttWrite.write(txt);
    }
    

}





