import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassRetryingTestProcessorTest {

    @Test
    fun `should generate test classes for annotated methods`() {
        val kotlinSource = SourceFile.kotlin(
            "KClass.kt", """
                package com.example.foo
                
                import com.ttypic.junitpioneer.ext.ClassRetryingTest
                
                class KClass {
                    @ClassRetryingTest
                    fun test1() {
                        assert(true)
                    }
                
                    @ClassRetryingTest
                    fun test2() {
                        assert(false)
                    }
                }
            """.trimIndent()
        )

        val compilation = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            symbolProcessorProviders = listOf(ClassRetryingTestProcessorProvider())
            inheritClassPath = true
            messageOutputStream = System.out
        }

        compilation.compile()
        val sourceDir = compilation.kspSourcesDir

        val firstFile = File(sourceDir, "/kotlin/com/example/foo/KClass\$test1.kt")
        val secondFile = File(sourceDir, "/kotlin/com/example/foo/KClass\$test2.kt")

        assertEquals(readResource("KClass\$test1"), firstFile.readText())
        assertEquals(readResource("KClass\$test2"), secondFile.readText())
    }
}
