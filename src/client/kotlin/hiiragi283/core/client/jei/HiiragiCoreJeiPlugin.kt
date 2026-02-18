package hiiragi283.core.client.jei

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.item.HTPotionBasedItem
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.common.crafting.HTEternalSmithingRecipe
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.registration.IGuiHandlerRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

@JeiPlugin
class HiiragiCoreJeiPlugin : HTJeiPlugin(HiiragiCoreAPI.MOD_ID) {
    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        registration.registerSubtypeInterpreter(
            HCItems.ALMIGHTY_PICKAXE.get(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(DataComponents.UNBREAKABLE) },
        )
        // Potion-Based Item
        for (item: Item in BuiltInRegistries.ITEM) {
            if (item is HTPotionBasedItem) {
                registration.registerSubtypeInterpreter(
                    item,
                    HTSubtypeInterpreter { stack: ItemStack, _ -> HTPotionHelper.getContents(stack) },
                )
            }
        }
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(HCAnvilCrushingRecipeCategory(guiHelper))
        registration.addRecipeCategories(HCSingleItemRecipeCategory.charging(guiHelper))
        registration.addRecipeCategories(HCSingleItemRecipeCategory.exploding(guiHelper))
    }

    override fun registerVanillaCategoryExtensions(registration: IVanillaCategoryExtensionRegistration) {
        registration.smithingCategory.addExtension(
            HTEternalSmithingRecipe::class.java,
            HTEternalSmithingCategoryExtension(registration.jeiHelpers.ingredientManager),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(HCJeiRecipeTypes.ANVIL_CRUSHING, HCRecipeTypes.ANVIL_CRUSHING.get()) { it.result.getId() }
        registration.addRecipes(HCJeiRecipeTypes.CHARGING, HCRecipeTypes.CHARGING.get()) { it.result.getId() }
        registration.addRecipes(HCJeiRecipeTypes.EXPLODING, HCRecipeTypes.EXPLODING.get()) { it.result.getId() }

        // Eternal Upgrade
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalysts(
            HCJeiRecipeTypes.ANVIL_CRUSHING,
            HCJeiRecipeTypes.CHARGING,
            HCJeiRecipeTypes.EXPLODING,
        )
    }

    companion object {
        @JvmStatic
        private fun getWidgets(screen: Screen): List<HTWidgetContainerScreen.WidgetWrapper<*>> = screen
            .children()
            .filterIsInstance<HTWidgetContainerScreen.WidgetWrapper<*>>()
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGhostIngredientHandler(HTWidgetContainerScreen::class.java, HTGhostIngredientHandler)
    }
}
