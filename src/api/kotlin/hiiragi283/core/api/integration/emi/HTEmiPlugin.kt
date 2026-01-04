package hiiragi283.core.api.integration.emi

import com.mojang.logging.LogUtils
import dev.emi.emi.EmiPort
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipe
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import org.slf4j.Logger

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[EmiPlugin]の抽象クラスです。
 * @param modId 対象のMOD ID
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTEmiPlugin(protected val modId: String) : EmiPlugin {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        protected val ITEM_LOOKUP: HolderLookup.RegistryLookup<Item> by lazy(EmiPort.getItemRegistry()::asLookup)

        @JvmStatic
        protected val FLUID_LOOKUP: HolderLookup.RegistryLookup<Fluid> by lazy {
            EmiPort.getFluidRegistry().asLookup().filterElements { fluid: Fluid ->
                fluid != Fluids.EMPTY && fluid.isSource(fluid.defaultFluidState())
            }
        }
    }

    //    Extensions    //

    /**
     * 指定した[カテゴリ][category]を[レジストリ][registry]に登録します。
     */
    protected fun addCategory(registry: EmiRegistry, category: HTEmiRecipeCategory) {
        registry.addCategory(category)
        category.workStations.forEach(registry::addWorkstation.partially1(category))
    }

    /**
     * レシピが登録されなかったときにログを出力します。
     */
    protected fun skipRecipe(id: ResourceLocation) {
        LOGGER.warn("Skipped recipe for EMI registration: $id")
    }

    /**
     * [レシピID][id]に基づいてレシピを追加します。
     */
    protected inline fun addRecipeSafe(registry: EmiRegistry, id: ResourceLocation, factory: (ResourceLocation) -> EmiRecipe) {
        runCatching {
            registry.addRecipe(factory(id))
        }.onFailure { throwable: Throwable ->
            LOGGER.warn("Exception thrown when parsing vanilla recipe: $id", throwable)
        }
    }

    protected inline fun addCraftingRecipe(
        registry: EmiRegistry,
        id: ResourceLocation,
        prefix: String,
        factory: (ResourceLocation) -> EmiRecipe,
    ) {
        addRecipeSafe(registry, id.withPrefix("/${HTConst.SHAPELESS}/$modId/$prefix"), factory)
    }

    /**
     * [EmiRegistry.getRecipeManager]に基づいてレシピを追加します。
     * @param BASE [RecipeType]のレシピのクラス
     * @param RECIPE [BASE]を継承したクラス
     * @param EMI_RECIPE EMIに登録するレシピのクラス
     * @param recipeType レシピのタイプ
     * @param factory [RECIPE]を[EMI_RECIPE]に変換するブロック
     * @see mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes
     */
    protected inline fun <INPUT : RecipeInput, BASE : HTRecipe<INPUT>, reified RECIPE : BASE, EMI_RECIPE : EmiRecipe> addRegistryRecipes(
        registry: EmiRegistry,
        recipeType: HTHolderLike<RecipeType<*>, RecipeType<BASE>>,
        noinline factory: (RecipeHolder<RECIPE>) -> EMI_RECIPE?,
    ) {
        registry.recipeManager
            .getAllRecipesFor(recipeType.get())
            .asSequence()
            .mapNotNull { holder: RecipeHolder<BASE> ->
                val id: ResourceLocation = holder.id
                val recipe: BASE = holder.value
                if (recipe is RECIPE) {
                    RecipeHolder(id, recipe)
                } else {
                    skipRecipe(id)
                    null
                }
            }.mapNotNull(factory)
            .forEach(registry::addRecipe)
    }

    /**
     * レシピの一覧を登録します。
     * @param RECIPE 元となるレシピのクラス
     * @param EMI_RECIPE EMIに登録するレシピのクラス
     * @param recipes [ResourceLocation]と[RECIPE]のペアの一覧
     * @param factory [ResourceLocation]と[RECIPE]を[EMI_RECIPE]に変換するブロック
     */
    protected fun <RECIPE : Any, EMI_RECIPE : EmiRecipe> addRecipes(
        registry: EmiRegistry,
        recipes: Sequence<Pair<ResourceLocation, RECIPE>>,
        factory: Factory<RECIPE, EMI_RECIPE>,
    ) {
        recipes.mapNotNull(factory::create).forEach(registry::addRecipe)
    }

    fun interface Factory<RECIPE : Any, EMI_RECIPE : EmiRecipe> {
        fun create(id: ResourceLocation, recipe: RECIPE): EMI_RECIPE?

        fun create(pair: Pair<ResourceLocation, RECIPE>): EMI_RECIPE? = create(pair.first, pair.second)
    }
}
