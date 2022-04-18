package com.mt.sandbox.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class StringSplitterTest {
    private static final int FAKE_LENGTH = MeasurePerformance.EMOJIS.length();
    private static final int length = MeasurePerformance.EMOJIS.codePointCount(0, FAKE_LENGTH);

    @Test
    void fake_length_should_be_greater_than_real_length() {
        assertThat(FAKE_LENGTH).isGreaterThan(length);
    }

    @Test
    void should_obtain_expected_list_with_surrogate_pairs() {
        assertThat(StringSplitter.toCharacterStringListWithStrategy("花ab🌹🌺🌷"))
                .isEqualTo(Arrays.asList("花", "a", "b", "🌹", "🌺", "🌷"));
        List<String> split = StringSplitter.toCharacterStringListWithStrategy(MeasurePerformance.EMOJIS);
        assertThat(split).hasSize(length);
    }

    @Test
    void should_obtain_expected_list_with_surrogate_pairs_with_regex() {
        assertThat(StringSplitter.toCharacterStringListWithRegex("花ab🌹🌺🌷"))
                .isEqualTo(Arrays.asList("花", "a", "b", "🌹", "🌺", "🌷"));
        List<String> split = StringSplitter.toCharacterStringListWithStrategy(MeasurePerformance.EMOJIS);
        assertThat(split).hasSize(length);
    }

    @Test
    void should_obtain_expected_list_with_surrogate_pairs_with_codepoints() {
        assertThat(StringSplitter.toCharacterStringListWithCodePoints("花ab🌹🌺🌷"))
                .isEqualTo(Arrays.asList("花", "a", "b", "🌹", "🌺", "🌷"));
        List<String> split = StringSplitter.toCharacterStringListWithStrategy(MeasurePerformance.EMOJIS);
        assertThat(split).hasSize(length);
    }

}
