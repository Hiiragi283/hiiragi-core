package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.conditions.NotCondition
import net.neoforged.neoforge.common.conditions.TagEmptyCondition

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[Recipe]のビルダークラスです。
 * @param prefix レシピIDに使われる前置詞
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTRecipeBuilder(private val prefix: String) {
    //    Conditions    //

    val conditions: Conditions = Conditions()

    inner class Conditions {
        private val conditions: MutableList<ICondition> = mutableListOf()

        @JvmName("addModCondition")
        operator fun plusAssign(modId: String) {
            this.plusAssign(ModLoadedCondition(modId))
        }

        @JvmName("addTagCondition")
        operator fun plusAssign(pair: Pair<HTTagPrefix, HTMaterialLike>) {
            val (prefix: HTTagPrefix, material: HTMaterialLike) = pair
            this.plusAssign(prefix.itemTagKey(material))
        }

        @JvmName("addTagCondition")
        operator fun plusAssign(tagKey: TagKey<Item>) {
            this.plusAssign(NotCondition(TagEmptyCondition(tagKey)))
        }

        @JvmName("addCondition")
        operator fun plusAssign(condition: ICondition) {
            conditions += condition
        }

        fun toArray(): Array<ICondition> = conditions.toTypedArray()
    }

    //    Save    //

    val recipeId: RecipeId by lazy(::RecipeId)

    inner class RecipeId {
        var id: ResourceLocation = getPrimalId()
            private set

        infix fun prefix(prefix: String) {
            id = id.withPrefix(prefix)
        }

        infix fun suffix(suffix: String) {
            id = id.withSuffix(suffix)
        }

        infix fun replace(newId: ResourceLocation) {
            id = newId
        }
    }

    /**
     * レシピを生成します。
     */
    fun save(recipeOutput: RecipeOutput) {
        recipeOutput.accept(recipeId.id.withPrefix("$prefix/"), createRecipe(), null, *conditions.toArray())
    }

    /**
     * デフォルトのIDを取得します。
     */
    protected abstract fun getPrimalId(): ResourceLocation

    /**
     * 生成されるレシピを作成します。
     */
    protected abstract fun createRecipe(): Recipe<*>
}
