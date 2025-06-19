package decoder

import decoder.impl.IDecoder
import java.io.File

class IH : IDecoder {
    override fun processFile(path: String?): Sequence<String> = sequence {
        val actualPath = if (path.isNullOrEmpty()) {
            val local = File("ChsPinyinIH.dat")
            if (local.exists()) local.path
            else "${System.getenv("USERPROFILE")}\\AppData\\Roaming\\Microsoft\\InputMethod\\Chs\\ChsPinyinIH.dat"
        } else path

        val file = File(actualPath)
        if (!file.exists()) {
            println("File $actualPath does not exist.")
            return@sequence
        }

        val data = file.readBytes()
            .drop(Const.HEADER_SIZE_IH.value)
            .toByteArray()

        generateSequence(1) { it + 1 }.forEach { blockIndex ->
            val chunkStart = blockIndex * Const.BLOCK_SIZE.value

            if (chunkStart + Const.WORD_START_OFFSET.value >= data.size) return@sequence

            val charCount = data[chunkStart].toInt() and 0xFF

            val wordStart = chunkStart + Const.WORD_START_OFFSET.value
            val wordEnd = wordStart + charCount * 2
            if (wordEnd > data.size) return@sequence

            data.copyOfRange(wordStart, wordEnd).takeIf(ByteArray::isNotEmpty)?.let {
                yield(String(it, Charsets.UTF_16LE))
            }
        }
    }
}