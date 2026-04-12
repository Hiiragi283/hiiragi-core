package hiiragi283.core.common.data.tank

import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack

data class HTOminousTankInteraction(val amplifier: Int) : HTTankInteraction.Emptying {
    companion object {
        @JvmField
        val RANGE: IntRange = 0..4
    }

    override fun canEmptyContainer(container: HTItemResourceType): Boolean =
        container.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER) == amplifier

    override fun emptyContainer(container: HTItemResourceType): Pair<ItemStack, FluidStack> =
        ItemStack(Items.GLASS_BOTTLE) to HCFluids.OMINOUS_FLUX.toStack(amount)

    override val amount: Int = 250 * (amplifier + 1)
}
