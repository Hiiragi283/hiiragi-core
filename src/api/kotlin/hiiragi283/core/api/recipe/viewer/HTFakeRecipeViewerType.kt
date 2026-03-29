package hiiragi283.core.api.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.resource.IdToValue
import hiiragi283.core.api.util.Either
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [IdToValue]に基づいた[HTRecipeViewerType]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 * @see mekanism.client.recipe_viewer.type.FakeRVRecipeType
 */
class HTFakeRecipeViewerType<INPUT : RecipeInput, RECIPE : Any>(
    override val recipeClass: Class<out IdToValue<RECIPE>>,
    deferredType: HTRecipeType.Fake<INPUT, RECIPE>,
    override val icon: Either<ResourceLocation, ItemStack>,
    override val bounds: HTBounds,
    override val workStations: List<ItemStack>,
) : HTRecipeViewerType<IdToValue<RECIPE>>,
    HTRecipeType.Fake<INPUT, RECIPE> by deferredType {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Any> create(
            deferredType: HTRecipeType.Fake<INPUT, RECIPE>,
            icon: ItemStack,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTFakeRecipeViewerType<INPUT, RECIPE> = create(deferredType, Either.Right(icon), bounds, *workStations)

        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Any> create(
            deferredType: HTRecipeType.Fake<INPUT, RECIPE>,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTFakeRecipeViewerType<INPUT, RECIPE> = create(deferredType, icon, bounds, listOfNotNull(icon.getRight(), *workStations))

        @JvmStatic
        inline fun <INPUT : RecipeInput, RECIPE : Any, reified HOLDER : IdToValue<RECIPE>> create(
            deferredType: HTRecipeType.Fake<INPUT, RECIPE>,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            workstations: List<ItemStack>,
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
