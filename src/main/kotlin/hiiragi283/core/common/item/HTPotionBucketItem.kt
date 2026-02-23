package hiiragi283.core.common.item

import hiiragi283.core.api.item.HTPotionBasedItem
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper

class HTPotionBucketItem(content: Fluid, properties: Properties) : HTPotionBasedItem(properties) {
    override fun getName(stack: ItemStack): Text {
        val descriptionId: String = HTPotionHelper.getPotionDescId(stack) ?: return super.getName(stack)
        return translatableText(super.descriptionId, translatableText(descriptionId))
    }

    class BucketHandler(container: ItemStack) : FluidBucketWrapper(container) {
        override fun getFluid(): FluidStack =
            HTPotionHelper.getContents(container)?.let(HCPotionFluidHelper::createFluid) ?: FluidStack.EMPTY
    }
}
