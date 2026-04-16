package hiiragi283.core.api.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.util.Either
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

/**
 * [HTRecipeHolder]に基づいた[HTRecipeViewerType]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 * @see mekanism.client.recipe_viewer.type.FakeRVRecipeType
 */
class HTLookupRecipeViewerType<BASE : Any, RECIPE : BASE>(
    override val recipeClass: Class<out HTRecipeHolder<RECIPE>>,
    deferredType: HTRecipeType<*, out BASE>,
    override val icon: Either<ResourceLocation, ItemStack>,
    override val bounds: HTBounds,
    override val workStations: List<ItemStack>,
) : HTHolderRecipeViewerType<RECIPE>,
    HTIdLike by deferredType,
    HTHasText by deferredType {
    companion object {
        @JvmStatic
        fun <BASE : Any, RECIPE : BASE> create(
            deferredType: HTRecipeType<*, out BASE>,
            icon: ItemStack,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTLookupRecipeViewerType<BASE, RECIPE> = create(deferredType, Either.Right(icon), bounds, *workStations)

        @JvmStatic
        fun <BASE : Any, RECIPE : BASE> create(
            deferredType: HTRecipeType<*, out BASE>,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTLookupRecipeViewerType<BASE, RECIPE> = create(deferredType, icon, bounds, listOfNotNull(icon.getRight(), *workStations))

        @JvmStatic
        inline fun <BASE : Any, RECIPE : BASE, reified HOLDER : HTRecipeHolder<RECIPE>> create(
            deferredType: HTRecipeType<*, out BASE>,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            workstations: List<ItemStack>,
        ): HTLookupRecipeViewerType<BASE, RECIPE> = HTLookupRecipeViewerType(
            HOLDER::class.java,
            deferredType,
            icon,
            bounds,
            workstations,
        )
    }

    override fun toString(): String = "HTFakeRecipeViewerType(class=${recipeClass.canonicalName})"
}
