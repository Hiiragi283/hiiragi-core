package hiiragi283.lib.transfer

/**
 * 搬入出の種類を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
enum class HTTransferIO(val canInsert: Boolean, val canExtract: Boolean) {
    INSERT_ONLY(true, false),
    EXTRACT_ONLY(false, true),
    BOTH(true, true),
    NONE(false, false),
}
