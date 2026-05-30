package hiiragi283.core.common.integration.ae2

import appeng.recipes.handlers.ChargerRecipe
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.NeoForge

data object HCAEIntegration : HTRecipeProviderContext.Delegated() {
    //    Setup    //

    @JvmStatic
    internal fun init(eventBus: IEventBus) {
        NeoForge.EVENT_BUS.addListener(::registerRuntimeRecipe)
    }

    //    Game    //

    override lateinit var delegated: HTRecipeProviderContext

    @JvmStatic
    private fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        this.delegated = event.context
        // Convert HC Charging recipes into AE2 Charger recipes
        for ((id: ResourceLocation, recipe: HCChargingRecipe) in event.getAllRecipes(HCRecipeLookups.CHARGING)) {
            recipe.result
                .create(true)
                .map { result: ItemStack -> ChargerRecipe(recipe.ingredient, result) }
                .onRight { recipe: ChargerRecipe -> output.accept(id.withPrefix("charger/"), recipe, null) }
        }
    }
}
