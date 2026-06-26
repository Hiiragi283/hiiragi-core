package hiiragi283.core.client.integration.jei

import hiiragi283.core.api.HCConstants
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.client.integration.jei.category.HCBrewingRecipeCategory
import hiiragi283.core.client.integration.jei.category.HCChargingRecipeCategory
import hiiragi283.core.client.integration.jei.category.HCExplodingRecipeCategory
import hiiragi283.core.client.integration.jei.category.HCTankEmptyingRecipeCategory
import hiiragi283.core.client.integration.jei.category.HCTankFillingRecipeCategory
import hiiragi283.core.client.integration.jei.category.HTItemToChancedItemsRecipeCategory
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.common.recipe.custom.HCEternalSmithingRecipe
import hiiragi283.core.common.recipe.viewer.display.HCRecipeDisplayFactories
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.core.setup.HCRecipeViewerTypes
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.HTJeiWorkstationHelper
import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.viewer.display.HTRecipeContents
import hiiragi283.lib.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.lib.recipe.viewer.display.HTRecipeDisplayFactories
import hiiragi283.lib.registry.toLike
import kotlin.streams.asSequence
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IPlatformFluidHelper
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.registration.IExtraIngredientRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.DataComponentFluidIngredient

@JeiPlugin
class HiiragiCoreJeiPlugin : HTJeiPlugin(HiiragiCoreAPI.MOD_ID) {
    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        // Potion-Based Item
        HTPhysicalSideHelper
            .filteredLookup(Registries.ITEM)
            .getOrNull()
            ?.listElements()
            ?.map(Holder<Item>::value)
            ?.forEach { item: Item ->
                if (item is HTPotionBasedItem) {
                    registration.registerSubtypeInterpreter(item) { stack: ItemStack, _ -> HTPotionHelper.getContents(stack) }
                }
            }
    }

    override fun <T : Any> registerFluidSubtypes(registration: ISubtypeRegistration, platformFluidHelper: IPlatformFluidHelper<T>) {
        registration.registerSubtypeInterpreter(platformFluidHelper.fluidIngredientType, HCFluids.POTION.get()) { stack: T, _ -> (stack as? FluidStack)?.let(HTPotionHelper::getContents) }
    }

    override fun registerExtraIngredients(registration: IExtraIngredientRegistration) {
        HTPhysicalSideHelper
            .filteredLookup(Registries.POTION)
            .getOrNull()
            ?.listElements()
            ?.map(::BottledPotionContents)
            ?.filter { !it.isWater }
            ?.map(HCPotionFluidHelper::createFluid)
            ?.toList()
            ?.let { registration.addExtraIngredients(NeoForgeTypes.FLUID_STACK, it) }
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(
            // Recipes
            HCBrewingRecipeCategory(guiHelper),
            HTItemToChancedItemsRecipeCategory(guiHelper, HCRecipeViewerTypes.CHOPPING),
            HTItemToChancedItemsRecipeCategory(guiHelper, HCRecipeViewerTypes.CRUSHING),
            HCChargingRecipeCategory(guiHelper),
            HCExplodingRecipeCategory(guiHelper),
            // Tank Interaction
            HCTankEmptyingRecipeCategory(guiHelper),
            HCTankFillingRecipeCategory(guiHelper),
        )
    }

    override fun registerVanillaCategoryExtensions(registration: IVanillaCategoryExtensionRegistration) {
        val manager: IIngredientManager = registration.jeiHelpers.ingredientManager
        registration.smithingCategory.addExtension(HCEternalSmithingRecipe::class.java, HCEternalSmithingCategoryExtension(manager))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val helper = HTJeiRecipeHelper(registration)

        helper.addLookupRecipes(HCRecipeViewerTypes.BREWING, HTVanillaRecipeTypes.BREWING, HCBrewingRecipe.SORTER)
        helper.addDisplayRecipes(HCRecipeViewerTypes.CHARGING, HCRecipeTypes.CHARGING, HCRecipeDisplayFactories::charging)
        helper.addDisplayRecipes(HCRecipeViewerTypes.CHOPPING, HCRecipeTypes.CHOPPING, HTRecipeDisplayFactories::itemToChancedItems)
        helper.addDisplayRecipes(HCRecipeViewerTypes.CRUSHING, HCRecipeTypes.CRUSHING, HTRecipeDisplayFactories::itemToChancedItems)
        helper.addDisplayRecipes(HCRecipeViewerTypes.EXPLODING, HCRecipeTypes.EXPLODING, HCRecipeDisplayFactories::inWorld)

        registerTankEmptying(helper)
        registerTankFilling(helper)
    }

    private fun registerTankEmptying(helper: HTJeiRecipeHelper) {
        helper.addDisplayRecipes(HCRecipeViewerTypes.EMPTYING, HCRecipeTypes.EMPTYING, HCRecipeDisplayFactories::emptyingTank)
        // Potion Bottle
        helper.addRecipes(
            HCRecipeViewerTypes.EMPTYING,
            getPotionHolders()
                .map { potion: Holder<Potion> ->
                    val contents = BottledPotionContents(potion)
                    HTRecipeDisplay.Simple(
                        potion.toLike().getId().withPath { "/${HCConstants.EMPTYING}/potion/$it" },
                        HTRecipeContents.create {
                            HTPotionHelper.createPotion(contents).let(ItemStackTemplate::create).let(::addInput)
                            addOutput(HCPotionFluidHelper.createFluid(contents, 250))
                            addOutput(ItemStack(Items.GLASS_BOTTLE))
                        },
                    )
                },
        )
    }

    private fun registerTankFilling(helper: HTJeiRecipeHelper) {
        helper.addDisplayRecipes(HCRecipeViewerTypes.FILLING, HCRecipeTypes.FILLING, HCRecipeDisplayFactories::fillingTank)
        // Potion Bottle
        registerPotionFilling(helper, "potion", Items.GLASS_BOTTLE, Items.POTION)
        // Potion Arrow
        registerPotionFilling(helper, "arrow", Items.ARROW, Items.TIPPED_ARROW, 125, HTBottleType.LINGERING)
    }

    private fun registerPotionFilling(
        helper: HTJeiRecipeHelper,
        prefix: String,
        input: ItemLike,
        output: ItemLike,
        amount: Int = 250,
        bottleType: HTBottleType = HTBottleType.DEFAULT,
    ) {
        helper.addRecipes(
            HCRecipeViewerTypes.FILLING,
            getPotionHolders()
                .map { potion: Holder<Potion> ->
                    HTRecipeDisplay.Simple(
                        potion.toLike().getId().withPath { "/${HCConstants.FILLING}/$prefix/$it" },
                        HTRecipeContents.create {
                            addInput(ItemStack(input))
                            addInput(
                                HTFluidIngredient(
                                    DataComponentFluidIngredient.of(
                                        false,
                                        HCPotionFluidHelper.createFluid(BottledPotionContents(potion, bottleType)),
                                    ),
                                    amount,
                                ),
                            )
                            HTPotionHelper.createPotion(output, potion).onRight(::addOutput)
                        },
                    )
                },
        )
    }

    private fun getPotionHolders(): Sequence<Holder<Potion>> = HTPhysicalSideHelper
        .filteredLookup(Registries.POTION)
        .map { it.listElements() }
        .fold({ emptySequence() }, { it.asSequence() })

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        val helper = HTJeiWorkstationHelper(registration)

        helper.addFromViewerType(
            HCRecipeViewerTypes.BREWING,
            HCRecipeViewerTypes.CHARGING,
            HCRecipeViewerTypes.CHOPPING,
            HCRecipeViewerTypes.CRUSHING,
            HCRecipeViewerTypes.EXPLODING,
        )

        val copperBasins: List<ItemStack> = HCBlocks.COPPER_BASIN.allCoppers.map { it.toStack() }
        helper.add(HCRecipeViewerTypes.EMPTYING, copperBasins)
        helper.add(HCRecipeViewerTypes.FILLING, copperBasins)
    }
}
