package hiiragi283.core.client.jei

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.setup.HCRecipeTypes
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration

@JeiPlugin
class HiiragiCoreJeiPlugin : HTJeiPlugin(HiiragiCoreAPI.MOD_ID) {
    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(HCAnvilCrushingRecipeCategory(guiHelper))
        registration.addRecipeCategories(HCSingleItemRecipeCategory.charging(guiHelper))
        registration.addRecipeCategories(HCSingleItemRecipeCategory.exploding(guiHelper))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(HCJeiRecipeTypes.ANVIL_CRUSHING, HCRecipeTypes.ANVIL_CRUSHING.get()) { it.result.getId() }
        registration.addRecipes(HCJeiRecipeTypes.CHARGING, HCRecipeTypes.CHARGING.get()) { it.result.getId() }
        registration.addRecipes(HCJeiRecipeTypes.EXPLODING, HCRecipeTypes.EXPLODING.get()) { it.result.getId() }
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalysts(
            HCJeiRecipeTypes.ANVIL_CRUSHING,
            HCJeiRecipeTypes.CHARGING,
            HCJeiRecipeTypes.EXPLODING,
        )
    }
}
