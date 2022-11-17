import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import java.io.OutputStream

class Method2ClassProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("com.example.annotation.Method2Class")
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(Method2ClassVisitor(), Unit) }
        return ret
    }

    inner class Method2ClassVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            classDeclaration.primaryConstructor!!.accept(this, data)
        }

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val parent = function.parentDeclaration as KSClassDeclaration
            val packageName = parent.containingFile!!.packageName.asString()
            val className = "${parent.simpleName.asString()}_${function.simpleName.asString()}"
            val file = codeGenerator.createNewFile(Dependencies(true, function.containingFile!!), packageName, className)
            file.appendText("package $packageName\n\n")
            file.appendText("class $className {\n")
            file.appendText("}")
            file.close()
        }
    }

    fun OutputStream.appendText(str: String) {
        this.write(str.toByteArray())
    }
}

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class Method2ClassProcessorProvider: SymbolProcessorProvider {
    override fun create(
        env: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return Method2ClassProcessor(env.codeGenerator, env.logger)
    }
}