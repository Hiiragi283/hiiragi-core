package hiiragi283.core.common.util

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.impl.registry.VanillaFluidContents
import hiiragi283.core.setup.HCFluids
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.fluid.FluidResource

object HCPotionFluidHelper {
    /**
     * @return [contents]が`null`の場合，水を返す。
     */
    @JvmStatic
    fun createResource(contents: BottledPotionContents): FluidResource = createFluid(contents).let(FluidResource::of)

    /**
     * @return [contents]が`null`の場合，水を返す。
     */
    @JvmStatic
    fun createFluid(contents: BottledPotionContents, amount: Int = FluidType.BUCKET_VOLUME): FluidStack = when {
        contents.isWater -> VanillaFluidContents.WATER.toStack(amount)
        else -> HTPotionHelper.setContents(HCFluids.POTION.toStack(amount), contents)
    }

    /**
     * @return [contents]が`null`の場合，水入りバケツを返す。
     */
    @JvmStatic
    fun createBucket(contents: BottledPotionContents): ItemStack = when {
        contents.isWater -> VanillaFluidContents.WATER.bucketHolder.toStack()
        else -> HTPotionHelper.setContents(ItemStack(HCFluids.POTION.getBucket()), contents)
    }
}
