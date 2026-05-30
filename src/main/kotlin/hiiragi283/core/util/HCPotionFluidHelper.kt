package hiiragi283.core.util

import hiiragi283.core.setup.HCFluids
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.registry.VanillaFluidContents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.fluid.FluidResource

data object HCPotionFluidHelper {
    /**
     * @return [contents]が`null`の場合，水を返す。
     */
    @JvmStatic
    fun createResource(contents: BottledPotionContents): FluidResource = FluidResource.of(createFluid(contents))

    /**
     * @return [contents]が`null`の場合，水を返す。
     */
    @JvmStatic
    fun createFluid(contents: BottledPotionContents, amount: Int = FluidType.BUCKET_VOLUME): FluidStack = when {
        contents.isWater -> VanillaFluidContents.WATER.toStack(amount)
        else -> HCFluids.POTION.toStack(amount, HTPotionHelper.createFluidPatch(HCFluids.POTION.get(), contents))
    }

    /**
     * @return [contents]が`null`の場合，水入りバケツを返す。
     */
    @JvmStatic
    fun createBucket(contents: BottledPotionContents): ItemStack = when {
        contents.isWater -> VanillaFluidContents.WATER.bucketHolder.toStack()
        else -> HCFluids.POTION.bucketHolder.toStack(patch = HTPotionHelper.createItemPatch(contents))
    }
}
