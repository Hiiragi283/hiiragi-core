package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTStackRecipeBuilder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.ItemLike
import java.util.function.IntUnaryOperator
import kotlin.math.max

/**
 * [AbstractCookingRecipe]向けの[HTStackRecipeBuilder]の実装クラスです。
 * @param factory [AbstractCookingRecipe]を作成するブロック
 * @param timeOperator レシピの処理時間を修飾するブロック
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
class HTCookingRecipeBuilder(
    prefix: String,
    private val factory: AbstractCookingRecipe.Factory<*>,
    stack: ItemStack,
    private val timeOperator: IntUnaryOperator = IntUnaryOperator.identity(),
) : HTStackRecipeBuilder.Single<HTCookingRecipeBuilder>(prefix, stack) {
    companion object {
        /**
         * [かまどレシピ][SmeltingRecipe]のみを登録するビルダーを作成します。
         */
        @JvmStatic
        fun smelting(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder = smelting(ItemStack(item, count))

        /**
         * [かまどレシピ][SmeltingRecipe]のみを登録するビルダーを作成します。
         */
        @JvmStatic
        fun smelting(stack: ItemStack): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            HTConst.SMELTING,
            ::SmeltingRecipe,
            stack,
        )

        /**
         * [溶鉱炉レシピ][BlastingRecipe]のみを登録するビルダーを作成します。
         */
        @JvmStatic
        fun blasting(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder = blasting(ItemStack(item, count))

        /**
         * [溶鉱炉レシピ][BlastingRecipe]のみを登録するビルダーを作成します。
         */
        @JvmStatic
        fun blasting(stack: ItemStack): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            HTConst.BLASTING,
            ::BlastingRecipe,
            stack,
        )

        /**
         * [燻製器レシピ][SmokingRecipe]のみを登録するビルダーを作成します。
         */
        @JvmStatic
        fun smoking(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder = smoking(ItemStack(item, count))

        /**
         * [燻製器レシピ][SmokingRecipe]のみを登録するビルダーを作成します。
         */
        @JvmStatic
        fun smoking(stack: ItemStack): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            HTConst.SMOKING,
            ::SmokingRecipe,
            stack,
        )

        /**
         * [かまどレシピ][SmeltingRecipe]と[溶鉱炉レシピ][BlastingRecipe]を同時に登録します。
         *
         * [溶鉱炉レシピ][BlastingRecipe]の処理時間は，[かまどレシピ][SmeltingRecipe]の半分に自動的に置き換えられます。
         */
        @HTBuilderMarker
        @JvmStatic
        inline fun smeltingAndBlasting(item: ItemLike, count: Int = 1, builderAction: HTCookingRecipeBuilder.() -> Unit) {
            smelting(item, count).apply(builderAction)
            HTCookingRecipeBuilder(HTConst.BLASTING, ::BlastingRecipe, ItemStack(item, count)) { it / 2 }.apply(builderAction)
        }

        /**
         * [かまどレシピ][SmeltingRecipe]と[燻製器レシピ][SmokingRecipe]を同時に登録します。
         *
         * [燻製器レシピ][SmokingRecipe]の処理時間は，[かまどレシピ][SmeltingRecipe]の半分に自動的に置き換えられます。
         */
        @HTBuilderMarker
        @JvmStatic
        inline fun smeltingAndSmoking(item: ItemLike, count: Int = 1, builderAction: HTCookingRecipeBuilder.() -> Unit) {
            smelting(item, count).apply(builderAction)
            HTCookingRecipeBuilder(HTConst.SMOKING, ::SmokingRecipe, ItemStack(item, count)) { it / 2 }.apply(builderAction)
        }
    }

    private var group: String? = null
    private var time: Int = 200
    private var exp: Float = 0f

    /**
     * レシピのグループを指定します。
     */
    fun setGroup(group: String?): HTCookingRecipeBuilder = apply {
        this.group = group
    }

    /**
     * レシピの処理時間を指定します。
     */
    fun setTime(time: Int): HTCookingRecipeBuilder = apply {
        this.time = max(0, time)
    }

    /**
     * レシピの経験値を指定します。
     */
    fun setExp(exp: Float): HTCookingRecipeBuilder = apply {
        this.exp = max(0f, exp)
    }

    override fun createRecipe(output: ItemStack): AbstractCookingRecipe =
        factory.create(group ?: "", CookingBookCategory.MISC, ingredient, output, exp, timeOperator.applyAsInt(time))
}
