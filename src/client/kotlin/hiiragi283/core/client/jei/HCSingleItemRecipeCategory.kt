package hiiragi283.core.client.jei

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.HTHolderRecipeCategory
import hiiragi283.core.api.integration.jei.HTJeiRecipeType
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class HCSingleItemRecipeCategory<RECIPE : HCSingleItemRecipe<*>>(
    guiHelper: IGuiHelper,
    recipeType: HTJeiRecipeType<RecipeHolder<RECIPE>>,
    private val iconFactory: (RECIPE) -> ItemStack,
) : HTHolderRecipeCategory<RECIPE>(guiHelper, recipeType) {
    companion object {
        @JvmStatic
        fun charging(guiHelper: IGuiHelper): HCSingleItemRecipeCategory<HCLightningChargingRecipe> =
            HCSingleItemRecipeCategory(guiHelper, HCJeiRecipeTypes.CHARGING) { ItemStack(Items.LIGHTNING_ROD) }

        @JvmStatic
        fun exploding(guiHelper: IGuiHelper): HCSingleItemRecipeCategory<HCExplodingRecipe> =
            HCSingleItemRecipeCategory(guiHelper, HCJeiRecipeTypes.EXPLODING) { recipe: HCExplodingRecipe ->
                HCExplodingRecipe.createIcon(recipe.minPower.toFloat())
            }
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder
            .addItemSlot(RecipeIngredientRole.RENDER_ONLY, getPosition(1.5), getPosition(0), listOf(recipe).map(iconFactory))

        // input
        builder
            .addItemSlot(getPosition(0), getPosition(1), recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addItemSlot(getPosition(3), getPosition(1), recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(1))
    }
}
