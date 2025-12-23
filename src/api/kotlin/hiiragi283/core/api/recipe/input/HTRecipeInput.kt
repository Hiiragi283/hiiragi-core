package hiiragi283.core.api.recipe.input

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.api.stack.ImmutableStack
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import java.util.*

/**
 * [HTRecipe]で使用される，[RecipeInput]を実装したクラスです。
 * @param pos レシピを実行する座標
 * @param items アイテムの入力の一覧
 * @param fluids 液体の入力の一覧
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTRecipeInput private constructor(
    val pos: BlockPos?,
    val items: List<ImmutableItemStack?>,
    val fluids: List<ImmutableFluidStack?>,
) : RecipeInput {
    companion object {
        /**
         * [HTRecipeInput]を作成します。
         * @param pos レシピを実行する座標
         */
        @JvmStatic
        inline fun create(pos: BlockPos?, builderAction: Builder.() -> Unit): HTRecipeInput? = Builder().apply(builderAction).build(pos)

        /**
         * 指定した[stacks]が空か判定します。
         * @return [List.isEmpty]または[stacks]の中身がすべて`null`の場合は`true`
         */
        @JvmStatic
        fun <STACK : ImmutableStack<*, STACK>> isEmpty(stacks: List<STACK?>): Boolean = stacks.isEmpty() || stacks.filterNotNull().isEmpty()
    }

    private fun validateItem(index: Int): ImmutableItemStack? = items
        .getOrNull(index)
    // ?.takeUnless { stack: ImmutableItemStack -> stack.isOf(RagiumModTags.Items.IGNORED_IN_RECIPES) }

    /**
     * [SingleRecipeInput]に変換します。
     * @return アイテムがない場合は`null`
     */
    fun toSingleItem(): SingleRecipeInput? = item(0)?.unwrap()?.let(::SingleRecipeInput)

    /**
     * 指定した[インデックス][index]から[アイテム][ImmutableItemStack]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合，またはアイテムがない場合は`null`
     */
    fun item(index: Int): ImmutableItemStack? = validateItem(index)

    /**
     * 指定した[インデックス][index]から[液体][ImmutableFluidStack]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合，または液体がない場合は`null`
     */
    fun fluid(index: Int): ImmutableFluidStack? = fluids.getOrNull(index)

    /**
     * 指定した[インデックス][index]にあるアイテムが[ingredient]に一致するか判定します。
     */
    fun testItem(index: Int, ingredient: HTItemIngredient): Boolean = testItem(index, ingredient::test)

    /**
     * 指定した[インデックス][index]にあるアイテムが[predicate]に一致するか判定します。
     * @return 指定した[インデックス][index]が範囲外の場合，またはアイテムがない場合は`false`
     */
    inline fun testItem(index: Int, predicate: (ImmutableItemStack) -> Boolean): Boolean = item(index)?.let(predicate) ?: false

    /**
     * 指定した[インデックス][index]にあるアイテムが[ingredient]に一致するか判定します。
     * - [item]の戻り値が`null`の場合は[Optional.isEmpty]に基づいて判定。
     * - [item]の戻り値が存在し，かつ[ingredient]が存在する場合は[HTItemIngredient.test]に基づいて判定。
     * - [item]の戻り値が存在し，かつ[ingredient]が存在しない場合は`false`
     */
    fun testItem(index: Int, ingredient: Optional<HTItemIngredient>): Boolean {
        val stack: ImmutableItemStack = item(index) ?: return ingredient.isEmpty
        return when {
            ingredient.isPresent -> ingredient.get().test(stack)
            else -> false
        }
    }

    /**
     * 指定した[インデックス][index]にある液体が[ingredient]に一致するか判定します。
     */
    fun testFluid(index: Int, ingredient: HTFluidIngredient): Boolean = testFluid(index, ingredient::test)

    /**
     * 指定した[インデックス][index]にある液体が[predicate]に一致するか判定します。
     * @return 指定した[インデックス][index]が範囲外の場合，または液体がない場合は`false`
     */
    inline fun testFluid(index: Int, predicate: (ImmutableFluidStack) -> Boolean): Boolean = fluid(index)?.let(predicate) ?: false

    /**
     * 指定した[インデックス][index]にあるアイテムが[ingredient]に一致するか，種類のみで判定します。
     * @return 指定した[インデックス][index]が範囲外の場合，またはアイテムがない場合は`false`
     */
    fun testCatalyst(index: Int, ingredient: HTItemIngredient): Boolean = item(index)?.let(ingredient::testOnlyType) ?: false

    /**
     * 指定した[インデックス][index]にあるアイテムが[ingredient]に一致するか，種類のみで判定します。
     * - [item]の戻り値が`null`の場合は[Optional.isEmpty]に基づいて判定。
     * - [item]の戻り値が存在し，かつ[ingredient]が存在する場合は[HTItemIngredient.test]に基づいて判定。
     * - [item]の戻り値が存在し，かつ[ingredient]が存在しない場合は`false`
     */
    fun testCatalyst(index: Int, ingredient: Optional<HTItemIngredient>): Boolean {
        val stack: ImmutableItemStack = item(index) ?: return ingredient.isEmpty
        return when {
            ingredient.isPresent -> ingredient.get().testOnlyType(stack)
            else -> false
        }
    }

    //    RecipeInput    //

    /**
     * @suppress
     */
    @Deprecated("Use `item(Int)` instead", ReplaceWith("this.item(Int)"), DeprecationLevel.ERROR)
    override fun getItem(index: Int): ItemStack = item(index)?.unwrap() ?: ItemStack.EMPTY

    /**
     * @suppress
     */
    override fun size(): Int = items.size

    /**
     * @suppress
     */
    override fun isEmpty(): Boolean = pos == null && isEmpty(items) && isEmpty(fluids)

    //    Builder    //

    /**
     * [HTRecipeInput]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    class Builder {
        /**
         * アイテムの一覧
         */
        val items: MutableList<ImmutableItemStack?> = mutableListOf()

        /**
         * 液体の一覧
         */
        val fluids: MutableList<ImmutableFluidStack?> = mutableListOf()

        /**
         * [HTRecipeInput]を返します。
         */
        fun build(pos: BlockPos?): HTRecipeInput? = HTRecipeInput(pos, items, fluids).takeUnless(HTRecipeInput::isEmpty)
    }
}
