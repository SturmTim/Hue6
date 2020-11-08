/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htlgrieskirchen.po3.tsturm18;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author timst
 */
public class HTMLCallable implements Callable<TagString> {

    private final int priority;
    private final Tag tag;

    public HTMLCallable(int priority, Tag tag) {
        this.priority = priority;
        this.tag = tag;
    }

    @Override
    public TagString call() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();

        List<TagString> splittetContent = getSplittedContent(tag.getContent());

        List<HTMLCallable> callableList = new ArrayList<>();
        for (int i = 0; i < splittetContent.size(); i++) {
            if (splittetContent.get(i).getFullString().startsWith("<")) {
                callableList.add(new HTMLCallable(splittetContent.get(i).getTagPriority(), new Tag(splittetContent.get(i).getFullString())));
            }
        }

        List<Future<TagString>> results = executor.invokeAll(callableList);
        results.stream()
                .map(result -> {
                    try {
                        return result.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).forEach(result -> {
            splittetContent.set(splittetContent.indexOf(result), result);
        });

        executor.shutdown();

        StringBuilder ergebniss = new StringBuilder();
        splittetContent.forEach(prioritisedString -> ergebniss.append(prioritisedString.getFullString()));
        TagString tagString = new TagString(priority, ergebniss.toString());
        return tagString;
    }

    public static List<TagString> getSplittedContent(String content) {

        List<TagString> splittedContent = new ArrayList<>();
        int prio = 1;

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {

            if (i + 1 < content.length() && content.charAt(i) == '<' && content.charAt(i + 1) != '/') {

                if (!stringBuilder.toString().equals("")) {
                    splittedContent.add(new TagString(prio, stringBuilder.toString()));
                    prio++;
                }
                stringBuilder = new StringBuilder();

                int tagIndex;
                for (tagIndex = i; content.charAt(tagIndex) != '>'; tagIndex++) {
                    stringBuilder.append(content.charAt(tagIndex));
                }
                stringBuilder.append(">");
                i = tagIndex;
                StringBuilder innerStrinngBuilder = new StringBuilder();
                int innerTags = 1;

                for (int j = tagIndex + 1; j < content.length(); j++) {

                    if (content.charAt(j) == '<' && content.charAt(j + 1) != '/') {

                        innerStrinngBuilder.append(content.charAt(j));
                        innerTags++;

                    } else if (content.charAt(j) == '<' && content.charAt(j + 1) == '/') {

                        innerTags--;
                        if (innerTags == 0) {
                            stringBuilder.append(innerStrinngBuilder.toString());
                            for (j = j; content.charAt(j) != '>'; j++) {
                                stringBuilder.append(content.charAt(j));
                            }
                            stringBuilder.append(">");
                            i = j;

                            splittedContent.add(new TagString(prio, stringBuilder.toString()));
                            prio++;
                            stringBuilder = new StringBuilder();
                            break;
                        } else {
                            innerStrinngBuilder.append(content.charAt(j));
                        }

                    } else {

                        innerStrinngBuilder.append(content.charAt(j));

                    }
                }
                if (!stringBuilder.toString().equals("")) {
                    splittedContent.add(new TagString(prio, stringBuilder.toString()));
                    prio++;
                    stringBuilder = new StringBuilder();
                }

            } else {
                stringBuilder.append(content.charAt(i));
            }

        }
        if (!stringBuilder.toString().trim().equals("") && !splittedContent.contains(stringBuilder.toString())) {
            splittedContent.add(new TagString(prio, stringBuilder.toString()));
        }

        return splittedContent;
    }

}
