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
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.WithConditions

/**
 * @see net.minecraft.data.recipes.RecipeProvider
 */
abstract class HTRecipeProvider(packOutput: PackOutput, private val future: CompletableFuture<HolderLookup.Provider>, protected val modId: String) : DataProvider {
    private val pathProvider: PackOutput.PathProvider = packOutput.createRegistryElementsPathProvider(Registries.RECIPE)
    protected lateinit var registries: HolderLookup.Provider
    protected lateinit var exporter: HTRecipeExporter

    override fun run(cache: CachedOutput): CompletableFuture<*> = future.thenCompose { registries: HolderLookup.Provider ->
        val recipes: MutableSet<RecipeKey> = hashSetOf()
        val tasks: MutableList<CompletableFuture<*>> = mutableListOf()
        this.registries = registries
        this.exporter = HTRecipeExporter { key: RecipeKey, recipe: Recipe<*>, conditions: List<ICondition> ->
            val fixedKey: RecipeKey = key.identifier().let(::modifyId).let(::RecipeKey)
            val id: Identifier = fixedKey.identifier()
            check(recipes.add(fixedKey)) { "Duplicate recipe $id" }
            tasks += DataProvider.saveStable(cache, registries, Recipe.CONDITIONAL_CODEC, Optional.of(WithConditions(conditions, recipe)), pathProvider.json(id))
        }
        buildRecipes()
        CompletableFuture.allOf(*tasks.toTypedArray())
    }

    protected abstract fun buildRecipes()

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

    fun materialPart(tagKey: TagKey<HTMaterialContents>, vararg parts: HTMaterialPartKey): Ingredient = HTMaterialPartIngredient(registries.getOrThrow(tagKey), listOf(*parts).sorted()).toVanilla()

    fun HTMaterialRawEntry.setHolderSet(builder: IngredientBuilder) {
        builder.apply {
            this@setHolderSet.map(
                { items { +it.asItem() } },
                { +holderSet(it) },
            )
        }
    }

    fun <T : Any> holderSet(tagKey: TagKey<T>): HolderSet<T> = this.registries.getOrThrow(tagKey)

    fun holderSet(prefix: HTTagPrefix, material: HTMaterialKey): HolderSet<Item> = holderSet(prefix.itemTagKey(material))

    fun holderSet(content: HTFluidContent): HolderSet<Fluid> = holderSet(content.fluidTag)
}
