package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.TagsUpdatedEvent

/**
 * 複数の[HTRecipeLookup]を束ねた[HTRecipeLookup]の実装クラスです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
class HTCompoundRecipeLookup<out RECIPE> private constructor(private val id: ResourceLocation) : HTRecipeLookup<RECIPE> {
    @EventBusSubscriber
    companion object {
        @JvmStatic
        private val instances: MutableMap<ResourceLocation, HTCompoundRecipeLookup<*>> = hashMapOf()

        /**
         * 新しい[HTCompoundRecipeLookup]のインスタンスを作成します。
         */
        @JvmStatic
        fun <RECIPE : Any> create(id: ResourceLocation): HTCompoundRecipeLookup<RECIPE> {
            val recipeType = HTCompoundRecipeLookup<RECIPE>(id)
            check(instances.put(id, recipeType) == null) { "Duplicated recipe type $id" }
            return recipeType
        }

        @SubscribeEvent
        fun clearCache(event: TagsUpdatedEvent) {
            // Clear cached recipes
            instances.values.forEach(HTCompoundRecipeLookup<*>::clearCache)
        }
    }

    private val lookups: MutableList<HTRecipeLookup<RECIPE>> = mutableListOf()
    private var cachedRecipes: Map<ResourceLocation, RECIPE> = mapOf()

    private fun clearCache() {
        cachedRecipes = mapOf()
    }

    /**
     * レシピの一覧を追加します。
     */
    fun addRecipes(vararg recipes: Pair<ResourceLocation, @UnsafeVariance RECIPE>) {
        addSubLookup { recipes.toMap() }
    }

    /**
     * [HTRecipeLookup]を追加します。
     */
    fun addSubLookup(lookup: HTRecipeLookup<@UnsafeVariance RECIPE>) {
        check(lookup != this)
        this.lookups += lookup
    }

    override fun getAllRecipes(context: HTRecipeLookup.Context): Map<ResourceLocation, RECIPE> {
        if (cachedRecipes.isEmpty()) {
            val recipes: MutableMap<ResourceLocation, RECIPE> = mutableMapOf()
            for (lookup in lookups) {
                recipes += lookup.getAllRecipes(context)
            }
            cachedRecipes = recipes
        }
        return cachedRecipes
    }

    override fun toString(): String = "HTCompoundRecipeLookup(id=$id)"
}

//    Extensions    //

/**
 * バニラの[RecipeType]からレシピの一覧を追加します。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE [HTCompoundRecipeLookup]のレシピのクラス
 * @param VANILLA_RECIPE バニラの[Recipe]を継承したクラス
 * @param recipeType バニラの[RecipeType]
 * @param transform [VANILLA_RECIPE]を[RECIPE]に変換するブロック
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <INPUT : RecipeInput, RECIPE : Any, VANILLA_RECIPE : Recipe<INPUT>> HTCompoundRecipeLookup<RECIPE>.fromRecipeType(
    recipeType: RecipeType<VANILLA_RECIPE>,
    transform: (VANILLA_RECIPE) -> RECIPE?,
) {
    this.addSubLookup { context ->
        val map: MutableMap<ResourceLocation, RECIPE> = mutableMapOf()
        for ((id: ResourceLocation, recipe: VANILLA_RECIPE) in context.getAllRecipes(recipeType)) {
            if (recipe.isIncomplete) continue
            val recipe1: RECIPE = transform(recipe) ?: continue
            map[id] = recipe1
        }
        map
    }
}
