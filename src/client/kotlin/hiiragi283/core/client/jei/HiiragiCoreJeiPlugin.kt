package hiiragi283.core.client.jei

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.negate
import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper.addDisplayRecipes
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper.addLookupRecipes
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper.addRecipes
import hiiragi283.core.api.integration.jei.HTJeiWorkstationHelper
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.item.HTPotionBasedItem
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.client.jei.category.HCBrewingRecipeCategory
import hiiragi283.core.client.jei.category.HCChargingRecipeCategory
import hiiragi283.core.client.jei.category.HCCrushingRecipeCategory
import hiiragi283.core.client.jei.category.HCExplodingRecipeCategory
import hiiragi283.core.client.jei.category.HCForgingRecipeCategory
import hiiragi283.core.client.jei.category.HCMaterialPartCategory
import hiiragi283.core.client.jei.category.HCTankEmptyingRecipeCategory
import hiiragi283.core.client.jei.category.HCTankFillingRecipeCategory
import hiiragi283.core.client.jei.extension.HCEternalSmithingCategoryExtension
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.common.recipe.viewer.display.HCExplodingRecipeDisplay
import hiiragi283.core.common.recipe.viewer.display.HCRecipeDisplayFactories
import hiiragi283.core.impl.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.impl.recipe.HTBasicDoubleMultiOutputRecipe
import hiiragi283.core.impl.recipe.HTBasicSingleMultiOutputRecipe
import hiiragi283.core.impl.recipe.viewer.display.HTRecipeDisplayFactories
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCDataComponents
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
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.DataComponentFluidIngredient
import kotlin.streams.asSequence

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
            HCChargingRecipeCategory(guiHelper),
            HCExplodingRecipeCategory(guiHelper),
            HCForgingRecipeCategory(guiHelper),
            // Tank Interaction
            HCTankEmptyingRecipeCategory(guiHelper),
            HCTankFillingRecipeCategory(guiHelper),
        )
    }

    override fun registerVanillaCategoryExtensions(registration: IVanillaCategoryExtensionRegistration) {
        registration.smithingCategory.addExtension(
            HCEternalSmithingRecipe::class.java,
            HCEternalSmithingCategoryExtension(registration.jeiHelpers.ingredientManager),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        addLookupRecipes(registration, HCRecipeViewerTypes.BREWING, HTVanillaRecipeTypes.BREWING)
        addDisplayRecipes(registration, HCRecipeViewerTypes.CHARGING, HCRecipeLookups.CHARGING, HCRecipeDisplayFactories::charging)
        addDisplayRecipes(registration, HCRecipeViewerTypes.CRUSHING, HCRecipeLookups.CRUSHING) {
            it.castRecipe<HTBasicSingleMultiOutputRecipe>()?.let(HTRecipeDisplayFactories::singleMultiItem)
        }
        addDisplayRecipes(registration, HCRecipeViewerTypes.EXPLODING, HCRecipeLookups.EXPLODING, HCExplodingRecipeDisplay::fromHolder)
        addDisplayRecipes(registration, HCRecipeViewerTypes.FORGING, HCRecipeLookups.FORGING) {
            it.castRecipe<HTBasicDoubleMultiOutputRecipe>()?.let(HTRecipeDisplayFactories::doubleMultiItem)
        }

        registerTankEmptying(registration)
        registerTankFilling(registration)

        addRecipes(
            registration,
            HCRecipeViewerTypes.MaterialType,
            HTMaterialManager.getInstance().entries.asSequence(),
        )
    }

    private fun registerTankEmptying(registration: IRecipeRegistration) {
        addDisplayRecipes(registration, HCRecipeViewerTypes.EMPTYING, HCRecipeLookups.EMPTYING) {
            it.castRecipe<HCTankEmptyingRecipe>()?.let(HCRecipeDisplayFactories::emptyingTank)
        }
        // Potion Bottle
        addRecipes(
            registration,
            HCRecipeViewerTypes.EMPTYING,
            BuiltInRegistries.POTION
                .holders()
                .asSequence()
                .map { potion: Holder<Potion> ->
                    val contents = BottledPotionContents(potion)
                    HTRecipeDisplay.Simple(
                        potion.toLike().getId().withPath { "/${HTConst.EMPTYING}/potion/$it" },
                        HTRecipeContents.create {
                            addInput(HTPotionHelper.createPotion(contents))
                            addOutput(HCPotionFluidHelper.createFluid(contents, 250))
                            addInput(ItemStack(Items.GLASS_BOTTLE))
                        },
                    )
                },
        )
    }

    private fun registerTankFilling(registration: IRecipeRegistration) {
        addDisplayRecipes(registration, HCRecipeViewerTypes.FILLING, HCRecipeLookups.FILLING) {
            it.castRecipe<HCTankFillingRecipe>()?.let(HCRecipeDisplayFactories::fillingTank)
        }
        // Potion Bottle
        registerPotionFilling(registration, "potion", Items.GLASS_BOTTLE, Items.POTION)
        // Potion Arrow
        registerPotionFilling(registration, "arrow", Items.ARROW, Items.TIPPED_ARROW, 125, HTBottleType.LINGERING)
    }

    private fun registerPotionFilling(
        registration: IRecipeRegistration,
        prefix: String,
        input: ItemLike,
        output: ItemLike,
        amount: Int = 250,
        bottleType: HTBottleType = HTBottleType.DEFAULT,
    ) {
        addRecipes(
            registration,
            HCRecipeViewerTypes.FILLING,
            BuiltInRegistries.POTION
                .holders()
                .asSequence()
                .map { potion: Holder<Potion> ->
                    HTRecipeDisplay.Simple(
                        potion.toLike().getId().withPath { "/${HTConst.FILLING}/$prefix/$it" },
                        HTRecipeContents.create {
                            addInput(ItemStack(input))
                            addInput(
                                DataComponentFluidIngredient.of(
                                    false,
                                    HCPotionFluidHelper.createFluid(BottledPotionContents(potion, bottleType)),
                                ),
                            )
                            addOutput(HTPotionHelper.createPotion(output, potion))
                        },
                    )
                },
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        HTJeiWorkstationHelper.addFromViewerType(
            registration,
            HCRecipeViewerTypes.BREWING,
            HCRecipeViewerTypes.CHARGING,
            HCRecipeViewerTypes.CRUSHING,
            HCRecipeViewerTypes.EXPLODING,
            HCRecipeViewerTypes.FORGING,
        )

        val copperBasins: List<ItemStack> = HCBlocks.COPPER_BASINS.allBlocks
            .map(HTBlockHolderLike<*>::get)
            .map(::ItemStack)
            .toList()
        HTJeiWorkstationHelper.add(registration, HCRecipeViewerTypes.EMPTYING, copperBasins)
        HTJeiWorkstationHelper.add(registration, HCRecipeViewerTypes.FILLING, copperBasins)
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGuiContainerHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
        registration.addGhostIngredientHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
    }
}
