import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.*
import com.ttypic.junitpioneer.ext.ClassRetryingTest
import org.junitpioneer.jupiter.RetryingTest

class ClassRetryingTestProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotationName = ClassRetryingTest::class.qualifiedName!!
        val symbols = resolver.getSymbolsWithAnnotation(annotationName)
        symbols
            .filter { it.validate() }
            .forEach { it.accept(ClassRetryingTestVisitor(), Unit) }
        return listOf()
    }

    inner class ClassRetryingTestVisitor : KSVisitorVoid() {
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val parent = function.parentDeclaration as KSClassDeclaration
            val parentClassName = parent.simpleName.asString()
            val packageName = parent.containingFile!!.packageName.asString()
            val methodName = function.simpleName.asString()
            val className = "${parentClassName}$$methodName"

            logger.info("Processing $parentClassName method $methodName")

            val fileSpec = FileSpec.builder(
                packageName = packageName, fileName = className
            ).apply {
                addType(
                    TypeSpec.classBuilder(className)
                        .addFunction(funSpec = FunSpec.builder(name = methodName)
                            .addAnnotation(
                                annotationSpec = AnnotationSpec
                                    .builder(RetryingTest::class).addMember("3").build()
                            )
                            .addStatement("$parentClassName().$methodName()")
                            .build()
                        )
                        .build()
                )
            }.build()

            fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
        }
    }
}

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class ClassRetryingTestProcessorProvider: SymbolProcessorProvider {
    override fun create(
        env: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return ClassRetryingTestProcessor(env.codeGenerator, env.logger)
    }
}
