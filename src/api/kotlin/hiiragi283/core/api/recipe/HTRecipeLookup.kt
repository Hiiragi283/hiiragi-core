package hiiragi283.core.api.recipe

import net.minecraft.core.RegistryAccess
import net.minecraft.resources.Identifier
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeMap
import net.minecraft.world.item.crafting.RecipeType

/**
 * レシピの一覧を提供するインターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @param HOLDER [RecipeKey]と[RECIPE]を束ねたクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.common.recipe.IMekanismRecipeTypeProvider
 */
sealed interface HTRecipeLookup<INPUT : RecipeInput, RECIPE : Any, HOLDER : Any> {
    /**
     * [HTRecipeCache]の新しいインスタンスを作成します。
     */
    fun createCache(): HTRecipeCache<INPUT, RECIPE>

    /**
     * 指定した[server]からレシピの一覧を取得します。
     * @return [HOLDER]の[Sequence]
     */
    fun getAllRecipes(server: MinecraftServer): Sequence<HOLDER> =
        getAllRecipes(Context(server.recipeManager.recipeMap(), server.registryAccess(), server.potionBrewing()))

    /**
     * 指定した[context]からレシピの一覧を取得します。
     * @return [HOLDER]の[Sequence]
     */
    fun getAllRecipes(context: Context): Sequence<HOLDER>

    /**
     * 指定した[level]から，[predicate]に一致するレシピを取得します。
     * @return [predicate]に一致するレシピがない場合は`null`
     */
    fun findFirst(level: ServerLevel, predicate: (RECIPE) -> Boolean): HOLDER? =
        this.getAllRecipes(level.server).firstOrNull { getRecipe(it).let(predicate) }

    fun createHolder(key: RecipeKey, recipe: RECIPE): HOLDER

    fun getKey(holder: HOLDER): RecipeKey

    fun getId(holder: HOLDER): Identifier = getKey(holder).identifier()

    fun getRecipe(holder: HOLDER): RECIPE

    /**
     * @author Hiiragi Tsubasa
     * @since 0.11.0
     */
    @JvmRecord
    data class Context(val recipeMap: RecipeMap, val access: RegistryAccess, val brewing: PotionBrewing) {
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipes(recipeType: RecipeType<RECIPE>): Sequence<RecipeHolder<RECIPE>> =
            recipeMap.byType(recipeType).asSequence()
    }

    /**
     * [RecipeHolder]に基づいた[HTRecipeLookup]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Managed<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> : HTRecipeLookup<INPUT, RECIPE, RecipeHolder<RECIPE>> {
        override fun createHolder(key: RecipeKey, recipe: RECIPE): RecipeHolder<RECIPE> = RecipeHolder(key, recipe)

        override fun getKey(holder: RecipeHolder<RECIPE>): RecipeKey = holder.id()

        override fun getRecipe(holder: RecipeHolder<RECIPE>): RECIPE = holder.value()
    }

    /**
     * [FakeRecipeHolder]に基づいた[HTRecipeLookup]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Fake<INPUT : RecipeInput, RECIPE : Any> : HTRecipeLookup<INPUT, RECIPE, FakeRecipeHolder<RECIPE>> {
        override fun createHolder(key: RecipeKey, recipe: RECIPE): FakeRecipeHolder<RECIPE> = FakeRecipeHolder(key, recipe)

        override fun getKey(holder: FakeRecipeHolder<RECIPE>): RecipeKey = holder.key

        override fun getRecipe(holder: FakeRecipeHolder<RECIPE>): RECIPE = holder.recipe
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>, HOLDER : Any> HTRecipeLookup<INPUT, RECIPE, HOLDER>.findFirst(
    input: INPUT,
    level: ServerLevel,
): HOLDER? = this.findFirst(level) { it.test(input) }
