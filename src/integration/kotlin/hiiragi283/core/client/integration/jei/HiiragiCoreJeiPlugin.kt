package hiiragi283.core.client.integration.jei

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.client.integration.jei.category.HCBrewingRecipeCategory
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCRecipeViewerTypes
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.HTJeiWorkstationHelper
import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
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
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

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
        registration.registerSubtypeInterpreter(
            platformFluidHelper.fluidIngredientType,
            HCFluids.POTION.get(),
            { stack: T, _ -> (stack as? FluidStack)?.let(HTPotionHelper::getContents) },
        )
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
            HCBrewingRecipeCategory(guiHelper),
        )
    }

    override fun registerVanillaCategoryExtensions(registration: IVanillaCategoryExtensionRegistration) {
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        HTJeiRecipeHelper.addLookupRecipes(registration, HCRecipeViewerTypes.BREWING, HTVanillaRecipeTypes.BREWING, HCBrewingRecipe.SORTER)
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        HTJeiWorkstationHelper.addFromViewerType(registration, HCRecipeViewerTypes.BREWING)
    }
}
