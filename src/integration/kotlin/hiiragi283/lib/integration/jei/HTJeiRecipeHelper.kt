package hiiragi283.lib.integration.jei

import hiiragi283.lib.HTComparators
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.lib.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import hiiragi283.lib.recipe.viewer.display.HTRecipeDisplay
import java.util.function.Supplier
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.display.FluidStackContentsFactory

/**
 * [IRecipeRegistration]へのレシピ登録を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class HTJeiRecipeHelper(@PublishedApi internal val registration: IRecipeRegistration) {
    companion object {
        @JvmStatic
        fun createContext(): ContextMap = HTPhysicalSideHelper.runForSide(HTRecipeLookupContext::createOnClient, HTRecipeLookupContext::create) ?: ContextMap.EMPTY

        @JvmStatic
        fun resolveFluids(ingredient: FluidIngredient): List<FluidStack> = resolveFluids(ingredient.display())

        @JvmStatic
        fun resolveFluids(display: SlotDisplay): List<FluidStack> = display.resolve(createContext(), FluidStackContentsFactory.INSTANCE).toList()

        @JvmStatic
        fun resolveItems(ingredient: Ingredient): List<ItemStack> = resolveItems(ingredient.display())

        @JvmStatic
        fun resolveItems(display: SlotDisplay): List<ItemStack> = display.resolveForStacks(createContext())

        @JvmField
        val DISPLAY_SORTER: Comparator<in HTRecipeDisplay> = compareBy(HTComparators.ID, HTRecipeDisplay::getId)

        @JvmField
        val HOLDER_SORTER: Comparator<in HTRecipeHolder<*>> = compareBy(HTComparators.ID, HTRecipeHolder<*>::getId)
    }

    fun <T : Any> addRecipes(recipeType: IRecipeType<T>, recipes: Sequence<T>) {
        val list: List<T> = recipes.toList()
        if (list.isEmpty()) return
        registration.addRecipes(recipeType, list)
    }

    // HTRecipeViewerType
    fun <T : Any> addRecipes(viewerType: HTRecipeViewerType<T>, recipes: Sequence<T>) {
        this.addRecipes(HTJeiPlugin.getRecipeType(viewerType), recipes)
    }

    fun <T : Any> addRecipes(viewerType: HTRecipeViewerType<T>, recipes: Sequence<T>, sorter: Comparator<in T>) {
        this.addRecipes(viewerType, recipes.sortedWith(sorter))
    }

    // HTRecipeHolder
    fun <T : Any> addHolderRecipes(viewerType: HTHolderRecipeViewerType<T>, recipes: Sequence<HTRecipeHolder<T>>) {
        this.addRecipes(viewerType, recipes, HOLDER_SORTER)
    }

    fun <T : Any> addHolderRecipes(
        viewerType: HTHolderRecipeViewerType<T>,
        recipes: Sequence<HTRecipeHolder<T>>,
        sorter: Comparator<in T>,
    ) {
        this.addRecipes(viewerType, recipes, compareBy(sorter, HTRecipeHolder<T>::recipe).thenComparing(HOLDER_SORTER))
    }

    // HTRecipeLookup
    /**
     * 指定した[viewerType]と[lookup]からレシピを登録します。
     * @param T レシピのクラス
     */
    fun <T : Any> addLookupRecipes(viewerType: HTHolderRecipeViewerType<T>, lookup: HTRecipeLookup<T>) {
        this.addHolderRecipes(viewerType, lookup.getAllRecipes(createContext()))
    }

    /**
     * 指定した[viewerType]と[lookup]からレシピを登録します。
     * @param T レシピのクラス
     * @param sorter レシピの順番の制御
     */
    fun <T : Any> addLookupRecipes(viewerType: HTHolderRecipeViewerType<T>, lookup: HTRecipeLookup<T>, sorter: Comparator<in T>) {
        this.addHolderRecipes(viewerType, lookup.getAllRecipes(createContext()), sorter)
    }

    // HTRecipeDisplay
    fun <DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipes: Sequence<DISPLAY>) {
        this.addRecipes(viewerType, recipes, DISPLAY_SORTER)
    }

    fun <DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipes: Sequence<DISPLAY>, sorter: Comparator<DISPLAY>) {
        this.addRecipes(viewerType, recipes, sorter.thenComparing(DISPLAY_SORTER))
    }

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipeType: RecipeType<RECIPE>, transform: (HTRecipeHolder<RECIPE>) -> DISPLAY) {
        this.addDisplayRecipes(viewerType, HTVanillaRecipeLookup(recipeType).getAllRecipes(createContext()).map(transform))
    }

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipeType: Supplier<out RecipeType<RECIPE>>, transform: (HTRecipeHolder<RECIPE>) -> DISPLAY) {
        this.addDisplayRecipes(viewerType, HTVanillaRecipeLookup(recipeType).getAllRecipes(createContext()).map(transform))
    }

    fun <BASE : Any, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, lookup: HTRecipeLookup<BASE>, transform: (HTRecipeHolder<BASE>) -> DISPLAY?) {
        this.addDisplayRecipes(viewerType, lookup.getAllRecipes(createContext()).mapNotNull(transform))
    }

    fun <BASE : Any, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, lookup: HTRecipeLookup<BASE>, sorter: Comparator<DISPLAY>, transform: (HTRecipeHolder<BASE>) -> DISPLAY?) {
        this.addDisplayRecipes(viewerType, lookup.getAllRecipes(createContext()).mapNotNull(transform), sorter)
    }
}
