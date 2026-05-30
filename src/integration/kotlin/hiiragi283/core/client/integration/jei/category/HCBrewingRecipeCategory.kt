package hiiragi283.core.client.integration.jei.category

import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.setup.HCRecipeViewerTypes
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.neoforged.neoforge.fluids.FluidType

class HCBrewingRecipeCategory(guiHelper: IGuiHelper) : HTHolderRecipeCategory<HCBrewingRecipe>(guiHelper, HCRecipeViewerTypes.BREWING, HCBrewingRecipe.CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCBrewingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.potionFrom.let(::HTPotionFluidIngredient).display())
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT, FluidType.BUCKET_VOLUME)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.potionTo.let(::BottledPotionContents).let(HCPotionFluidHelper::createFluid))
            .setSlotBackground(HTBackgroundType.OUTPUT, FluidType.BUCKET_VOLUME)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HCBrewingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe.progressData).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }
}
