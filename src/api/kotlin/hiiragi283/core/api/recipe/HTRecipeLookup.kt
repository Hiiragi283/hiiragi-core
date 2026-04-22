package hiiragi283.core.api.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.buildPropertyMap
import hiiragi283.core.api.property.getOrThrow
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import net.minecraft.client.Minecraft
import net.minecraft.core.Registry
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
import kotlin.jvm.optionals.getOrNull

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
    fun getAllRecipes(): Sequence<HTRecipeHolder<RECIPE>> = getAllRecipes(null)

    /**
     * 指定した[level]からレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(level: Level?): Sequence<HTRecipeHolder<RECIPE>> {
        val level1: Level = level
            ?: callWhenOn(Dist.CLIENT) { Minecraft.getInstance().level }
            ?: return HiiragiCoreAPI.getActiveServer()?.let(::getAllRecipes) ?: emptySequence()
        return Context.create(level1).let(::getAllRecipes)
    }

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
    fun findFirst(level: Level?, predicate: (RECIPE) -> Boolean): HTRecipeHolder<RECIPE>? =
        this.getAllRecipes(level).firstOrNull { it.recipe.let(predicate) }

    /**
     * @since 0.14.0
     */
    fun getHolder(id: ResourceLocation): HTRecipeHolder<RECIPE>? =
        getAllRecipes().firstOrNull { holder: HTRecipeHolder<RECIPE> -> holder.id == id }

    interface Context : HTPropertyGetter {
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
            fun create(level: Level): Context = object : Context {
                val map: HTPropertyGetter = buildPropertyMap {
                    this[BREWING] = level.potionBrewing()
                    this[MANAGER] = level.recipeManager
                    this[REGISTRY] = level.registryAccess()
                }

                override fun <T> get(key: HTPropertyKey<T>): T? = map[key]
            }

            @JvmStatic
            fun create(server: MinecraftServer): Context = object : Context {
                val map: HTPropertyGetter = buildPropertyMap {
                    this[BREWING] = server.potionBrewing()
                    this[MANAGER] = server.recipeManager
                    this[REGISTRY] = server.registryAccess()
                }

                override fun <T> get(key: HTPropertyKey<T>): T? = map[key]
            }
        }

        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipes(recipeType: RecipeType<RECIPE>): Sequence<HTRecipeHolder<RECIPE>> =
            this[MANAGER]?.getAllRecipesFor(recipeType)?.asSequence()?.map(HTRecipeHolder.Companion::from) ?: emptySequence()

        fun <T : Any> registry(key: RegistryKey<T>): Registry<T>? = this[REGISTRY]?.registry(key)?.getOrNull()

        fun <T : Any> registryOrThrow(key: RegistryKey<T>): Registry<T> = this.getOrThrow(REGISTRY).registryOrThrow(key)
    }
}
