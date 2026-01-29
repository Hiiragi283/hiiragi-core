package hiiragi283.core.api.gui.sync

/**
 * 同期の方向を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
enum class HTSyncType(val allowS2C: Boolean, val allowC2S: Boolean) {
    BOTH(true, true),
    S2C(true, false),
    C2S(false, true),
}
