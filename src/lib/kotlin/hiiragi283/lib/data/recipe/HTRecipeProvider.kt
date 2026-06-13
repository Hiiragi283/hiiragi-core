package hiiragi283.lib.data.recipe

import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.HTMaterialRawEntry
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.ingredient.HTMaterialPartIngredient
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.toId
import hiiragi283.lib.tag.HTTagPrefix
import java.util.Optional
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.WithConditions

/**
 * Hiiragi Seriesで使用される，レシピ向けの[DataProvider]の抽象クラスです。
 * 参照 : [Minecraft - RecipeProvider][net.minecraft.data.recipes.RecipeProvider]
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
abstract class HTRecipeProvider(packOutput: PackOutput, private val future: CompletableFuture<HolderLookup.Provider>, protected val modId: String) : DataProvider {
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
    protected lateinit var exporter: HTRecipeExporter
        private set

    override fun run(cache: CachedOutput): CompletableFuture<*> = future.thenCompose { registries: HolderLookup.Provider ->
        val recipes: MutableSet<Identifier> = hashSetOf()
        val tasks: MutableList<CompletableFuture<*>> = mutableListOf()
        this.registries = registries
        this.exporter = HTRecipeExporter { key: RecipeKey, recipe: Recipe<*>, conditions: List<ICondition> ->
            val fixedId: Identifier = key.identifier().let(::modifyId)
            check(recipes.add(fixedId)) { "Duplicate recipe $fixedId" }
            tasks += DataProvider.saveStable(cache, registries, Recipe.CONDITIONAL_CODEC, Optional.of(WithConditions(conditions, recipe)), pathProvider.json(fixedId))
        }
        buildRecipes()
        CompletableFuture.allOf(*tasks.toTypedArray())
    }

    /**
     * レシピを生成します。
     */
    protected abstract fun buildRecipes()

    /**
     * 受け取った[id]を[exporter]内で変換します。
     */
    protected open fun modifyId(id: Identifier): Identifier = modId.toId(id.path)

    //    Extensions    //

    /**
     * 指定した[パス][path]から[ID][Identifier]を作成します。
     * @return [modId]を[名前空間][Identifier.getNamespace]とする[ID][Identifier]
     */
    protected fun id(path: String): Identifier = modId.toId(path)

    /**
     * 指定した[パス][path]から[ID][Identifier]を作成します。
     * @return [modId]を[名前空間][Identifier.getNamespace]とする[ID][Identifier]
     */
    protected fun id(vararg path: String): Identifier = modId.toId(*path)

    fun getHasName(id: HTIdLike): String = "has_${id.path}"

    fun getHasName(tagKey: TagKey<*>): String = "has_${tagKey.location().path.replace("/", "_")}"

    /**
     * 新しい[HTMaterialPartIngredient]のインスタンスを作成します。
     */
    fun materialPart(tagKey: TagKey<HTMaterialContents>, vararg parts: HTMaterialPartKey): HTMaterialPartIngredient = HTMaterialPartIngredient(registries.getOrThrow(tagKey), listOf(*parts).sorted())

    fun HTMaterialRawEntry.setHolderSet(builder: IngredientBuilder) {
        builder.apply {
            this@setHolderSet.map(
                { items { +it.asItem() } },
                { +holderSet(it) },
            )
        }
    }

    /**
     * [HolderSet]を取得します。
     * @param tagKey 対応するタグ
     */
    fun <T : Any> holderSet(tagKey: TagKey<T>): HolderSet<T> = this.registries.getOrThrow(tagKey)

    /**
     * [HolderSet]を取得します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    fun holderSet(prefix: HTTagPrefix, material: HTMaterialKey): HolderSet<Item> = holderSet(prefix.itemTagKey(material))

    /**
     * [HolderSet]を取得します。
     * @param content 液体タグの提供元
     */
    fun holderSet(content: HTFluidContent): HolderSet<Fluid> = holderSet(content.fluidTag)
}
