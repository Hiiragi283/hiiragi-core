package hiiragi283.core.api.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.IdToValue
import net.minecraft.client.Minecraft
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import thedarkcolour.kotlinforforge.neoforge.forge.callWhenOn

/**
 * レシピの一覧を提供するインターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @param HOLDER [ResourceLocation]と[RECIPE]を束ねたクラス
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
     * 現在のサーバーまたはクライアントからレシピの一覧を取得します。
     * @return [HOLDER]の[Sequence]
     */
    fun getAllRecipes(): Sequence<HOLDER> = callWhenOn(Dist.CLIENT) { Minecraft.getInstance().level?.let(::getAllRecipes) }
        ?: run { HiiragiCoreAPI.getActiveServer()?.let(::getAllRecipes) }
        ?: emptySequence()

    /**
     * 指定した[level]からレシピの一覧を取得します。
     * @return [HOLDER]の[Sequence]
     */
    fun getAllRecipes(level: Level): Sequence<HOLDER> =
        getAllRecipes(Context(level.recipeManager, level.registryAccess(), level.potionBrewing()))

    /**
     * 指定した[server]からレシピの一覧を取得します。
     * @return [HOLDER]の[Sequence]
     */
    fun getAllRecipes(server: MinecraftServer): Sequence<HOLDER> =
        getAllRecipes(Context(server.recipeManager, server.registryAccess(), server.potionBrewing()))

    /**
     * 指定した[context]からレシピの一覧を取得します。
     * @return [HOLDER]の[Sequence]
     */
    fun getAllRecipes(context: Context): Sequence<HOLDER>

    /**
     * 指定した[level]から，[predicate]に一致するレシピを取得します。
     * @return [predicate]に一致するレシピがない場合は`null`
     */
    fun findFirst(level: Level?, predicate: (RECIPE) -> Boolean): HOLDER? =
        (level?.let(this::getAllRecipes) ?: this.getAllRecipes()).firstOrNull { getRecipe(it).let(predicate) }

    fun createHolder(id: ResourceLocation, recipe: RECIPE): HOLDER

    fun getId(holder: HOLDER): ResourceLocation

    fun getRecipe(holder: HOLDER): RECIPE

    /**
     * @author Hiiragi Tsubasa
     * @since 0.11.0
     */
    @JvmRecord
    data class Context(val manager: RecipeManager, val access: RegistryAccess, val brewing: PotionBrewing) {
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipes(recipeType: RecipeType<RECIPE>): Sequence<RecipeHolder<RECIPE>> =
            manager.getAllRecipesFor(recipeType).asSequence()
    }

    /**
     * [RecipeHolder]に基づいた[HTRecipeLookup]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Managed<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> : HTRecipeLookup<INPUT, RECIPE, RecipeHolder<RECIPE>> {
        override fun createHolder(id: ResourceLocation, recipe: RECIPE): RecipeHolder<RECIPE> = RecipeHolder(id, recipe)

        override fun getId(holder: RecipeHolder<RECIPE>): ResourceLocation = holder.id()

        override fun getRecipe(holder: RecipeHolder<RECIPE>): RECIPE = holder.value()
    }

    /**
     * [IdToValue]に基づいた[HTRecipeLookup]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Fake<INPUT : RecipeInput, RECIPE : Any> : HTRecipeLookup<INPUT, RECIPE, IdToValue<RECIPE>> {
        override fun createHolder(id: ResourceLocation, recipe: RECIPE): IdToValue<RECIPE> = IdToValue(id, recipe)

        override fun getId(holder: IdToValue<RECIPE>): ResourceLocation = holder.first

        override fun getRecipe(holder: IdToValue<RECIPE>): RECIPE = holder.second
    }
}

//    Extensions    //

fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>, HOLDER : Any> HTRecipeLookup<INPUT, RECIPE, HOLDER>.findFirst(
    input: INPUT,
    level: Level?,
): HOLDER? = this.findFirst(level) { it.test(input) }
