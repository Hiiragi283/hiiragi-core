package hiiragi283.core.common.item

import hiiragi283.core.setup.HCItems
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

class HTInfinitePotionItem(properties: Properties) : HTCreativeItem(properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        livingEntity.activeEffects
            .map {
                MobEffectInstance(
                    it.effect,
                    -1,
                    it.amplifier,
                    it.isAmbient,
                    it.isVisible,
                    it.showIcon(),
                )
            }.forEach(livingEntity::addEffect)
        return super.finishUsingItem(stack, level, livingEntity)
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.DRINK

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int =
        stack.getFoodProperties(entity)?.eatDurationTicks() ?: super.getUseDuration(stack, entity)

    override fun getFoodProperties(stack: ItemStack, entity: LivingEntity?): FoodProperties? {
        if (entity == null) return null
        if (entity.activeEffects.isEmpty()) return null
        return HCItems.FAKE_FOOD
    }
}
