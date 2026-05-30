package hiiragi283.core.common.item.endgame

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTAmbrosiaItem(properties: Properties) : HTCreativeItem(properties) {
    /**
     * @see net.minecraft.world.item.component.Consumable.onConsume
     */
    override fun finishUsingItem(itemStack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        if (!entity.hasInfiniteMaterials()) {
            itemStack.grow(1)
        }
        return super.finishUsingItem(itemStack, level, entity)
    }
}
