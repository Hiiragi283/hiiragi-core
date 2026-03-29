package hiiragi283.core.api.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.FakeRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.util.Either
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [FakeRecipeHolder]に基づいた[HTRecipeViewerType]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 * @see mekanism.client.recipe_viewer.type.FakeRVRecipeType
 */
class HTFakeRecipeViewerType<INPUT : RecipeInput, RECIPE : Any>(
    override val recipeClass: Class<out FakeRecipeHolder<RECIPE>>,
    deferredType: HTRecipeType.Fake<INPUT, RECIPE>,
    override val icon: Either<Identifier, ItemStackTemplate>,
    override val bounds: HTBounds,
    override val workStations: List<ItemStackTemplate>,
) : HTRecipeViewerType<FakeRecipeHolder<RECIPE>>,
    HTRecipeType.Fake<INPUT, RECIPE> by deferredType {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Any> create(
            deferredType: HTRecipeType.Fake<INPUT, RECIPE>,
            icon: ItemStackTemplate,
            bounds: HTBounds,
            vararg workStations: ItemStackTemplate,
        ): HTFakeRecipeViewerType<INPUT, RECIPE> = create(deferredType, Either.Right(icon), bounds, *workStations)

        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Any> create(
            deferredType: HTRecipeType.Fake<INPUT, RECIPE>,
            icon: Either<Identifier, ItemStackTemplate>,
            bounds: HTBounds,
            vararg workStations: ItemStackTemplate,
        ): HTFakeRecipeViewerType<INPUT, RECIPE> = create(deferredType, icon, bounds, listOfNotNull(icon.getRight(), *workStations))

        @JvmStatic
        inline fun <INPUT : RecipeInput, RECIPE : Any, reified HOLDER : FakeRecipeHolder<RECIPE>> create(
            deferredType: HTRecipeType.Fake<INPUT, RECIPE>,
            icon: Either<Identifier, ItemStackTemplate>,
            bounds: HTBounds,
            workstations: List<ItemStackTemplate>,
        ): HTFakeRecipeViewerType<INPUT, RECIPE> = HTFakeRecipeViewerType(
            HOLDER::class.java,
            deferredType,
            icon,
            bounds,
            workstations,
        )
    }

    override fun toString(): String = "HTFakeRecipeViewerType(class=${recipeClass.canonicalName})"
}
