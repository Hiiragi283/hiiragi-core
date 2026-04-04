package hiiragi283.core.api

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

abstract class HiiragiCoreAccess {
    companion object {
        @JvmField
        val INSTANCE: HiiragiCoreAccess = HiiragiCoreAPI.getService()
    }

    //    Potion    //

    /**
     * 指定した[resource]から[BottledPotionContents]を取得します。
     * @return 取得できなかった場合は`null`
     * @since 0.11.0
     * @see HTPotionHelper.getContents
     */
    abstract fun getContents(resource: FluidResource): BottledPotionContents?

    abstract fun getContents(resource: ItemResource): BottledPotionContents?

    /**
     * 指定した[stack]に[BottledPotionContents]を設定します。
     * @since 0.11.0
     * @see HTPotionHelper.setContents
     */
    abstract fun setContents(stack: FluidStack, contents: BottledPotionContents)

    abstract fun setContents(stack: ItemStack, contents: BottledPotionContents)
}
