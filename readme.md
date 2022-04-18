# What is it?
This is an experimental project in order to compare different implementations of `String` -> `Character`s conversion.

With **Surrogate Pairs** in mind, the split result type cannot be `char[]` or `Character[]`, anything based on `char/Character`;
sadly they cannot represent a *character* as humans understand.

So these methods return `List<String>` of which each element represent
*one character* as humans understand.

These implementations are extensively tested, however the performance tester
class is not.

There are 4 implementations:
- **String.codePoints()** (`IntStream`, `Character.toString(int)`) -- leveraging Java 9's `String#codePoints()` and Java 11's `Character.toString(int)` takinig the *codePoint* and Stream API. Fastest. The code is the **simplest** and easily understandable, too.

        public static List<String> toCharacterStringListWithCodePoints(String str) {
            if (str == null) {
                return Collections.emptyList();
            }
            return str.codePoints()
                .mapToObj(Character::toString)
                .collect(Collectors.toList());
        }

- classical -- iterate on character array examining if current + next are surrogate pair, in which case create one string with these two `char`'s. If not, the current character becomes one string.
- regex (`"(?<=.)"`) -- This magical regex pattern can be used to our purpose. Slower than the above 2.
- classical with *Strategy* with **Functional API** (very slow)

# How to run
The `./gradlew build` command will copy dependency jars into `jars` directory.
Then you can issue

    $ java -cp build/libs/sandbox-0.0.1-SNAPSHOT.jar:'jars/*' \
    com.mt.sandbox.utils.MeasurePerformance

You can also issue

    $ ./gradlew run

