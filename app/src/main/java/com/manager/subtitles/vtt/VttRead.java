package com.manager.subtitles.vtt;

import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.model.SubTime;
import com.manager.subtitles.model.SubtitleLine;
import com.manager.subtitles.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VttRead {

    private enum CursorStatus {
        NONE,
        SIGNATURE,
        EMPTY_LINE,
        CUE_ID,
        CUE_TIMECODE,
        CUE_TEXT;
    }

    private enum TagStatus {
        NONE,
        OPEN,
        CLOSE
    }

    public ArrayList<SubModel> parse(String filetext) throws Exception {
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

            // Remove BOM
            if (cursorStatus == CursorStatus.NONE) {
                textLine = StringUtils.removeBOM(textLine);
            }

            // All Vtt files start with WEBVTT
            if (cursorStatus == CursorStatus.NONE && textLine.equals("WEBVTT")) {
                cursorStatus = CursorStatus.SIGNATURE;
                continue;
            }

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
                // There is no subModel number
            }

            // Second textLine defines the start and end time codes
            // 00:01:21.456 --> 00:01:23.417
            // 01:21.456 --> 01:23.417
            if (cursorStatus == CursorStatus.CUE_ID) {

                if (textLine.length() == 23 || textLine.substring(10, 13).equals("-->")) {
                    subModel.setTimeStart(this.parseTimeCode(textLine.substring(0, 9),false));
                    subModel.setTimeEnd(this.parseTimeCode(textLine.substring(14),false));
                    cursorStatus = CursorStatus.CUE_TIMECODE;
                    continue;
                }else
                if (textLine.length() == 29 || textLine.substring(13, 16).equals("-->")) {
                    subModel.setTimeStart(this.parseTimeCode(textLine.substring(0, 12),true));
                    subModel.setTimeEnd(this.parseTimeCode(textLine.substring(17),true));
                    cursorStatus = CursorStatus.CUE_TIMECODE;
                    continue;
                }else
                    throw new Exception(String.format("Timecode textLine is badly formated: %s", textLine));

            }

            if (cursorStatus == CursorStatus.CUE_TIMECODE && textLine.isEmpty()) {
                // if line is empty
            }

            // Enf of subModel
            if (((cursorStatus == CursorStatus.CUE_TIMECODE || cursorStatus == CursorStatus.CUE_TEXT) && textLine.isEmpty()) || i==ss.length-1) {
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
                continue;
            }

            throw new Exception(String.format("Unexpected line: %s", textLine));
        }

        return list;
    }
/*
    private List<SubtitleLine> parseCueText(String cueText) {
        String text = "";
        List<String> tags = new ArrayList<>();
        List<SubtitleLine> cueLines = new ArrayList<>();
        VttLine cueLine = null; // Current cue line

        // Process:
        // - voice
        // - class
        // - styles
        for (int i=0; i<cueText.length(); i++) {
            String tag = null;
            TagStatus tagStatus = TagStatus.NONE;
            char c = cueText.charAt(i);

            if (c != '\n') {
                // Remove this newline from text
                text += c;
            }

            // Last characters (3 characters max)
            String textEnd = text.substring(Math.max(0, text.length()-3), text.length());

            if (textEnd.equals("<b>") || textEnd.equals("<u>") || textEnd.equals("<i>") ||
                    textEnd.equals("<v ") || textEnd.equals("<c.") || textEnd.equals("<c ")) {
                // Open tag
                tag = String.valueOf(textEnd.charAt(1));
                tagStatus = TagStatus.OPEN;

                // Add tag
                tags.add(tag);

                // Remove open tag from text
                text = text.substring(0, text.length()-3);
            } else if (c == '>') {
                // Close tag
                tagStatus = TagStatus.CLOSE;

                // Pop tag from tags
                tag = tags.remove(tags.size()-1);

                int closeTagLength = 1; // Size in chars of the close tag

                if (textEnd.charAt(0) == '/') {
                    // Real close tag: </u>, </c>, </b>, </i>
                    closeTagLength = 4;
                }

                // Remove close tag from text
                text = text.substring(0, text.length()-closeTagLength);
            } else if (c != '\n' && i < cueText.length()-1){
                continue;
            }

            if (c != '\n' && text.isEmpty()) {
                // No thing todo
                continue;
            }

            if (cueLine == null) {
                cueLine = new VttLine();
            }

            // Create text, apply styles and append to the cue line
            SubtitleStyle style = new SubtitleStyle();
            List<String> analyzedTags = new ArrayList<>();
            analyzedTags.addAll(tags);

            if (tagStatus == TagStatus.CLOSE) {
                // Apply style from last close tag
                analyzedTags.add(tag);
            } else if (tagStatus == TagStatus.OPEN) {
                analyzedTags.remove(tags.size() - 1);
            }

            for (String analyzedTag: analyzedTags) {
                if (analyzedTag.equals("v")) {
                    cueLine.setVoice(text);
                    text = "";
                    break;
                }

                // Bold characters
                if (analyzedTag.equals("b")) {
                    style.setProperty(SubtitleStyle.Property.FONT_WEIGHT, SubtitleStyle.FontWeight.BOLD);
                    continue;
                }

                // Italic characters
                if (analyzedTag.equals("i")) {
                    style.setProperty(SubtitleStyle.Property.FONT_STYLE, SubtitleStyle.FontStyle.ITALIC);
                    continue;
                }

                // Underline characters
                if (analyzedTag.equals("u")) {
                    style.setProperty(SubtitleStyle.Property.TEXT_DECORATION, SubtitleStyle.TextDecoration.UNDERLINE);
                    continue;
                }

                // Class apply to characters
                if (analyzedTag.equals("c")) {
                    // Cannot convert class
                    if (tagStatus == TagStatus.CLOSE && tag.equals("c") && !textEnd.equals("/c>")) {
                        // This is not a real close tag
                        // so push it again
                        text = "";
                        tags.add(tag);
                    }

                    continue;
                }
            }

            if (!text.isEmpty()) {
                if (style.hasProperties()) {
                    cueLine.addText(new SubtitleStyledText(text, style));
                } else {
                    cueLine.addText(new SubtitlePlainText(text));
                }
            }

            if (c == '\n' || i == (cueText.length()-1)) {
                // Line is finished
                cueLines.add(cueLine);
                cueLine = null;
            }

            text = "";
        }

        return cueLines;
    }
    */
// 123456789abc
// 00:01:21.456 --> 00:01:23.417
// 01:21.456 --> 01:23.417
    private SubTime parseTimeCode(String timeCodeString,boolean withHour) throws Exception {
        if (withHour){
        try {
            int hour = Integer.parseInt(timeCodeString.substring(0, 2));
            int minute = Integer.parseInt(timeCodeString.substring(3, 5));
            int second = Integer.parseInt(timeCodeString.substring(6, 8));
            int millisecond = Integer.parseInt(timeCodeString.substring(9, 13));
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
