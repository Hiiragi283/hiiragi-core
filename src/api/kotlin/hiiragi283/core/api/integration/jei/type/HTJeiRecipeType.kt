package hiiragi283.core.api.integration.jei.type

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

/**
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType
 */
interface HTJeiRecipeType<RECIPE : Any> :
    HTHasText,
    HTIdLike {
    val recipeClass: Class<out RECIPE>
    val icon: Either<ResourceLocation, ItemStack>
    val bounds: HTBounds
    val workStations: List<ItemStack>
}
