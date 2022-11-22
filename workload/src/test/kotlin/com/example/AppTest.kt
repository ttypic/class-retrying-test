package com.example

import com.ttypic.junitpioneer.ext.ClassRetryingTest
import org.junit.jupiter.api.Test

class AppTest {

    @Test
    fun test1() {
        assert(true)
    }

    @ClassRetryingTest
    fun test2() {
        assert(true)
    }

    @ClassRetryingTest
    fun test3() {
        assert(true)
    }

}
