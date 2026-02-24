package hiiragi283.core.client.jei.category

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.resource.IdToValue
import hiiragi283.core.client.jei.HCJeiRecipeTypes
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

class HCBrewingRecipeCategory(guiHelper: IGuiHelper) : HTFakeRecipeCategory<HCBrewingRecipe>(guiHelper, HCJeiRecipeTypes.BREWING) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: IdToValue<HCBrewingRecipe>, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.second.time).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlusSign().setPosition(getPosition(1) + 2, getPosition(0) + 2)
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCBrewingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidIngredient(false, recipe.potionFrom)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
            .addRichTooltipCallback(::addPotionTooltip)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addFluidResult(false, recipe.potionTo)
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
}
