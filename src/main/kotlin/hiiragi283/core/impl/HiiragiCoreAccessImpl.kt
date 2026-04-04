package hiiragi283.core.impl

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

class HiiragiCoreAccessImpl : HiiragiCoreAccess() {
    companion object {
        @JvmField
        val DEFAULT_POTION_HANDLER: HTPotionFluidManager.Handler = object : HTPotionFluidManager.Handler {
            override fun get(holder: DataComponentGetter): HTBottleType? = holder.get(HCDataComponents.BOTTLE_TYPE)

            override fun set(holder: MutableDataComponentHolder, bottleType: HTBottleType) {
                holder.set(HCDataComponents.BOTTLE_TYPE, bottleType)
            }
        }
    }

    override fun getContents(resource: FluidResource): BottledPotionContents? {
        val handler: HTPotionFluidManager.Handler = when {
            resource.`is`(Tags.Fluids.WATER) -> return BottledPotionContents(Potions.WATER)
            else -> HTPotionFluidManager.getFluidHandler(resource.typeHolder()) ?: DEFAULT_POTION_HANDLER
        }
        val bottleType: HTBottleType = handler[resource] ?: return null
        val contents: PotionContents = HTPotionHelper.getPotion(resource)
        return BottledPotionContents(contents, bottleType)
    }

    override fun getContents(resource: ItemResource): BottledPotionContents? {
        val bottleType: HTBottleType = DEFAULT_POTION_HANDLER[resource] ?: return null
        val contents: PotionContents = HTPotionHelper.getPotion(resource)
        return BottledPotionContents(contents, bottleType)
    }

    override fun setContents(stack: FluidStack, contents: BottledPotionContents) {
        HTPotionHelper.setPotion(stack, contents.contents)
        val handler: HTPotionFluidManager.Handler = HTPotionFluidManager.getFluidHandler(stack.typeHolder()) ?: DEFAULT_POTION_HANDLER
        handler[stack] = contents.bottleType
    }

    override fun setContents(stack: ItemStack, contents: BottledPotionContents) {
        HTPotionHelper.setPotion(stack, contents.contents)
        DEFAULT_POTION_HANDLER[stack] = contents.bottleType
    }
}
