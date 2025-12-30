package hiiragi283.core.api.inventory.slot

/**
 * 同期のフラグを管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.inventory.container.sync.ISyncableData.DirtyType
 */
enum class HTChangeType {
    EMPTY,
    AMOUNT,
    FULL,
}
