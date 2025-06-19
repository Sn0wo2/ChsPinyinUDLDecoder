package decoder.impl

interface IDecoder {
    fun processFile(path: String? = null): Sequence<String>
}