package hiiragi283.core.setup

import hiiragi283.core.common.item.consume.HTClearRandomEffectConsumeEffect
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.Consumables

/**
 * @see Consumables
 */
data object HCConsumables {
    @JvmField
    val WARPED_WART: Consumable = Consumables.defaultFood()
        .onConsume(HTClearRandomEffectConsumeEffect)
        .build()
}
