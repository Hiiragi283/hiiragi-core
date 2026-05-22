package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe

/**
 * @see net.minecraft.data.recipes.SimpleCookingRecipeBuilder
 */
class HTCookingRecipeBuilder(
    private val factory: AbstractCookingRecipe.Factory<*>,
    private val timeOperator: Identity<Int>,
    prefix: String,
) : HTRecipeBuilder<AbstractCookingRecipe>(prefix) {
    companion object {
        @JvmStatic
        inline fun smelting(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            ::SmeltingRecipe,
            identity(),
            HTConstants.SMELTING,
        ).apply(builderAction)

        @JvmStatic
        inline fun blasting(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            ::BlastingRecipe,
            identity(),
            HTConstants.BLASTING,
        ).apply(builderAction)

        @JvmStatic
        inline fun smoking(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            ::SmokingRecipe,
            identity(),
            HTConstants.SMOKING,
        ).apply(builderAction)

        @JvmStatic
        inline fun smeltingAndBlasting(builderAction: HTCookingRecipeBuilder.() -> Unit): Sequence<HTCookingRecipeBuilder> = sequenceOf(
            smelting(builderAction),
            HTCookingRecipeBuilder(::BlastingRecipe, { it / 2 }, HTConstants.BLASTING).apply(builderAction),
        )

        @JvmStatic
        inline fun smeltingAndSmoking(builderAction: HTCookingRecipeBuilder.() -> Unit): Sequence<HTCookingRecipeBuilder> = sequenceOf(
            smelting(builderAction),
            HTCookingRecipeBuilder(::SmokingRecipe, { it / 2 }, HTConstants.SMOKING).apply(builderAction),
        )
    }

    var group: String? = null
    var craftingCategory: RecipeCategory = RecipeCategory.MISC
    var category: CookingBookCategory = CookingBookCategory.MISC
    var ingredient: Ingredient by HTDelegates.onceInitialize()
    var result: ItemStackTemplate by HTDelegates.onceInitialize()
    var exp: Float = 0f
    var time: Int = 20 * 10

    val unlocker = RecipeUnlockAdvancementBuilder()

    override fun getPrimalId(): Identifier = result.item().unwrapKey().orElseThrow().identifier()

    override fun createRecipe(): AbstractCookingRecipe = factory.create(
        commonInfo(true),
        AbstractCookingRecipe.CookingBookInfo(category, group ?: ""),
        ingredient,
        result,
        exp,
        timeOperator(time),
    )

    override fun save(recipeOutput: RecipeOutput) {
        this.save { id: Identifier, recipe: AbstractCookingRecipe ->
            val recipeKey: RecipeKey = RecipeKey(id)
            recipeOutput.accept(
                recipeKey,
                recipe,
                unlocker.build(recipeOutput, recipeKey, craftingCategory),
            )
        }
    }
}
