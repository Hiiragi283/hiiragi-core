package hiiragi283.core.api.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.util.Either
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [RecipeHolder]に基づいた[HTRecipeViewerType]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper
 */
class HTHolderRecipeViewerType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
    override val recipeClass: Class<out RecipeHolder<RECIPE>>,
    deferredType: HTRecipeType.Managed<INPUT, RECIPE>,
    override val icon: Either<Identifier, ItemStackTemplate>,
    override val bounds: HTBounds,
    override val workStations: List<ItemStackTemplate>,
) : HTRecipeViewerType<RecipeHolder<RECIPE>>,
    HTRecipeType.Managed<INPUT, RECIPE> by deferredType {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
            deferredType: HTRecipeType.Managed<INPUT, RECIPE>,
            icon: ItemStackTemplate,
            bounds: HTBounds,
            vararg workStations: ItemStackTemplate,
        ): HTHolderRecipeViewerType<INPUT, RECIPE> = create(deferredType, Either.Right(icon), bounds, *workStations)

        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
            deferredType: HTRecipeType.Managed<INPUT, RECIPE>,
            icon: Either<Identifier, ItemStackTemplate>,
            bounds: HTBounds,
            vararg workStations: ItemStackTemplate,
        ): HTHolderRecipeViewerType<INPUT, RECIPE> = create(deferredType, icon, bounds, listOfNotNull(icon.getRight(), *workStations))

        @JvmStatic
        inline fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, reified HOLDER : RecipeHolder<RECIPE>> create(
            deferredType: HTRecipeType.Managed<INPUT, RECIPE>,
            icon: Either<Identifier, ItemStackTemplate>,
            bounds: HTBounds,
            workstations: List<ItemStackTemplate>,
        ): HTHolderRecipeViewerType<INPUT, RECIPE> = HTHolderRecipeViewerType(
            HOLDER::class.java,
            deferredType,
            icon,
            bounds,
            workstations,
        )
    }

    override fun toString(): String = "HTHolderRecipeViewerType(class=${recipeClass.canonicalName})"
}
