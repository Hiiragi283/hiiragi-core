package hiiragi283.core.api.item.alchemy

import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions

fun PotionContents.isEmpty(): Boolean {
    if (this == PotionContents.EMPTY) return true
    val bool1: Boolean = this.potion().filter { it != Potions.WATER }.isEmpty
    val bool2: Boolean = this.customEffects().isEmpty()
    return bool1 && bool2
}
