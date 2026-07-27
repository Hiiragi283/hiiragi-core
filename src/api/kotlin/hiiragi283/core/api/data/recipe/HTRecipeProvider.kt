package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.allOf
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.getResult
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.resource.toId
import java.util.Optional
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.conditions.WithConditions

/**
 * Hiiragi Seriesで使用される，レシピ向けの[DataProvider]の抽象クラスです。
 * 参照 : [Minecraft - RecipeProvider][net.minecraft.data.recipes.RecipeProvider]
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
abstract class HTRecipeProvider(packOutput: PackOutput, private val future: CompletableFuture<HolderLookup.Provider>, protected val modId: String) :
    HTRecipeProviderContext(),
    DataProvider {
    private val pathProvider: PackOutput.PathProvider = packOutput.createRegistryElementsPathProvider(Registries.RECIPE)

    /**
     * レジストリへのアクセス
     *
     * [buildRecipes]の前に初期化されます。
     */
    protected lateinit var registries: HolderLookup.Provider
        private set

    /**
     * レシピの出力先
     *
     * [buildRecipes]の前に初期化されます。
     */
    override lateinit var exporter: HTRecipeExporter
        internal set

    final override fun run(output: CachedOutput): CompletableFuture<*> = future.thenCompose { registries: HolderLookup.Provider ->
        val recipes: MutableMap<ResourceLocation, WithConditions<Recipe<*>>> = hashMapOf()
        this.registries = registries
        this.exporter = HTRecipeExporter { id: ResourceLocation, recipe: Recipe<*>, conditions: List<ICondition> ->
            val fixedId: ResourceLocation = id.let(::modifyId)
            check(recipes.put(fixedId, WithConditions(conditions, recipe)) == null) { "Duplicate recipe $fixedId" }
        }
        buildRecipes()
        recipes
            .map { (id: ResourceLocation, value: WithConditions<Recipe<*>>) -> DataProvider.saveStable(output, registries, Recipe.CONDITIONAL_CODEC, Optional.of(value), pathProvider.json(id)) }
            .allOf()
    }

    /**
     * レシピを生成します。
     */
    protected abstract fun buildRecipes()

    /**
     * 受け取った[id]を[exporter]内で変換します。
     */
    protected open fun modifyId(id: ResourceLocation): ResourceLocation = modId.toId(id.path)

    //    Extensions    //

    /**
     * 指定した[パス][path]から[ID][ResourceLocation]を作成します。
     * @return [modId]を[名前空間][ResourceLocation.getNamespace]とする[ID][ResourceLocation]
     */
    protected fun id(path: String): ResourceLocation = modId.toId(path)

    /**
     * 指定した[パス][path]から[ID][ResourceLocation]を作成します。
     * @return [modId]を[名前空間][ResourceLocation.getNamespace]とする[ID][ResourceLocation]
     */
    protected fun id(vararg path: String): ResourceLocation = modId.toId(*path)

    /**
     * @since 21.1.0
     */
    protected inline fun useItem(part: HTPartLike, key: HTMaterialKey, action: (HTMaterialContents.ItemEntry) -> Unit) {
        HiiragiCoreAccess.INSTANCE
            .registeredContents
            .items
            .getResult(part, key)
            .onLeft { DataProvider.LOGGER.error(it.value) }
            .onRight(action)
    }

    //    Integration    //

    abstract class Integration(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>, modId: String, integrationModId: String) : HTRecipeProvider(packOutput, future, modId) {
        val condition = ModLoadedCondition(integrationModId)
        private val builtInIds: Set<String> = HTConst.getBuiltInIdSet(modId)

        final override fun modifyId(id: ResourceLocation): ResourceLocation {
            val namespace: String = id.namespace
            return if (namespace in builtInIds) {
                val path: List<String> = id.path.split("/", limit = 2)
                id(path[0], modId, path[1])
            } else {
                val path: List<String> = id.path.split("/", limit = 2)
                id(path[0], namespace, path[1])
            }
        }
    }
}
