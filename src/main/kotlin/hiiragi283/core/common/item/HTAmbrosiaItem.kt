package hiiragi283.core.common.item

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTAmbrosiaItem(properties: Properties) : HTCreativeItem(properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val copied: ItemStack = stack.copy()
        super.finishUsingItem(stack, level, livingEntity)
        return copied
    }
}
