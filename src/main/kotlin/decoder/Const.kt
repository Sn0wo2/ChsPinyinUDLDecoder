package decoder

enum class Const(val value: Int) {
    HEADER_SIZE_UDL(9216),
    HEADER_SIZE_IH(5120),
    BLOCK_SIZE(60),
    WORD_LEN_OFFSET(10),
    WORD_START_OFFSET(12);
}