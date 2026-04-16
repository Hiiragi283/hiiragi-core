package hiiragi283.core.api.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.util.Either
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

/**
 * 単純な[HTRecipeViewerType]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 * @see mekanism.client.recipe_viewer.type.SimpleRVRecipeType
 */
class HTSimpleRecipeViewerType<RECIPE : Any>(
    override val recipeClass: Class<out RECIPE>,
    idLike: HTIdLike,
    hasText: HTHasText,
    override val icon: Either<ResourceLocation, ItemStack>,
    override val bounds: HTBounds,
    override val workStations: List<ItemStack>,
) : HTRecipeViewerType<RECIPE>,
    HTIdLike by idLike,
    HTHasText by hasText {
    companion object {
        @JvmStatic
        inline fun <reified RECIPE : Any> create(
            idLike: HTIdLike,
            hasText: HTHasText,
            icon: ItemStack,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTSimpleRecipeViewerType<RECIPE> = create<RECIPE>(idLike, hasText, Either.Right(icon), bounds, *workStations)

        @JvmStatic
        inline fun <reified RECIPE : Any> create(
            idLike: HTIdLike,
            hasText: HTHasText,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTSimpleRecipeViewerType<RECIPE> = create<RECIPE>(idLike, hasText, icon, bounds, listOf(*workStations))

        @JvmStatic
        inline fun <reified RECIPE : Any> create(
            idLike: HTIdLike,
            hasText: HTHasText,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            workStations: List<ItemStack>,
        ): HTSimpleRecipeViewerType<RECIPE> = HTSimpleRecipeViewerType(
            RECIPE::class.java,
            idLike,
            hasText,
            icon,
            bounds,
            workStations,
        )
    }
}
