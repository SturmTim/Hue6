/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htlgrieskirchen.po3.tsturm18;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author timst
 */
public class Tag {

    private String tag;
    private String inTagContent;

    private boolean hasError = true;

    public Tag(String fullContent) {

        if (fullContent.startsWith("<") && fullContent.endsWith(">")) {
            String[] splittedValue = fullContent.split(">");
            String firstTag = splittedValue[0] + ">";
            tag = firstTag.substring(1, firstTag.length() - 1);

            if (fullContent.endsWith("</" + tag + ">")) {
                inTagContent = fullContent.substring(firstTag.length(), fullContent.length() - (tag.length() + 3));
                hasError = false;
            }
        } else {
            hasError = false;
            inTagContent = fullContent;
        }
    }

    public String getContent() {
        if (!hasError) {
            if (inTagContent == null || inTagContent.equals("")) {
                return tag + " hat keinen Inhalt" + "\n";
            }
            return inTagContent + "\n";
        } else {
            return tag + " enthält einen Fehler" + "\n";
        }
    }

    public static List<TagString> getSplittedContent(String content) {

        List<TagString> splittedContent = new ArrayList<>();
        int prio = 1;

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {

            if (i + 1 < content.length() && content.charAt(i) == '<' && content.charAt(i + 1) != '/') {

                if (stringBuilder.toString().equals("") == false) {
                    splittedContent.add(new TagString(prio, stringBuilder.toString()));
                    prio++;
                    stringBuilder = new StringBuilder();
                }

                for (; content.charAt(i) != '>'; i++) {
                    stringBuilder.append(content.charAt(i));
                }
                stringBuilder.append(">");

                StringBuilder innerTagContent = new StringBuilder();

                int innerTags = 0;

                for (int j = i + 1; j < content.length(); j++) {

                    if (content.charAt(j) == '<' && content.charAt(j + 1) == '/') {
                        innerTags--;
                        if (innerTags < 0) {
                            stringBuilder.append(innerTagContent.toString());
                            for (; content.charAt(j) != '>'; j++) {
                                stringBuilder.append(content.charAt(j));
                            }
                            stringBuilder.append(">");

                            splittedContent.add(new TagString(prio, stringBuilder.toString()));
                            prio++;
                            stringBuilder = new StringBuilder();
                            i = j;
                            break;
                        } else {
                            innerTagContent.append(content.charAt(j));
                        }

                    } else if (content.charAt(j) == '<' && content.charAt(j + 1) != '/') {
                        innerTagContent.append(content.charAt(j));
                        innerTags++;
                    } else {

                        innerTagContent.append(content.charAt(j));
                    }
                }
                if (stringBuilder.toString().equals("") == false) {
                    splittedContent.add(new TagString(prio, stringBuilder.toString()));
                    prio++;
                    stringBuilder = new StringBuilder();
                }

            } else {
                stringBuilder.append(content.charAt(i));
            }

        }
        if (stringBuilder.toString().equals("") == false) {
            splittedContent.add(new TagString(prio, stringBuilder.toString()));
            prio++;
        }

        return splittedContent;
    }

}
