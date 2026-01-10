package hiiragi283.core.common.fluid

import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.util.HTMoltenMetalHelper
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTMoltenMetalFluidType(properties: Properties) : FluidType(properties) {
    override fun getDescription(stack: FluidStack): Component = HTMoltenMetalHelper
        .getMoltenMetal(stack)
        ?.let { HCTranslation.MOLTEN_METAL.translate(it) }
        ?: super.getDescription(stack)

    override fun getBucket(stack: FluidStack): ItemStack = HTMoltenMetalHelper.createBucket(stack)
}
