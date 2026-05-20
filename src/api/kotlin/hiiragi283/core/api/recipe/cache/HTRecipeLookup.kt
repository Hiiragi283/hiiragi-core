package hiiragi283.core.api.recipe.cache

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.buildPropertyMap
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.lookupResult
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.flatMap
import hiiragi283.core.api.util.right
import net.minecraft.core.HolderLookup
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

/**
 * レシピの一覧を提供するインターフェースです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.common.recipe.IMekanismRecipeTypeProvider
 */
fun interface HTRecipeLookup<out RECIPE> {
    /**
     * 指定した[context]からレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(context: Context): Sequence<HTRecipeHolder<RECIPE>>

    /**
     * 指定した[level]から，[predicate]に一致するレシピを取得します。
     * @return [predicate]に一致するレシピがない場合は`null`
     */
    fun findFirst(level: Level, predicate: (RECIPE) -> Boolean): HTRecipeHolder<RECIPE>? = Context.create(level).let(::getAllRecipes).firstOrNull { it.recipe.let(predicate) }

    class Context(getter: HTPropertyGetter) : HTPropertyGetter by getter {
        companion object {
            @JvmField
            val BREWING: HTPropertyKey<PotionBrewing?> = create("brewing")

            @JvmField
            val MANAGER: HTPropertyKey<RecipeManager?> = create("manager")

            @JvmField
            val REGISTRY: HTPropertyKey<HolderLookup.Provider?> = create("registry")

            private fun <T : Any> create(path: String): HTPropertyKey<T?> = HTPropertyKey.createNullable(HTConst.MINECRAFT.toId("recipe", path))

            @JvmStatic
            fun create(level: Level): Context = create {
                this[BREWING] = level.potionBrewing()
                this[MANAGER] = level.recipeManager
                this[REGISTRY] = level.registryAccess()
            }

            @JvmStatic
            fun create(server: MinecraftServer): Context = create {
                this[BREWING] = server.potionBrewing()
                this[MANAGER] = server.recipeManager
                this[REGISTRY] = server.registryAccess()
            }

            @JvmStatic
            inline fun create(builderAction: HTPropertyMap.Builder.() -> Unit): Context = buildPropertyMap(builderAction).let(::Context)
        }

        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipes(recipeType: RecipeType<RECIPE>): Sequence<HTRecipeHolder<RECIPE>> = this[MANAGER]?.getAllRecipesFor(recipeType)?.asSequence()?.map(HTRecipeHolder.Companion::from) ?: emptySequence()

        fun <T : Any> lookup(key: RegistryKey<T>): HTTextResult<HolderLookup.RegistryLookup<T>> = this[REGISTRY]?.right()?.flatMap { it.lookupResult(key) } ?: HTTextResult("Recipe lookup context does not have registry access")
    }
}
