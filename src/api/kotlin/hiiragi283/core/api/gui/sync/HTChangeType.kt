package hiiragi283.core.api.gui.sync

/**
 * 同期のフラグを管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.common.inventory.container.sync.ISyncableData.DirtyType
 */
enum class HTChangeType {
    PARTIAL,
    FULL,
}
