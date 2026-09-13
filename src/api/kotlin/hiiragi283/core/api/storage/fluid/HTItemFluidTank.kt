package hiiragi283.core.api.storage.fluid

import net.minecraft.world.item.ItemStack

/**
 * 液体入りコンテナ向けの[HTFluidTank]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTItemFluidTank : HTFluidTank {
    val container: ItemStack
}
