package hiiragi283.core.impl.storage.fluid

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.fluid.HTFluidTank
import net.minecraft.world.item.ItemStack

/**
 * 液体入りコンテナ向けの[HTFluidTank]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTItemFluidTank :
    HTFluidTank,
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    val container: ItemStack
}
