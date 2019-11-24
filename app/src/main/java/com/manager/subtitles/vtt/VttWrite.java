package com.manager.subtitles.vtt;

public class VttWrite {

    private String charset;

/*
    public void write(SubtitleObject subtitleObject, OutputStream os)  {
        try {
            // Write header
            os.write(new String("WEBVTT\n\n").getBytes(this.charset));

            // Write cues
            for (SubtitleCue cue : subtitleObject.getCues()) {
                if (cue.getId() != null) {
                    // Write number of subtitle
                    String number = String.format("%s\n", cue.getId());
                    os.write(number.getBytes(this.charset));
                }

                // Write Start time and end time
                String startToEnd = String.format("%s --> %s \n",
                        this.formatTimeCode(cue.getStartTime()),
                        this.formatTimeCode(cue.getEndTime()));
                os.write(startToEnd.getBytes(this.charset));

                // Write text
                String text = String.format("%s\n", cue.getText());
                os.write(text.getBytes(this.charset));

                // Write empty line
                os.write("\n".getBytes(this.charset));
            }
        } catch (UnsupportedEncodingException e) {
            throw new IOException("Encoding error in input subtitle");
        }
    }

    private String formatTimeCode(SubtitleTimeCode timeCode) {
        return String.format("%02d:%02d:%02d.%03d",
                timeCode.getHour(),
                timeCode.getMinute(),
                timeCode.getSecond(),
                timeCode.getMillisecond());
    }*/

}
