package hiiragi283.core.api.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.util.Either
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [HTRecipeHolder]に基づいた[HTRecipeViewerType]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 * @see mekanism.client.recipe_viewer.type.FakeRVRecipeType
 */
class HTLookupRecipeViewerType<INPUT : RecipeInput, RECIPE : Any>(
    override val recipeClass: Class<out HTRecipeHolder<RECIPE>>,
    deferredType: HTRecipeType<INPUT, RECIPE>,
    override val icon: Either<ResourceLocation, ItemStack>,
    override val bounds: HTBounds,
    override val workStations: List<ItemStack>,
) : HTRecipeViewerType<HTRecipeHolder<RECIPE>>,
    HTRecipeType<INPUT, RECIPE> by deferredType {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Any> create(
            deferredType: HTRecipeType<INPUT, RECIPE>,
            icon: ItemStack,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTLookupRecipeViewerType<INPUT, RECIPE> = create(deferredType, Either.Right(icon), bounds, *workStations)

        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Any> create(
            deferredType: HTRecipeType<INPUT, RECIPE>,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTLookupRecipeViewerType<INPUT, RECIPE> = create(deferredType, icon, bounds, listOfNotNull(icon.getRight(), *workStations))

        @JvmStatic
        inline fun <INPUT : RecipeInput, RECIPE : Any, reified HOLDER : HTRecipeHolder<RECIPE>> create(
            deferredType: HTRecipeType<INPUT, RECIPE>,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            workstations: List<ItemStack>,
        ): HTLookupRecipeViewerType<INPUT, RECIPE> = HTLookupRecipeViewerType(
            HOLDER::class.java,
            deferredType,
            icon,
            bounds,
            workstations,
        )
    }

    override fun toString(): String = "HTFakeRecipeViewerType(class=${recipeClass.canonicalName})"
}
