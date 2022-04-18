package com.mt.sandbox.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MeasurePerformance {

    private static final List<MethodReferenceWrapper> REF_LIST = Arrays.asList(
        new MethodReferenceWrapper(StringSplitter::toCharacterStringListWithCodePoints, "codePoints"),
        new MethodReferenceWrapper(StringSplitter::toCharacterStringListWithIfBlock, "classic"),
        new MethodReferenceWrapper(StringSplitter::toCharacterStringListWithRegex, "regex")
        // new RefWrapper(StringSplitter::toCharacterStringListWithStrategy, "strategy") // uncomment this if very slow execution is acceptable
    );

    static final String ASCII = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ;:=-_,.1234567890";
    static final String EMOJIS;

    static {
        InputStream is = null;
        try {
            is = MeasurePerformance.class.getResourceAsStream("/emojis.txt");
            EMOJIS = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            closeSilentlyOrExit(is);
        }
    }

    private static void closeSilentlyOrExit(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }

    static class MethodReferenceWrapper {
        final Function<String, List<String>> function;
        final String name;

        MethodReferenceWrapper(Function<String, List<String>> function, String name) {
            this.function = function;
            this.name = name;
        }
    }

    public static void main(String[] args) {
        log.info("Performance comparison\n");

        List<String> methodNames = new ArrayList<>();
        List<Map<String, Long>> measureList = new ArrayList<>();
        IntStream.range(0, 10).forEach(idx -> {
            int max = (int) Math.pow(2, 8 + idx);
            int step = max / 100;
            List<String> testData = prepareTestData(max, step);
            Map<String, Long> measureMap = new HashMap<>();
            for (MethodReferenceWrapper methodRef : REF_LIST) {
                if (methodNames.size() < REF_LIST.size()) {
                    methodNames.add(methodRef.name);
                }
                Instant begin = Instant.now();
                List<Integer> doneList = Collections.synchronizedList(new ArrayList<>());
                IntStream.range(0, testData.size()).parallel().forEach(i -> {
                    methodRef.function.apply(testData.get(i));
                    doneList.add(i);
                    int methodDoneCount = doneList.size();
                    if (methodDoneCount % step == 0) {
                        log.info("{}: {}%\033[K\r", methodRef.name, methodDoneCount * 100 / max);
                    }
                });
                long millis = Duration.between(begin, Instant.now()).toMillis();
                measureMap.put(methodRef.name, millis);
            }
            measureMap.put("lines", (long) max);
            measureList.add(measureMap);
        });
        methodNames.add("lines");
        log.info("{}\n", String.join(";", methodNames));
        measureList.stream().map(map ->
            methodNames.stream()
                .map(map::get)
                .map(String::valueOf)
                .collect(Collectors.joining(";")))
            .forEach(s -> log.info("{}\n", s) );
    }

    private static List<String> prepareTestData(int max, int step) {
        List<String> testData = new ArrayList<>();
        List<String> emojiList = StringSplitter.toCharacterStringListWithIfBlock(EMOJIS);
        int count = (int) EMOJIS.codePoints().count();
        char[] normalChars = ASCII.toCharArray();
        for (int i = 0; i < max; i++) {
            List<String> charStringList = new ArrayList<>();
            IntStream.range(0, 1000).forEach((idx) -> {
                int random = (int) (Math.random() * max);
                charStringList.add(emojiList.get(random % count));
                charStringList.add(Character.toString(normalChars[random % normalChars.length]));
            });
            testData.add(String.join("", charStringList));
            if (i % step == 0) {
                log.info("\r\033[Kprepared: {}%", i * 100 / max);
            }
        }
        return testData;
    }
}