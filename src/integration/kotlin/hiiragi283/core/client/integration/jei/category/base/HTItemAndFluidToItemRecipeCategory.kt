package hiiragi283.core.client.integration.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.ItemStack

class HTItemAndFluidToItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<HTProgressRecipeDisplay>) : HTDisplayRecipeCategory.Progress(guiHelper, recipeType) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        val inputItem: List<ItemStack> = contents.inputItem(0)
        if (inputItem.isEmpty()) {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .addItemStacks(contents.catalyst(0))
                .setSlotBackground(HTBackgroundType.NONE)
        } else {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .addItemStacks(inputItem)
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        contents.inputFluid(0) {
            builder
                .addInputSlot(getPosition(2), getPosition(0))
                .addFluidStacks(it.stacks)
                .setSlotBackground(HTBackgroundType.EXTRA_INPUT, it.capacity)
        }
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
