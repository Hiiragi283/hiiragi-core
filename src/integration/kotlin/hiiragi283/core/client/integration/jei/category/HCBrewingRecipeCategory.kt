package hiiragi283.core.client.integration.jei.category

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.util.DFUEither
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.util.HCPotionFluidHelper
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.core.Holder
import net.minecraft.network.chat.FormattedText
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents

class HCBrewingRecipeCategory(guiHelper: IGuiHelper) : HTHolderRecipeCategory<HCBrewingRecipe>(guiHelper, HCRecipeViewerTypes.BREWING, HCBrewingRecipe.CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCBrewingRecipe, focuses: IFocusGroup) {
        // inputs
        val potionFrom: Holder<Potion> = recipe.potionFrom
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidStacks(
                recipe.potionFrom
                    .let(::HTPotionFluidIngredient)
                    .stacks
                    .toList(),
            ).setSlotBackground(HTBackgroundType.EXTRA_INPUT, HTConst.DEFAULT_FLUID_AMOUNT)
            .addRichTooltipCallback(::addPotionTooltip)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addIngredients(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addFluidStack(recipe.potionTo.let(::BottledPotionContents).let(HCPotionFluidHelper::createFluid))
            .setSlotBackground(HTBackgroundType.OUTPUT, HTConst.DEFAULT_FLUID_AMOUNT)
            .addRichTooltipCallback(::addPotionTooltip)
    }

    private fun addPotionTooltip(view: IRecipeSlotView, builder: ITooltipBuilder) {
        view
            .getDisplayedIngredient(NeoForgeTypes.FLUID_STACK)
            .map(HTPotionHelper::getPotion)
            .ifPresent { contents: PotionContents ->
                buildList { contents.addPotionTooltip(this::add, 1f, 20f) }
                    .map { DFUEither.left<FormattedText, TooltipComponent>(it) }
                    .let { builder.lines.addAll(1, it) }
            }
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HCBrewingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe.progressData).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }
}
