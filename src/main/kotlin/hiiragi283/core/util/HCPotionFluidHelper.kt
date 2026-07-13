package hiiragi283.core.util

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

data object HCPotionFluidHelper {
    /**
     * @return [contents]が`null`の場合，水を返す。
     */
    @JvmStatic
    fun createResource(contents: BottledPotionContents): HTFluidResourceType = createFluid(contents).toResource()!!

    @JvmStatic
    fun createFluid(potion: Holder<Potion>, bottleType: HTBottleType = HTBottleType.DEFAULT, amount: Int = FluidType.BUCKET_VOLUME): FluidStack = createFluid(BottledPotionContents(potion, bottleType), amount)

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
        else -> HTPotionHelper.setContents(HCFluids.POTION.bucketHolder.toStack(), contents)
    }
}
