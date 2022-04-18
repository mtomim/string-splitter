# Read Me
The `./gradlew build` command will copy dependency jars into `jars` directory.
Then you can issue

    $ java -cp build/libs/sandbox-0.0.1-SNAPSHOT.jar:'jars/*' \
    com.mt.sandbox.utils.MeasurePerformance

You can also issue

    $ ./gradlew run

