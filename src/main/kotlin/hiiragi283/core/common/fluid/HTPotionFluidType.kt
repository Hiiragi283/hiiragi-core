package hiiragi283.core.common.fluid

import hiiragi283.core.api.data.buildDataPatch
import hiiragi283.core.api.fluid.HTFluidType
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTPotionFluidType(properties: Properties) : HTFluidType(properties) {
    override fun getDescriptionId(stack: FluidStack): String =
        HTPotionHelper.getPotionName(stack, HTPotionFluidManager.getBottleType(stack))

    override fun getBucket(stack: FluidStack): ItemStack = createItemStack(
        HCFluids.POTION.getBucketHolder(),
        patch = buildDataPatch {
            set(DataComponents.POTION_CONTENTS, HTPotionFluidManager.getContents(stack))
            set(HCDataComponents.BOTTLE_TYPE, HTPotionFluidManager.getBottleType(stack))
        },
    )
}
