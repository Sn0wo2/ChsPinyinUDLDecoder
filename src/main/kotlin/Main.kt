import decoder.IH
import decoder.UDL
import decoder.impl.IDecoder
import kotlin.time.Duration.Companion.milliseconds

fun main() {
    val start = System.currentTimeMillis()
    val ih: IDecoder = IH()
    ih.processFile().forEach { println(it) }

    val udl: IDecoder = UDL()
    udl.processFile().forEach { println(it) }

    println("Time: ${(System.currentTimeMillis() - start).milliseconds}")
}
