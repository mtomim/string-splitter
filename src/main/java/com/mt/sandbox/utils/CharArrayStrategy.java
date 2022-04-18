package com.mt.sandbox.utils;

import java.util.function.Function;
import java.util.function.Predicate;

import lombok.AllArgsConstructor;

enum CharArrayStrategy {
    SURROGATE_PAIR(
            arg -> arg.i < arg.array.length - 1
                    && Character.isSurrogatePair(arg.current(), arg.next()),
            arg -> new String(new char[] { arg.current(), arg.incrementAndNext()})),
    DEFAULT(
            arg -> true,
            arg -> Character.toString(arg.current()));

    @AllArgsConstructor
    public static class Arg {
        int i;
        char[] array;

        char current() {
            return this.array[this.i];
        }

        char next() {
            return this.array[this.i + 1];
        }

        char incrementAndNext() {
            this.i++;
            return current();
        }
    }

    private final Predicate<Arg> predicate;
    private final Function<Arg, String> work;

    CharArrayStrategy(Predicate<Arg> predicate, Function<Arg, String> work) {
        this.predicate = predicate;
        this.work = work;
    }

    static CharArrayStrategy of(final Arg arg) {
        if (SURROGATE_PAIR.predicate.test(arg)) {
            return SURROGATE_PAIR;
        }
        return DEFAULT;
    }

    String doWork(Arg arg) {
        return this.work.apply(arg);
    }
}