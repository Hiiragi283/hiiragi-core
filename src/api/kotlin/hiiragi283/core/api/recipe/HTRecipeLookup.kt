package hiiragi283.core.api.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.client.Minecraft
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
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
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.common.recipe.IMekanismRecipeTypeProvider
 */
fun interface HTRecipeLookup<INPUT : RecipeInput, RECIPE : Any> {
    /**
     * 現在のサーバーまたはクライアントからレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(): Sequence<HTRecipeHolder<RECIPE>> = callWhenOn(Dist.CLIENT) { Minecraft.getInstance().level?.let(::getAllRecipes) }
        ?: run { HiiragiCoreAPI.getActiveServer()?.let(::getAllRecipes) }
        ?: emptySequence()

    /**
     * 指定した[level]からレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(level: Level): Sequence<HTRecipeHolder<RECIPE>> =
        getAllRecipes(Context(level.recipeManager, level.registryAccess(), level.potionBrewing()))

    /**
     * 指定した[server]からレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(server: MinecraftServer): Sequence<HTRecipeHolder<RECIPE>> =
        getAllRecipes(Context(server.recipeManager, server.registryAccess(), server.potionBrewing()))

    /**
     * 指定した[context]からレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(context: Context): Sequence<HTRecipeHolder<RECIPE>>

    /**
     * 指定した[level]から，[predicate]に一致するレシピを取得します。
     * @return [predicate]に一致するレシピがない場合は`null`
     */
    fun findFirst(level: Level?, predicate: (RECIPE) -> Boolean): HTRecipeHolder<RECIPE>? =
        (level?.let(this::getAllRecipes) ?: this.getAllRecipes()).firstOrNull { it.recipe.let(predicate) }

    /**
     * @since 0.14.0
     */
    fun getHolder(id: ResourceLocation): HTRecipeHolder<RECIPE>? =
        getAllRecipes().firstOrNull { holder: HTRecipeHolder<RECIPE> -> holder.id == id }

    /**
     * @author Hiiragi Tsubasa
     * @since 0.11.0
     */
    @JvmRecord
    data class Context(val manager: RecipeManager, val access: RegistryAccess, val brewing: PotionBrewing) {
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipes(recipeType: RecipeType<RECIPE>): Sequence<HTRecipeHolder<RECIPE>> =
            manager.getAllRecipesFor(recipeType).asSequence().map(HTRecipeHolder.Companion::from)
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> HTRecipeLookup<INPUT, RECIPE>.findFirst(
    input: INPUT,
    level: Level?,
): HTRecipeHolder<RECIPE>? = this.findFirst(level) { it.test(input) }
