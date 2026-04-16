package hiiragi283.core.api.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.util.Either
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

/**
 * [HTRecipeHolder]に基づいた[HTRecipeViewerType]の型エイリアスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
typealias HTHolderRecipeViewerType<RECIPE> = HTRecipeViewerType<HTRecipeHolder<RECIPE>>

/**
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType
 */
interface HTRecipeViewerType<RECIPE : Any> :
    HTHasText,
    HTIdLike {
    val recipeClass: Class<out RECIPE>
    val icon: Either<ResourceLocation, ItemStack>
    val bounds: HTBounds
    val workStations: List<ItemStack>
}
