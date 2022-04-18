package com.mt.sandbox.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class StringSplitterTest {
    private static final int FAKE_LENGTH = MeasurePerformance.EMOJIS.length();
    private static final int CODEPOINT_COUNT = MeasurePerformance.EMOJIS.codePointCount(0, FAKE_LENGTH);

    @Test
    void fake_length_should_be_greater_than_real_length() {
        assertThat(FAKE_LENGTH).isGreaterThan(CODEPOINT_COUNT);
    }

    @Test
    void should_obtain_expected_list_with_surrogate_pairs_with_strategy() {
        assertThat(StringSplitter.toCharacterStringListWithStrategy("花ab🌹🌺🌷"))
                .isEqualTo(Arrays.asList("花", "a", "b", "🌹", "🌺", "🌷"));
        List<String> split = StringSplitter.toCharacterStringListWithStrategy(MeasurePerformance.EMOJIS);
        assertThat(split).hasSize(CODEPOINT_COUNT);
    }

    @Test
    void should_obtain_expected_list_with_surrogate_pairs() {
        assertThat(StringSplitter.toCharacterStringListWithIfBlock("花ab🌹🌺🌷"))
                .isEqualTo(Arrays.asList("花", "a", "b", "🌹", "🌺", "🌷"));
        List<String> split = StringSplitter.toCharacterStringListWithIfBlock(MeasurePerformance.EMOJIS);
        assertThat(split).hasSize(CODEPOINT_COUNT);
    }

    @Test
    void should_obtain_expected_list_with_surrogate_pairs_with_regex() {
        assertThat(StringSplitter.toCharacterStringListWithRegex("花ab🌹🌺🌷"))
                .isEqualTo(Arrays.asList("花", "a", "b", "🌹", "🌺", "🌷"));
        List<String> split = StringSplitter.toCharacterStringListWithRegex(MeasurePerformance.EMOJIS);
        assertThat(split).hasSize(CODEPOINT_COUNT);
    }

    @Test
    void should_obtain_expected_list_with_surrogate_pairs_with_codepoints() {
        assertThat(StringSplitter.toCharacterStringListWithCodePoints("花ab🌹🌺🌷"))
                .isEqualTo(Arrays.asList("花", "a", "b", "🌹", "🌺", "🌷"));
        List<String> split = StringSplitter.toCharacterStringListWithCodePoints(MeasurePerformance.EMOJIS);
        assertThat(split).hasSize(CODEPOINT_COUNT);
    }

    @Test
    void given_string_ending_with_half_of_surrogate_pair_should_not_throw_exception() {
        String str = "🌹🌺🌷";
        String weirdStr = new String(Arrays.copyOf(str.toCharArray(), 5));
        Stream.<Function<String,List<String>>>of(
            StringSplitter::toCharacterStringListWithCodePoints,
            StringSplitter::toCharacterStringListWithIfBlock,
            StringSplitter::toCharacterStringListWithRegex,
            StringSplitter::toCharacterStringListWithStrategy)
            .forEach(f -> assertDoesNotThrow(() -> f.apply(weirdStr)));
    }

    @Test
    void given_null_should_return_empty_list() {
        Stream.<Function<String,List<String>>>of(
            StringSplitter::toCharacterStringListWithCodePoints,
            StringSplitter::toCharacterStringListWithIfBlock,
            StringSplitter::toCharacterStringListWithRegex,
            StringSplitter::toCharacterStringListWithStrategy)
            .forEach(f -> assertThat(f.apply(null)).isEqualTo(Collections.emptyList()));
    }

}
