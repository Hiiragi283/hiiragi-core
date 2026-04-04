package hiiragi283.core.client.integration.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.viewer.HTFakeRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.recipe.VanillaRecipeTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike

object HCJeiRecipeTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
        recipeType: HTRecipeType.Managed<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTHolderRecipeViewerType<INPUT, RECIPE> =
        HTHolderRecipeViewerType.create(recipeType, ItemStackTemplate(icon.asItem()), HTBounds(0, 0, width, height))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(
        recipeType: HTRecipeType.Fake<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTFakeRecipeViewerType<INPUT, RECIPE> =
        HTFakeRecipeViewerType.create(recipeType, ItemStackTemplate(icon.asItem()), HTBounds(0, 0, width, height))

    @JvmField
    val BREWING: HTFakeRecipeViewerType<HTItemAndFluidRecipeInput, HCBrewingRecipe> =
        create(VanillaRecipeTypes.BREWING, Items.BREWING_STAND, 18 * 6)

    @JvmField
    val CHARGING: HTHolderRecipeViewerType<SingleRecipeInput, HCChargingRecipe> =
        create(HCRecipeTypes.CHARGING, Items.LIGHTNING_ROD, 18 * 4)

    @JvmField
    val MELTING: HTHolderRecipeViewerType<HCMeltingRecipe.Input, HCMeltingRecipe> =
        create(HCRecipeTypes.MELTING, HCBlocks.CRUCIBLE, 18 * 4)
}
