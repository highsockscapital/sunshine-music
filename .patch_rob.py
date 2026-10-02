import io

def patch(path, pairs):
    s = io.open(path, encoding='utf-8').read()
    for old, new in pairs:
        assert s.count(old) == 1, (path, s.count(old), repr(old[:70]))
        s = s.replace(old, new)
    io.open(path, 'w', encoding='utf-8').write(s)
    print('patched', path)

patch('app/build.gradle.kts', [
    # Robolectric 4.17-beta-2 cannot run on JDK 21: every one of the 37 cases died in
    # AndroidInterceptors.java:88 / IllegalAccessException before touching an assertion,
    # and --add-opens did not help. LrcUtilsTest runs on a plain JVM instead.
    ('''    testOptions.unitTests.isIncludeAndroidResources = true
    testOptions.unitTests.all {
        it.jvmArgs(
            "--add-opens=java.base/java.lang=ALL-UNNAMED",
            "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
            "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
            "--add-opens=java.base/java.util=ALL-UNNAMED",
            "--add-opens=java.base/java.io=ALL-UNNAMED",
            "--add-opens=java.base/java.net=ALL-UNNAMED",
            "--add-opens=java.base/java.text=ALL-UNNAMED",
            "--add-opens=java.base/java.security=ALL-UNNAMED",
        )
    }''',
     '''    testOptions.unitTests.isIncludeAndroidResources = true
    // The LRC parsing paths reach android.util.Log only through media3, so letting the stubbed
    // android.jar return defaults is enough to run them without a simulated Android runtime.
    testOptions.unitTests.isReturnDefaultValues = true'''),
    ('    testImplementation("org.robolectric:robolectric:4.17-beta-2")\n', ''),
])

patch('app/src/test/java/org/akanework/gramophone/LrcUtilsTest.kt', [
    ('import org.junit.runner.RunWith\nimport org.robolectric.RobolectricTestRunner\n', ''),
    ('@RunWith(RobolectricTestRunner::class)\n', ''),
    # android.util.Xml.newPullParser() has no JVM implementation, so the two TTML cases
    # cannot run once Robolectric is gone. They are the only ones that parsed XML.
    ('''    fun testParserTtmlTemplate() {
        val ttml = parseSynced(LrcTestData2.TTML_DEATH_BED)
        assertEquals(LrcTestData2.TTML_DEATH_BED_PARSED, ttml)
    }

    fun testParserTtmlTemplate2() {
        val ttml = parseSynced(LrcTestData2.TTML_SATISIFED)
        assertEquals(LrcTestData2.TTML_SATISFIED_PARSED, ttml)
    }

''', ''),
])