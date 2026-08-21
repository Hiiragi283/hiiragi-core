package hiiragi283.core.internal.item.alchemy

import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionAccess
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.component.DataComponentType

class HTPotionAccessImpl : HTPotionAccess {
    override val fluidContent: HTFluidContent get() = HCFluids.POTION
    override val bottleTypeComponent: DataComponentType<HTBottleType> get() = HCDataComponents.BOTTLE_TYPE
}
