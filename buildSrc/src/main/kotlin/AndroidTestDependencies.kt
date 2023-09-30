object AndroidTestDependencies{

    const val instrumentation_runner = "com.seancoyle.core.testing.HiltTestRunner"

    private const val androidx_test_ext_version = "1.1.3"
    const val androidx_test_ext = "androidx.test.ext:junit-ktx:$androidx_test_ext_version"

    private const val espresso_core_version = "3.5.0"
    const val espresso_core = "androidx.test.espresso:espresso-core:$espresso_core_version"
    const val espresso_intents = "androidx.test.espresso:espresso-intents:$espresso_core_version"

    private const val espresso_idling_resource_version = "3.5.0"
    const val idling_resource = "androidx.test.espresso:espresso-idling-resource:$espresso_idling_resource_version"

    private const val mockk_version = "1.13.5"
    const val mockk_android = "io.mockk:mockk-android:$mockk_version"
    const val mockk = "io.mockk:mockk:$mockk_version"

    private const val test_rules_version = "1.5.0"
    const val test_rules = "androidx.test:rules:$test_rules_version"

    private const val test_runner_version = "1.5.2"
    const val test_runner = "androidx.test:runner:$test_runner_version"

    private const val test_core_version = "1.5.0"
    const val test_core_ktx = "androidx.test:core-ktx:$test_core_version"

    private const val test_monitor_version = "1.6.0"
    const val test_core_monitor = "androidx.test:monitor:$test_monitor_version"

    private const val test_arch_core_version = "2.2.0"
    const val test_arch_core = "androidx.arch.core:core-testing:$test_arch_core_version"

    private const val junit_4_version = "4.13"
    const val junit4 = "junit:junit:$junit_4_version"

    private const val junit_jupiter_version = "5.6.0"
    const val jupiter_api = "org.junit.jupiter:junit-jupiter-api:$junit_jupiter_version"
    const val jupiter_engine = "org.junit.jupiter:junit-jupiter-engine:$junit_jupiter_version"
    const val jupiter_params = "org.junit.jupiter:junit-jupiter-params:$junit_jupiter_version"
}