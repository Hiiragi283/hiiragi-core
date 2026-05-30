package hiiragi283.core.setup

import net.minecraft.world.food.FoodConstants
import net.minecraft.world.food.FoodProperties

/**
 * @see net.minecraft.world.food.Foods
 */
data object HCFoods {
    @JvmField
    val AMBROSIA: FoodProperties = FoodProperties
        .Builder()
        .nutrition(FoodConstants.MAX_FOOD)
        .saturationModifier(0.5f)
        .alwaysEdible()
        .build()
}
