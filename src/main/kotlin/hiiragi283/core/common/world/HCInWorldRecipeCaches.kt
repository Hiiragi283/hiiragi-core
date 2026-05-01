package hiiragi283.core.common.world

import hiiragi283.core.api.recipe.base.HTCrushingRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeCaches
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups

class HCInWorldRecipeCaches {
    val crushing: HTRecipeCaches.SingleItem<HTCrushingRecipe> =
        HTRecipeCaches.SingleItem(HCRecipeLookups.CRUSHING)
    val charging: HTRecipeCaches.SingleItem<HCChargingRecipe> =
        HTRecipeCaches.SingleItem(HCRecipeLookups.CHARGING)
    val exploding: HTRecipeCaches.SingleItem<HCExplodingRecipe> =
        HTRecipeCaches.SingleItem(HCRecipeLookups.EXPLODING)
}
