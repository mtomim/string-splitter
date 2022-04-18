package com.mt.sandbox.utils;

import java.util.function.Function;
import java.util.function.Predicate;

import lombok.AllArgsConstructor;

enum CharArrayStrategy {
    SURROGATE_PAIR(
        arg -> arg.i < arg.array.length - 1
                && Character.isSurrogatePair(arg.array[arg.i], arg.array[arg.i + 1]),
        arg -> new String(new char[] { arg.array[arg.i], arg.array[++arg.i] })
    ),
    DEFAULT(
        arg -> true,
        arg -> Character.toString(arg.array[arg.i])
    );

    @AllArgsConstructor
    public static class Args {
        int i;
        char[] array;
    }
        private final Predicate<CharArrayStrategy.Args> predicate;
    private final Function<CharArrayStrategy.Args, String> work;

    CharArrayStrategy(Predicate<CharArrayStrategy.Args> predicate, Function<CharArrayStrategy.Args, String> work) {
        this.predicate = predicate;
        this.work = work;
    }

    static CharArrayStrategy of(final CharArrayStrategy.Args arg) {
        if (SURROGATE_PAIR.predicate.test(arg)) {
            return SURROGATE_PAIR;
        }
        return DEFAULT;
    }

    String doWork(CharArrayStrategy.Args arg) {
        return this.work.apply(arg);
    }
}