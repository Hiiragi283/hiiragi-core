package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.core.util.HCPotionFluidHelper
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.streams.asSequence

abstract class HTPotionBasedTankInteractionCategoryExtension<RECIPE : HTTankInteraction> : HTTankInteractionCategoryExtension<RECIPE> {
    final override fun <T : IIngredientAcceptor<T>> setFluid(recipe: RECIPE, accessor: T) {
        val stacks: List<FluidStack> = supportedBottles().flatMap {
            BuiltInRegistries.POTION
                .holders()
                .asSequence()
                .map(::BottledPotionContents)
                .map { HCPotionFluidHelper.createFluid(it, recipe.amount) }
        }
        accessor.addFluidStacks(stacks, false)
    }

    protected abstract fun supportedBottles(): Iterable<HTBottleType>

    override fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        emptySlot: IRecipeSlotDrawable,
        filledSlot: IRecipeSlotDrawable,
        fluidSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {
        val fluidStack: FluidStack = fluidSlot.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).getOrEmpty()
        if (fluidStack.isEmpty) return
        val contents: BottledPotionContents = HTPotionHelper.getContents(fluidStack) ?: return
        onDisplayedIngredientsUpdate(recipe, emptySlot, filledSlot, contents)
    }

    protected abstract fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        emptySlot: IRecipeSlotDrawable,
        filledSlot: IRecipeSlotDrawable,
        contents: BottledPotionContents,
    )
}
