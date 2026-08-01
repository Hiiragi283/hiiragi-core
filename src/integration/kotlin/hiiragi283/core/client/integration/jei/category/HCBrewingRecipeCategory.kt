package hiiragi283.core.client.integration.jei.category

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.network.chat.FormattedText
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.alchemy.PotionContents

class HCBrewingRecipeCategory(guiHelper: IGuiHelper) : HTDisplayRecipeCategory.Progress(guiHelper, HCRecipeViewerTypes.BREWING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        contents.inputFluid(0) {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .addFluidStacks(it.stacks)
                .setSlotBackground(HTBackgroundType.EXTRA_INPUT, it.capacity)
                .addRichTooltipCallback(::addPotionTooltip)
        }
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        contents.outputFluid(0) {
            builder
                .addOutputSlot(getPosition(5), getPosition(0))
                .addFluidStack(it)
                .setSlotBackground(HTBackgroundType.OUTPUT, it.amount)
                .addRichTooltipCallback(::addPotionTooltip)
        }
    }

    private fun addPotionTooltip(view: IRecipeSlotView, builder: ITooltipBuilder) {
        view
            .getDisplayedIngredient(NeoForgeTypes.FLUID_STACK)
            .map(HTPotionHelper::getPotion)
            .ifPresent { contents: PotionContents ->
                buildList { contents.addPotionTooltip(this::add, 1f, 20f) }
                    .map { Either.left<FormattedText, TooltipComponent>(it) }
                    .let { builder.lines.addAll(1, it) }
            }
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe.progressData).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }
}
