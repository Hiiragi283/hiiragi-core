package hiiragi283.core.api.recipe.input

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.stack.ImmutableFluidStack
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
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
data class HTRecipeInput private constructor(val pos: BlockPos?, val items: List<ItemStack>, val fluids: List<FluidStack>) : RecipeInput {
    companion object {
        /**
         * [HTRecipeInput]を作成します。
         * @param pos レシピを実行する座標
         */
        @JvmStatic
        inline fun create(pos: BlockPos?, builderAction: Builder.() -> Unit): HTRecipeInput? = Builder().apply(builderAction).build(pos)
    }

    private fun validateItem(index: Int): ItemStack = items
        .getOrNull(index) ?: ItemStack.EMPTY
    // ?.takeUnless { stack: ImmutableItemStack -> stack.isOf(RagiumModTags.Items.IGNORED_IN_RECIPES) }

    /**
     * [SingleRecipeInput]に変換します。
     */
    fun toSingleItem(): SingleRecipeInput = SingleRecipeInput(item(0))

    /**
     * 指定した[インデックス][index]から[アイテム][ItemStack]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合，またはアイテムがない場合は[ItemStack.EMPTY]
     */
    fun item(index: Int): ItemStack = validateItem(index)

    /**
     * 指定した[インデックス][index]から[液体][ImmutableFluidStack]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合，または液体がない場合は[FluidStack.EMPTY]
     */
    fun fluid(index: Int): FluidStack = fluids.getOrNull(index) ?: FluidStack.EMPTY

    /**
     * 指定した[インデックス][index]にあるアイテムが[ingredient]に一致するか判定します。
     */
    fun testItem(index: Int, ingredient: HTItemIngredient): Boolean = testItem(index, ingredient::test)

    /**
     * 指定した[インデックス][index]にあるアイテムが[predicate]に一致するか判定します。
     * @return 指定した[インデックス][index]が範囲外の場合，またはアイテムがない場合は`false`
     */
    inline fun testItem(index: Int, predicate: (ItemStack) -> Boolean): Boolean = item(index).let(predicate)

    /**
     * 指定した[インデックス][index]にあるアイテムが[ingredient]に一致するか判定します。
     * - [item]の戻り値が`null`の場合は[Optional.isEmpty]に基づいて判定。
     * - [item]の戻り値が存在し，かつ[ingredient]が存在する場合は[HTItemIngredient.test]に基づいて判定。
     * - [item]の戻り値が存在し，かつ[ingredient]が存在しない場合は`false`
     */
    fun testItem(index: Int, ingredient: Optional<HTItemIngredient>): Boolean {
        val stack: ItemStack = item(index)
        return when {
            stack.isEmpty -> ingredient.isEmpty
            else -> when {
                ingredient.isPresent -> ingredient.get().test(stack)
                else -> false
            }
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
    inline fun testFluid(index: Int, predicate: (FluidStack) -> Boolean): Boolean = fluid(index).let(predicate) ?: false

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
        val stack: ItemStack = item(index)
        return when {
            stack.isEmpty -> ingredient.isEmpty
            else -> when {
                ingredient.isPresent -> ingredient.get().testOnlyType(stack)
                else -> false
            }
        }
    }

    //    RecipeInput    //

    /**
     * @suppress
     */
    @Deprecated("Use `item(Int)` instead", ReplaceWith("this.item(Int)"), DeprecationLevel.ERROR)
    override fun getItem(index: Int): ItemStack = item(index)

    /**
     * @suppress
     */
    override fun size(): Int = items.size

    /**
     * @suppress
     */
    override fun isEmpty(): Boolean {
        val bool1: Boolean = pos == null
        val bool2: Boolean = items.isEmpty() || items.all(ItemStack::isEmpty)
        val bool3: Boolean = fluids.isEmpty() || fluids.all(FluidStack::isEmpty)
        return bool1 && bool2 && bool3
    }

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
        val items: MutableList<ItemStack> = mutableListOf()

        /**
         * 液体の一覧
         */
        val fluids: MutableList<FluidStack> = mutableListOf()

        /**
         * [HTRecipeInput]を返します。
         */
        fun build(pos: BlockPos?): HTRecipeInput? = HTRecipeInput(pos, items, fluids).takeUnless(HTRecipeInput::isEmpty)
    }
}
