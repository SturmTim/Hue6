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
    private String inTagContent;

    private boolean hasError;

    public Tag(String fullContent) {
        hasError = true;

        if (fullContent.startsWith("<") && fullContent.endsWith(">")) {
            String[] splittedValue = fullContent.split(">");
            String firstTag = splittedValue[0] + ">";
            tag = firstTag.substring(1, firstTag.length() - 1);

            if (fullContent.endsWith("</" + tag + ">")) {
                hasError = false;
                inTagContent = fullContent.substring(firstTag.length(), fullContent.length() - (tag.length() + 3));
            }
        } else {
            hasError = false;
            inTagContent = fullContent;
        }
    }

    public String getContent() {

        if (!hasError) {
            if (inTagContent == null || inTagContent.equals("")) {
                return "\n" + tag + " hat keinen Inhalt";
            }
            return "\n" + inTagContent;
        } else {
            return "\n" + tag + " Fehler";
        }
    }

}
