package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.NotCondition
import net.neoforged.neoforge.common.conditions.TagEmptyCondition
import java.util.function.UnaryOperator

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[Recipe]のビルダークラスです。
 * @param BUILDER [HTRecipeBuilder]を継承したクラス
 * @param prefix レシピIDに使われる前置詞
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.datagen.recipe.MekanismRecipeBuilder
 */
abstract class HTRecipeBuilder<BUILDER : HTRecipeBuilder<BUILDER>>(private val prefix: String) {
    @Suppress("UNCHECKED_CAST")
    protected fun self(): BUILDER = this as BUILDER

    //    ICondition    //

    private val conditions: MutableList<ICondition> = mutableListOf()

    fun tagCondition(prefix: HTPrefixLike, material: HTMaterialLike): BUILDER = tagCondition(prefix.itemTagKey(material))

    /**
     * 指定した[tagKey]が存在する時に読み込むよう，条件を指定します。
     * @return このビルダーのインスタンス
     */
    fun tagCondition(tagKey: TagKey<Item>): BUILDER = addCondition(NotCondition(TagEmptyCondition(tagKey)))

    /**
     * [読み込みの条件][condition]を追加します。
     * @return このビルダーのインスタンス
     */
    fun addCondition(condition: ICondition): BUILDER {
        this.conditions.add(condition)
        return self()
    }

    //    Save    //

    /**
     * [ID][getPrimalId]を[prefix]で前置した値でレシピを生成します。
     */
    fun savePrefixed(recipeOutput: RecipeOutput, prefix: String) {
        save(recipeOutput, getPrimalId().withPrefix(prefix))
    }

    /**
     * [ID][getPrimalId]を[suffix]で後置した値でレシピを生成します。
     */
    fun saveSuffixed(recipeOutput: RecipeOutput, suffix: String) {
        save(recipeOutput, getPrimalId().withSuffix(suffix))
    }

    /**
     * [ID][getPrimalId]を[operator]で修飾した値でレシピを生成します。
     */
    fun saveModified(recipeOutput: RecipeOutput, operator: UnaryOperator<String>) {
        save(recipeOutput, getPrimalId().withPath(operator))
    }

    /**
     * [ID][getPrimalId]でレシピを生成します。
     */
    fun save(recipeOutput: RecipeOutput) {
        save(recipeOutput, getPrimalId())
    }

    /**
     * [ID][id]でレシピを生成します。
     */
    fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(
            id.withPrefix("$prefix/"),
            createRecipe(),
            null,
            *conditions.toTypedArray(),
        )
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
