package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.ingredient.HTMaterialPartIngredient
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.toId
import java.util.concurrent.CompletableFuture
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition

abstract class HTRecipeProvider(protected val modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {
    val ingredientCreator = HTIngredientCreator(registries)
    val fluidCreator: HTFluidIngredientCreator by lazy { HTFluidIngredientCreator(registries.lookupOrThrow(Registries.FLUID)) }
    val itemCreator: HTItemIngredientCreator by lazy { HTItemIngredientCreator(registries.lookupOrThrow(Registries.ITEM)) }

    val resultCreator: HTResultCreator get() = HTResultCreator

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

    /**
     * 指定した[key]からエンチャントを取得します。
     * @since 0.12.0
     */
    fun getEnchantment(key: ResourceKey<Enchantment>): Holder<Enchantment> = registries.holderOrThrow(key)

    fun getHasName(id: HTIdLike): String = "has_${id.path}"

    fun getHasName(tagKey: TagKey<*>): String = "has_${tagKey.location().path.replace("/", "_")}"

    fun materialPart(tagKey: TagKey<HTMaterialContents>, vararg parts: HTMaterialPartKey): Ingredient = HTMaterialPartIngredient(registries.getOrThrow(tagKey), listOf(*parts).sorted()).toVanilla()

    //    Runner    //

    sealed class Runner(protected val modId: String, packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>, private val factory: Factory) : RecipeProvider.Runner(packOutput, registries) {
        final override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider {
            val output1: RecipeOutput = object : RecipeOutput {
                override fun accept(key: RecipeKey, recipe: Recipe<*>, advancement: AdvancementHolder?, vararg conditions: ICondition) {
                    val fixedKey: RecipeKey = key.identifier().let(::modifyId).let(::RecipeKey)
                    val fixedHolder: AdvancementHolder? = advancement?.let { AdvancementHolder(it.id().let(::modifyId), it.value()) }
                    output.accept(fixedKey, recipe, fixedHolder, *conditions)
                }

                override fun advancement(): Advancement.Builder = output.advancement()

                override fun includeRootAdvancement() {
                    output.includeRootAdvancement()
                }
            }.let(::modifyOutput)
            return factory.createProvider(modId, registries, output1)
        }

        /**
         * 指定した[id]を別の[ID][Identifier]に変換します。
         */
        protected abstract fun modifyId(id: Identifier): Identifier

        /**
         * 指定した[output]別の[RecipeOutput]に変換します。
         */
        protected open fun modifyOutput(output: RecipeOutput): RecipeOutput = output

        fun interface Factory {
            fun createProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput): HTRecipeProvider
        }
    }

    //    Direct    //

    abstract class Direct(modId: String, packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>, factory: Factory) : Runner(modId, packOutput, registries, factory) {
        final override fun modifyId(id: Identifier): Identifier = modId.toId(id.path)
    }

    //    Integration    //

    abstract class Integration(modId: String, private val integrationModId: String, packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>, factory: Factory) : Runner(modId, packOutput, registries, factory) {
        private val builtInIds: Set<String> = HTConstants.getBuiltInIdSet(modId)

        final override fun modifyId(id: Identifier): Identifier {
            val namespace: String = id.namespace
            val infix: String = when {
                namespace in builtInIds -> modId
                else -> namespace
            }
            val path: List<String> = id.path.split("/", limit = 2)
            return modId.toId(path[0], infix, path[1])
        }

        final override fun modifyOutput(output: RecipeOutput): RecipeOutput = output.withConditions(ModLoadedCondition(integrationModId))
    }
}
