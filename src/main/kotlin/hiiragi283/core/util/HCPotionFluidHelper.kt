package hiiragi283.core.util

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.setup.HCFluids
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.fluids.FluidStack

object HCPotionFluidHelper {
    @JvmStatic
    fun createResource(contents: HTPotionContents): HTFluidResourceType = createFluid(contents).toResource()!!

    @JvmStatic
    fun createFluid(contents: HTPotionContents, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack =
        HTPotionHelper.setContents(HCFluids.POTION.toStack(amount), contents)

    @JvmStatic
    fun createItem(item: ItemLike, contents: HTPotionContents): ItemStack =
        HTPotionHelper.setContents(ItemStack(item), contents)

    @JvmStatic
    fun createBucket(contents: HTPotionContents): ItemStack = createItem(HCFluids.POTION.getBucket(), contents)
}
