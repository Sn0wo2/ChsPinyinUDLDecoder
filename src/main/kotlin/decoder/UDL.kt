package decoder

import decoder.impl.IDecoder
import java.io.File

class UDL : IDecoder {
    override fun processFile(path: String?): Sequence<String> = sequence {
        val actualPath = if (path.isNullOrEmpty()) {
            val local = File("ChsPinyinIH.dat")
            if (local.exists()) local.path
            else "${System.getenv("USERPROFILE")}\\AppData\\Roaming\\Microsoft\\InputMethod\\Chs\\ChsPinyinUDL.dat"
        } else path
        val file = File(actualPath)
        if (!file.exists()) {
            println("File $path does not exist.")
            return@sequence
        }

        // Drop header
        val data = file.readBytes().drop(Const.HEADER_SIZE_UDL.value).toByteArray()

        generateSequence(1) { it + 1 }.forEach { blockIndex ->
            val chunk = blockIndex * Const.BLOCK_SIZE.value
            val lenPos = chunk + Const.WORD_LEN_OFFSET.value
            val start = chunk + Const.WORD_START_OFFSET.value

            if (lenPos >= data.size) return@sequence
            val end = start + (data[lenPos].toInt() and 0xFF) * 2
            if (end > data.size) return@sequence

            data.copyOfRange(start, end).takeIf(ByteArray::isNotEmpty)?.let {
                yield(String(it, Charsets.UTF_16LE))
            }
        }
    }
}