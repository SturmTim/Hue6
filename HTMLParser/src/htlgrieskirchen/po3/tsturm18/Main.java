/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htlgrieskirchen.po3.tsturm18;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author timst
 */
public class Main {

    private static final File file = new File("sample3.html");

    public static void main(String[] args) {

        ExecutorService executor = Executors.newCachedThreadPool();

        List<TagString> tagList = new ArrayList<>();
        try {
            tagList = HTMLCallable.getSplittedContent(readFile(file));
        } catch (IOException e) {
            System.out.println("IOException");
        }

        List<HTMLCallable> callableList = new ArrayList<>();
        for (TagString string : tagList) {
            callableList.add(new HTMLCallable(string.getTagPriority(), new Tag(string.getFullString())));
        }

        List<Future<TagString>> result = new ArrayList<>();
        try {
            result = executor.invokeAll(callableList);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }

        result.stream()
                .map(ergebniss -> {
                    try {
                        return ergebniss.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).sorted()
                .map(prioritisedString -> prioritisedString.getFullString())
                .forEach(System.out::print);

        System.out.print("\n");
        executor.shutdown();
    }

    private static String readFile(File file) throws IOException {
        return Files.lines(file.toPath())
                .reduce((string1, string2) -> string1 + string2)
                .orElse("");
    }
}
