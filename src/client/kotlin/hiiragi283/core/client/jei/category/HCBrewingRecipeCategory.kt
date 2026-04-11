package hiiragi283.core.client.jei.category

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.core.common.recipe.HCBrewingRecipe
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

class HCBrewingRecipeCategory(guiHelper: IGuiHelper) : HTLookupRecipeCategory<HCBrewingRecipe>(guiHelper, HCJeiRecipeTypes.BREWING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCBrewingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidIngredient(recipe.potionFrom)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
            .addRichTooltipCallback(::addPotionTooltip)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addIngredients(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addFluidResult(recipe.potionTo)
            .setSlotBackground(HTBackgroundType.OUTPUT)
            .addRichTooltipCallback(::addPotionTooltip)
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

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HCBrewingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }
}
