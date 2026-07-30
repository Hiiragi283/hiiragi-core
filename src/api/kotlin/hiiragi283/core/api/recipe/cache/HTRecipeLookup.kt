@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.recipe.cache

import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.buildPropertyMap
import hiiragi283.core.api.property.getOrThrow
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.lookupResult
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.api.util.HTTextResult
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

/**
 * レシピの一覧を提供するインターフェースです。
 *
 * 参照 : [Mekanism - IMekanismRecipeTypeProvider](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/recipe/IMekanismRecipeTypeProvider.java)
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun interface HTRecipeLookup<out RECIPE> {
    /**
     * レシピの一覧を取得します。
     * @param context レシピのコンテキスト
     */
    fun getAllRecipes(context: Context): Map<ResourceLocation, RECIPE>

    fun asSequence(context: Context): Sequence<HTRecipeHolder<RECIPE>> = getAllRecipes(context).asSequence().map { (id: ResourceLocation, value: RECIPE) -> id to value }

    /**
     * [HTRecipeLookup]の拡張インターフェースです。
     *
     * 参照 : [Mekanism - IMekanismRecipeTypeProvider](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/recipe/IMekanismRecipeTypeProvider.java)
     * @param RECIPE レシピのクラス
     * @author Hiiragi Tsubasa
     * @since 21.1.0
     */
    interface Translatable<out RECIPE> :
        HTRecipeLookup<RECIPE>,
        HTKeyLike.SimpleTranslatable<RecipeType<*>>

    /**
     * レシピの一覧などを提供するクラスです。
     */
    class Context(getter: HTPropertyGetter) : HTPropertyGetter by getter {
        companion object {
            @JvmField
            val RECIPES: HTPropertyKey<RecipeManager?> = create("recipes")

            @JvmField
            val REGISTRIES: HTPropertyKey<HolderLookup.Provider?> = create("registries")

            @JvmField
            val BREWING: HTPropertyKey<PotionBrewing?> = create("brewing")

            private fun <T : Any> create(path: String): HTPropertyKey<T?> = HTPropertyKey.createNullable(vanillaId("recipe", path))

            @JvmStatic
            fun create(level: Level): Context = create {
                this[RECIPES] = level.recipeManager
                this[REGISTRIES] = level.registryAccess()
                this[BREWING] = level.potionBrewing()
            }

            @JvmStatic
            fun create(server: MinecraftServer): Context = create {
                this[RECIPES] = server.recipeManager
                this[REGISTRIES] = server.registryAccess()
                this[BREWING] = server.potionBrewing()
            }

            @JvmStatic
            inline fun create(builderAction: HTPropertyMap.Builder.() -> Unit): Context {
                contract {
                    callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
                }
                return buildPropertyMap(builderAction).let(::Context)
            }
        }

        val recipes: RecipeManager get() = this.getOrThrow(RECIPES)
        val registries: HolderLookup.Provider? get() = this[REGISTRIES]
        val brewing: PotionBrewing? get() = this[BREWING]

        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipes(recipeType: RecipeType<RECIPE>): Sequence<HTRecipeHolder<RECIPE>> = recipes.getAllRecipesFor(recipeType).asSequence().map(::HTRecipeHolder)

        fun <T : Any> lookup(key: RegistryKey<T>): HTTextResult<HolderLookup.RegistryLookup<T>> = registries?.lookupResult(key) ?: HTTextResult("Recipe lookup context does not have registry access")
    }
}
