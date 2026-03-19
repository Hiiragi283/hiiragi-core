package hiiragi283.core.setup

import net.minecraft.world.food.FoodProperties

data object HCFoods {
    @JvmField
    val WARPED_WART: FoodProperties = FoodProperties.Builder().alwaysEdible().build()
}
