package hiiragi283.core.common.item

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.food.FoodConstants
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTAmbrosiaItem(properties: Properties) : HTCreativeItem(properties) {
    override fun getFoodProperties(stack: ItemStack, entity: LivingEntity?): FoodProperties = super.getFoodProperties(stack, entity)
        ?: FoodProperties
            .Builder()
            .nutrition(FoodConstants.MAX_FOOD)
            .saturationModifier(0.5f)
            .alwaysEdible()
            .usingConvertsTo(this)
            .build()

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val copied: ItemStack = stack.copy()
        super.finishUsingItem(stack, level, livingEntity)
        return copied
    }
}
