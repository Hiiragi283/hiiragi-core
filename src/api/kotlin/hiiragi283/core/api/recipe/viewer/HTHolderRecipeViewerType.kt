package hiiragi283.core.api.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.recipe.HTRecipeType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [RecipeHolder]に基づいた[HTRecipeViewerType]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.client.recipe_viewer.type.RecipeViewerRecipeType
 */
class HTHolderRecipeViewerType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
    override val recipeClass: Class<out RecipeHolder<RECIPE>>,
    deferredType: HTRecipeType<INPUT, RECIPE>,
    override val icon: Either<ResourceLocation, ItemStack>,
    override val bounds: HTBounds,
    override val workStations: List<ItemStack>,
) : HTRecipeViewerType<RecipeHolder<RECIPE>>,
    HTRecipeType<INPUT, RECIPE> by deferredType {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
            deferredType: HTRecipeType<INPUT, RECIPE>,
            icon: ItemStack,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTHolderRecipeViewerType<INPUT, RECIPE> = create(deferredType, Either.Right(icon), bounds, *workStations)

        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
            deferredType: HTRecipeType<INPUT, RECIPE>,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTHolderRecipeViewerType<INPUT, RECIPE> = create(deferredType, icon, bounds, listOfNotNull(icon.getRight(), *workStations))

        @JvmStatic
        inline fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, reified HOLDER : RecipeHolder<RECIPE>> create(
            deferredType: HTRecipeType<INPUT, RECIPE>,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            workstations: List<ItemStack>,
        ): HTHolderRecipeViewerType<INPUT, RECIPE> = HTHolderRecipeViewerType(
            HOLDER::class.java,
            deferredType,
            icon,
            bounds,
            workstations,
        )
    }
}
