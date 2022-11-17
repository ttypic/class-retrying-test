package com.example

import com.example.annotation.Method2Class

class SimpleTest {

    fun beforeEach() {
        val i = 0
    }

    @Method2Class
    fun test1() {
        val i = 1
    }

    @Method2Class
    fun test2() {
        val i = 2
    }

    @Method2Class
    fun test3() {
        val i = 3
    }
}