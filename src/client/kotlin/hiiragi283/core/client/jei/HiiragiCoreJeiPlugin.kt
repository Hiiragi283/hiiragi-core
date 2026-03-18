package hiiragi283.core.client.jei

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.function.negate
import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.item.HTPotionBasedItem
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.client.jei.category.HCBrewingRecipeCategory
import hiiragi283.core.client.jei.category.HCExplodingRecipeCategory
import hiiragi283.core.client.jei.category.HCMaterialPartCategory
import hiiragi283.core.client.jei.category.HTItemToChancedRecipeCategory
import hiiragi283.core.client.jei.category.HTItemToItemRecipeCategory
import hiiragi283.core.client.jei.extension.HCEternalSmithingCategoryExtension
import hiiragi283.core.client.jei.extension.HTBasicItemToChancedRecipeCategoryExtension
import hiiragi283.core.client.jei.extension.HTBasicItemToItemRecipeCategoryExtension
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.util.HCPotionFluidHelper
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IPlatformFluidHelper
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.registration.IExtraIngredientRegistration
import mezz.jei.api.registration.IGuiHandlerRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JeiPlugin
class HiiragiCoreJeiPlugin : HTJeiPlugin(HiiragiCoreAPI.MOD_ID) {
    companion object {
        // ItemToItem
        @JvmStatic
        lateinit var charging: HTItemToItemRecipeCategory
            private set

        // ItemToChanced
        @JvmStatic
        lateinit var crushing: HTItemToChancedRecipeCategory
            private set
    }

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

    override fun <T : Any> registerFluidSubtypes(registration: ISubtypeRegistration, platformFluidHelper: IPlatformFluidHelper<T>) {
        registration.registerSubtypeInterpreter(
            platformFluidHelper.fluidIngredientType,
            HCFluids.POTION.get(),
            HTSubtypeInterpreter { stack: T, _ -> (stack as? FluidStack)?.let(HTPotionHelper::getContents) },
        )
    }

    override fun registerExtraIngredients(registration: IExtraIngredientRegistration) {
        registration.addExtraIngredients(
            NeoForgeTypes.FLUID_STACK,
            BuiltInRegistries.POTION
                .asLookup()
                .listElements()
                .map(::BottledPotionContents)
                .filter(BottledPotionContents::isWater.negate())
                .map(HCPotionFluidHelper::createFluid)
                .toList(),
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper
        val manager: IIngredientManager = registration.jeiHelpers.ingredientManager

        initItemToItem(guiHelper, manager)
        initItemToChanced(guiHelper, manager)

        registration.addRecipeCategories(
            // Material
            HCMaterialPartCategory(guiHelper),
            // Basic
            HCBrewingRecipeCategory(guiHelper),
            charging,
            crushing,
            HCExplodingRecipeCategory(guiHelper),
        )
    }

    private fun initItemToItem(guiHelper: IGuiHelper, manager: IIngredientManager) {
        charging = HTItemToItemRecipeCategory(guiHelper, HCJeiRecipeTypes.CHARGING)

        charging.addExtension(HTBasicItemToItemRecipeCategoryExtension())
    }

    private fun initItemToChanced(guiHelper: IGuiHelper, manager: IIngredientManager) {
        crushing = HTItemToChancedRecipeCategory(guiHelper, HCJeiRecipeTypes.CRUSHING)

        crushing.addExtension(HTBasicItemToChancedRecipeCategoryExtension())
    }

    override fun registerVanillaCategoryExtensions(registration: IVanillaCategoryExtensionRegistration) {
        registration.smithingCategory.addExtension(
            HCEternalSmithingRecipe::class.java,
            HCEternalSmithingCategoryExtension(registration.jeiHelpers.ingredientManager),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(
            getRecipeType(HCJeiRecipeTypes.MaterialType),
            HiiragiCoreAccess.INSTANCE.materialManager.entries
                .asSequence(),
        )

        registration.addRecipes(HCJeiRecipeTypes.BREWING)
        registration.addRecipes(HCJeiRecipeTypes.CHARGING)
        registration.addRecipes(HCJeiRecipeTypes.CRUSHING)
        registration.addRecipes(HCJeiRecipeTypes.EXPLODING, sorter = compareBy { it.result.getId() })
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalysts(
            HCJeiRecipeTypes.BREWING,
            HCJeiRecipeTypes.CHARGING,
            HCJeiRecipeTypes.CRUSHING,
            HCJeiRecipeTypes.EXPLODING,
        )
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGuiContainerHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
        registration.addGhostIngredientHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
    }
}
