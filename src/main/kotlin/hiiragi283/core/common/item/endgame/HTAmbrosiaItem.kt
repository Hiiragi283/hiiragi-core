package hiiragi283.core.common.item.endgame

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTAmbrosiaItem(properties: Properties) : HTCreativeItem(properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        livingEntity.eat(level, stack.copy())
        return stack
    }
}
