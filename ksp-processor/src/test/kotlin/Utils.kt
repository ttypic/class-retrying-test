import java.nio.charset.Charset

inline fun <reified T: Any> T.readResource(filename: String): String {
    return this.javaClass.classLoader?.getResource(filename)?.readText(Charset.defaultCharset())
        ?: throw IllegalArgumentException("Can not find file with name=$filename in resources")
}
