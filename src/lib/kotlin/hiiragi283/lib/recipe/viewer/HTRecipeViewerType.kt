package hiiragi283.lib.recipe.viewer

import com.mojang.datafixers.util.Either
import hiiragi283.lib.math.HTBounds
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.HTHasText
import net.minecraft.resources.Identifier
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
 */
interface HTRecipeViewerType<RECIPE : Any> :
    HTHasText,
    HTIdLike {
    val recipeClass: Class<out RECIPE>
    val icon: Either<Identifier, ItemStack>
    val bounds: HTBounds
    val workStations: List<ItemStack>
}
