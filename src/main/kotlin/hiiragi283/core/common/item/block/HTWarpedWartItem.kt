package hiiragi283.core.common.item.block

import hiiragi283.core.api.collection.randomOrNull
import hiiragi283.core.api.item.HTDescriptionBlockItem
import hiiragi283.core.common.block.HTWarpedWartBlock
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import java.util.Optional

class HTWarpedWartItem(block: HTWarpedWartBlock, properties: Properties) : HTDescriptionBlockItem<HTWarpedWartBlock>(block, properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        livingEntity.activeEffects
            .map(MobEffectInstance::getEffect)
            .filter { it.value().category == MobEffectCategory.HARMFUL }
            .randomOrNull(level.random)
            ?.let(livingEntity::removeEffect)
        return super.finishUsingItem(stack, level, livingEntity)
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT
    
    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int =
        stack.getFoodProperties(entity)?.eatDurationTicks() ?: super.getUseDuration(stack, entity)

    override fun getFoodProperties(stack: ItemStack, entity: LivingEntity?): FoodProperties? {
        if (entity == null) return null
        if (entity.activeEffects.isEmpty()) return null
        return FoodProperties(0, 0f, true, 1.6f, Optional.empty(), emptyList())
    }
}
