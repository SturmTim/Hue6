/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htlgrieskirchen.po3.tsturm18;

/**
 *
 * @author timst
 */
public class Tag {

    private String tag;
    private String content;

    private boolean valid;

    public Tag(String value) {
        valid = false;

        if (value.startsWith("<") && value.endsWith(">")) {
            String[] splited = value.split(">");
            String firstTag = splited[0] + ">";
            tag = firstTag.substring(1, firstTag.length() - 1);

            if (value.endsWith("</" + tag + ">")) {
                valid = true;
                content = value.substring(firstTag.length(), value.length() - (tag.length() + 3));
            }

            if (value.equals(firstTag)) {
                valid = true;
            }
        } else {
            content = value;
        }
    }

    public String getContent() {
        if (valid) {
            if (content == null || content.equals("")) {
                return tag + ": hat keinen Inhalt";
            }
            return content;
        } else {
            return tag + " Fehler";
        }
    }

}
