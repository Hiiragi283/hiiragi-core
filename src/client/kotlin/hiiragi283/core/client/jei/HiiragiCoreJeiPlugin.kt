package hiiragi283.core.client.jei

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.negate
import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.integration.jei.JeiRecipeType
import hiiragi283.core.api.item.HTPotionBasedItem
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.client.jei.category.HCBrewingRecipeCategory
import hiiragi283.core.client.jei.category.HCCrushingRecipeCategory
import hiiragi283.core.client.jei.category.HCExplodingRecipeCategory
import hiiragi283.core.client.jei.category.HCForgingRecipeCategory
import hiiragi283.core.client.jei.category.HCMaterialPartCategory
import hiiragi283.core.client.jei.category.HCMeltingRecipeCategory
import hiiragi283.core.client.jei.category.HTSingleItemRecipeCategory
import hiiragi283.core.client.jei.extension.HCEternalSmithingCategoryExtension
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.impl.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.util.HCPotionFluidHelper
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.VanillaTypes
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
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JeiPlugin
class HiiragiCoreJeiPlugin : HTJeiPlugin(HiiragiCoreAPI.MOD_ID) {
    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        registration.registerSubtypeInterpreter(
            HCItems.BLUEPRINT.get(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(HCDataComponents.BLUEPRINT_NUMBER) },
        )
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

        registration.addRecipeCategories(
            // Material
            HCMaterialPartCategory(guiHelper),
            // Recipes
            HCBrewingRecipeCategory(guiHelper),
            HCCrushingRecipeCategory(guiHelper),
            HTSingleItemRecipeCategory(guiHelper, HCRecipeViewerTypes.CHARGING, HCRecipeSerializers.CHARGING),
            HCExplodingRecipeCategory(guiHelper),
            HCForgingRecipeCategory(guiHelper),
            HCMeltingRecipeCategory(guiHelper),
        )
    }

    override fun registerVanillaCategoryExtensions(registration: IVanillaCategoryExtensionRegistration) {
        registration.smithingCategory.addExtension(
            HCEternalSmithingRecipe::class.java,
            HCEternalSmithingCategoryExtension(registration.jeiHelpers.ingredientManager),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(HCRecipeViewerTypes.BREWING, HTVanillaRecipeTypes.BREWING)
        registration.addRecipes(HCRecipeViewerTypes.CHARGING, HCRecipeLookups.CHARGING)
        registration.addRecipes(HCRecipeViewerTypes.CRUSHING, HCRecipeLookups.CRUSHING)
        registration.addRecipes(HCRecipeViewerTypes.EXPLODING, HCRecipeLookups.EXPLODING)
        registration.addRecipes(HCRecipeViewerTypes.FORGING, HCRecipeLookups.FORGING)
        registration.addRecipes(HCRecipeViewerTypes.MELTING, HCRecipeLookups.MELTING)

        registerTankEmptying(registration)
        registerTankFilling(registration)

        registration.addRecipes(
            getRecipeType(HCRecipeViewerTypes.MaterialType),
            HTMaterialManager.getInstance().entries.asSequence(),
        )
    }

    private fun registerTankEmptying(registration: IRecipeRegistration) {}

    private fun registerTankFilling(registration: IRecipeRegistration) {}

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalysts(
            HCRecipeViewerTypes.BREWING,
            HCRecipeViewerTypes.CHARGING,
            HCRecipeViewerTypes.CRUSHING,
            HCRecipeViewerTypes.EXPLODING,
            HCRecipeViewerTypes.FORGING,
            HCRecipeViewerTypes.MELTING,
        )

        arrayOf(HCRecipeViewerTypes.EMPTYING, HCRecipeViewerTypes.FILLING)
            .map { getRecipeType(it) }
            .forEach { recipeType: JeiRecipeType<*> ->
                registration.addRecipeCatalysts(
                    recipeType,
                    VanillaTypes.ITEM_STACK,
                    HCBlocks.COPPER_BASINS.allBlocks
                        .map(HTBlockHolderLike<*>::get)
                        .map(::ItemStack)
                        .toList(),
                )
            }
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGuiContainerHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
        registration.addGhostIngredientHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
    }
}
