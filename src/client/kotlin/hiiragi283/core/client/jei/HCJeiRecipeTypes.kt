package hiiragi283.core.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.integration.jei.HTJeiHolderRecipeType
import hiiragi283.core.api.integration.jei.HTJeiRecipeType
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike

object HCJeiRecipeTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int = 18 * 4,
        height: Int = 18 * 1,
    ): HTJeiHolderRecipeType<RECIPE> = HTJeiRecipeType.createRecipe(recipeType, recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    @JvmField
    val ANVIL_CRUSHING: HTJeiHolderRecipeType<HCAnvilCrushingRecipe> = create(HCRecipeTypes.ANVIL_CRUSHING, Items.ANVIL, 18 * 5)

    @JvmField
    val CHARGING: HTJeiHolderRecipeType<HCLightningChargingRecipe> = create(HCRecipeTypes.CHARGING, Items.LIGHTNING_ROD)

    @JvmField
    val EXPLODING: HTJeiHolderRecipeType<HCExplodingRecipe> = create(HCRecipeTypes.EXPLODING, Items.TNT)
}
