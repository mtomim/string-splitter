package com.mt.sandbox.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mt.sandbox.utils.CharArrayStrategy.Args;

public class StringSplitter {

    /**
     * This functional API based Strategy pattern to avoid if/else blocks
     * is very slow, the slowest by far among all implementations.
     * @param str The string to split into string list each of which elements is a character (for human).
     * @return List of string each representing a character.
     */
    public static List<String> toCharacterStringListWithStrategy(String str) {
        if (str == null) {
            return Collections.emptyList();
        }
        List<String> strings = new ArrayList<>();
        for (Args arg = new Args(0, str.toCharArray()); arg.i < str.toCharArray().length; arg.i++) {
            strings.add(CharArrayStrategy.of(arg).doWork(arg));
        }
        return strings;
    }

    public static List<String> toCharacterStringListWithIfBlock(String str) {
        if (str == null) {
            return Collections.emptyList();
        }
        List<String> strings = new ArrayList<>();
        char[] charArray = str.toCharArray();
        int delta = 1;
        for (int i = 0; i < charArray.length; i += delta) {
            delta = 1;
            if (i < charArray.length - 1 && Character.isSurrogatePair(charArray[i], charArray[i + 1])) {
                delta = 2;
                strings.add(String.valueOf(new char[]{ charArray[i], charArray[i + 1] }));
            } else {
                strings.add(Character.toString(charArray[i]));
            }
        }
        return strings;
    }

    public static List<String> toCharacterStringListWithCodePoints(String str) {
        if (str == null) {
            return Collections.emptyList();
        }
        return str.codePoints()
            .mapToObj(Character::toString)
            .collect(Collectors.toList());
    }

    static final Pattern p = Pattern.compile("(?<=.)");
    public static List<String> toCharacterStringListWithRegex(String str) {
        if (str == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(p.split(str));
    }

    private StringSplitter() {
        super();
    }
}
