package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.resource.IdToValue
import hiiragi283.core.common.event.HCRecipeEventHandler
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHARGING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe.Serializable> = REGISTER.registerType(HTConst.CHARGING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> = REGISTER.registerType(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTDeferredRecipeType<HCExplodingRecipe.Input, HCExplodingRecipe> = REGISTER.registerType(HTConst.EXPLODING)

    @JvmField
    val MELTING: HTDeferredRecipeType<HCMeltingRecipe.Input, HCMeltingRecipe> = REGISTER.registerType(HTConst.MELTING)

    @JvmField
    val TANK_INTERACTION: HTRecipeType.Fake<RecipeInput, HTTankInteraction> = object : HTRecipeType.Fake<RecipeInput, HTTankInteraction> {
        override fun getId(): ResourceLocation = HiiragiCoreAPI.id(HTConst.TANK_INTERACTION)

        override fun createCache(): HTRecipeCache<RecipeInput, HTTankInteraction> = throw UnsupportedOperationException()

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<IdToValue<HTTankInteraction>> =
            HCRecipeEventHandler.tankInteractionMap
                .asSequence()
                .map { (key: ResourceLocation, value: HTTankInteraction.Serializable) -> key to value }
    }
}
