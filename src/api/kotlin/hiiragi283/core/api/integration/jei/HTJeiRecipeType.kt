package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType

typealias HTJeiHolderRecipeType<RECIPE> = HTJeiRecipeType<RecipeHolder<RECIPE>>

/**
 * @see mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType
 */
class HTJeiRecipeType<RECIPE : Any>(
    val recipeClass: Class<out RECIPE>,
    private val id: ResourceLocation,
    hasText: HTHasText,
    val icon: Either<ResourceLocation, ItemStack>,
    val bounds: HTBounds,
    val workStations: List<ItemStack>,
) : HTHasText by hasText,
    HTIdLike {
    companion object {
        @JvmStatic
        fun <RECIPE : Recipe<*>> createRecipe(
            recipeType: HTHolderLike<RecipeType<*>, RecipeType<RECIPE>>,
            hasText: HTHasText,
            icon: ItemStack,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTJeiHolderRecipeType<RECIPE> = createRecipe(
            recipeType,
            hasText,
            Either.Right(icon),
            bounds,
            *workStations,
        )

        @JvmStatic
        fun <RECIPE : Recipe<*>> createRecipe(
            recipeType: HTHolderLike<RecipeType<*>, RecipeType<RECIPE>>,
            hasText: HTHasText,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            vararg workStations: ItemStack,
        ): HTJeiHolderRecipeType<RECIPE> = createRecipe(
            recipeType,
            hasText,
            icon,
            bounds,
            listOfNotNull(icon.getRight(), *workStations),
        )

        @JvmStatic
        inline fun <RECIPE : Recipe<*>, reified HOLDER : RecipeHolder<RECIPE>> createRecipe(
            recipeType: HTHolderLike<RecipeType<*>, RecipeType<RECIPE>>,
            hasText: HTHasText,
            icon: Either<ResourceLocation, ItemStack>,
            bounds: HTBounds,
            workStations: List<ItemStack>,
        ): HTJeiHolderRecipeType<RECIPE> = HTJeiRecipeType(
            HOLDER::class.java,
            recipeType.getId(),
            hasText,
            icon,
            bounds,
            workStations,
        )
    }

    override fun getId(): ResourceLocation = id
}
