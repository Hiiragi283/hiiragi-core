package hiiragi283.core.api.recipe.cache

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTResultCreator
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.buildPropertyMap
import hiiragi283.core.api.property.getOrThrow
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import net.minecraft.client.Minecraft
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist
import kotlin.jvm.optionals.getOrNull

/**
 * レシピの一覧を提供するインターフェースです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.common.recipe.IMekanismRecipeTypeProvider
 */
fun interface HTRecipeLookup<RECIPE : Any> {
    /**
     * 現在のサーバーまたはクライアントからレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(): Sequence<HTRecipeHolder<RECIPE>> = runForDist(
        { Minecraft.getInstance().level?.let(::getAllRecipes) },
        { ServerLifecycleHooks.getCurrentServer()?.let(::getAllRecipes) },
    ) ?: emptySequence()

    /**
     * 指定した[level]からレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(level: Level): Sequence<HTRecipeHolder<RECIPE>> = Context.create(level).let(::getAllRecipes)

    /**
     * 指定した[server]からレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(server: MinecraftServer): Sequence<HTRecipeHolder<RECIPE>> = Context.create(server).let(::getAllRecipes)

    /**
     * 指定した[context]からレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(context: Context): Sequence<HTRecipeHolder<RECIPE>>

    /**
     * 指定した[level]から，[predicate]に一致するレシピを取得します。
     * @return [predicate]に一致するレシピがない場合は`null`
     */
    fun findFirst(level: Level, predicate: (RECIPE) -> Boolean): HTRecipeHolder<RECIPE>? =
        getAllRecipes(level).firstOrNull { it.recipe.let(predicate) }

    class Context(getter: HTPropertyGetter) : HTPropertyGetter by getter {
        companion object {
            @JvmField
            val BREWING: HTPropertyKey<PotionBrewing?> = create("brewing")

            @JvmField
            val MANAGER: HTPropertyKey<RecipeManager?> = create("manager")

            @JvmField
            val REGISTRY: HTPropertyKey<RegistryAccess?> = create("registry")

            private fun <T : Any> create(path: String): HTPropertyKey<T?> =
                HTPropertyKey.createNullable(HTConst.MINECRAFT.toId("recipe", path))

            @JvmStatic
            fun create(level: Level): Context = Context(
                buildPropertyMap {
                    this[BREWING] = level.potionBrewing()
                    this[MANAGER] = level.recipeManager
                    this[REGISTRY] = level.registryAccess()
                },
            )

            @JvmStatic
            fun create(server: MinecraftServer): Context = Context(
                buildPropertyMap {
                    this[BREWING] = server.potionBrewing()
                    this[MANAGER] = server.recipeManager
                    this[REGISTRY] = server.registryAccess()
                },
            )
        }

        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipes(recipeType: RecipeType<RECIPE>): Sequence<HTRecipeHolder<RECIPE>> =
            this[MANAGER]?.getAllRecipesFor(recipeType)?.asSequence()?.map(HTRecipeHolder.Companion::from) ?: emptySequence()

        fun <T : Any> lookup(key: RegistryKey<T>): HolderLookup.RegistryLookup<T>? = this[REGISTRY]?.lookup(key)?.getOrNull()

        fun <T : Any> lookupOrThrow(key: RegistryKey<T>): HolderLookup.RegistryLookup<T> = this.getOrThrow(REGISTRY).lookupOrThrow(key)

        fun <T : Any> registry(key: RegistryKey<T>): Registry<T>? = this[REGISTRY]?.registry(key)?.getOrNull()

        fun <T : Any> registryOrThrow(key: RegistryKey<T>): Registry<T> = this.getOrThrow(REGISTRY).registryOrThrow(key)

        fun resultCreator(): HTResultCreator? = this[REGISTRY]?.let(::HTResultCreator)
    }
}
