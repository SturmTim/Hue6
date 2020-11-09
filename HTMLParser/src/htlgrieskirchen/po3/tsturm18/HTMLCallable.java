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

        List<TagString> splittetContent = Tag.getSplittedContent(tag.getContent());

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
        splittetContent.forEach((tagString) -> {
            ergebniss.append(tagString.getFullString());
        });
        TagString tagString = new TagString(priority, ergebniss.toString());
        return tagString;
    }

}
