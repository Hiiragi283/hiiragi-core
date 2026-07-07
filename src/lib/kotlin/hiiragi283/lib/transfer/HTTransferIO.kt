package hiiragi283.lib.transfer

enum class HTTransferIO(val canInsert: Boolean, val canExtract: Boolean) {
    INSERT_ONLY(true, false),
    EXTRACT_ONLY(false, true),
    BOTH(true, true),
    NONE(false, false),
}
