package hiiragi283.lib.recipe.result

import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * アイテムと液体の完成品をまとめて管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTItemAndFluidResult private constructor(val item: ItemStack, val fluid: FluidStack) {
    companion object {
        /**
         * 空の[HTItemAndFluidResult]のインスタンス
         */
        @JvmField
        val EMPTY = HTItemAndFluidResult(ItemStack.EMPTY, FluidStack.EMPTY)

        /**
         * 新しい[HTItemAndFluidResult]のインスタンスを作成します。
         * @return [item]と[fluid]が共に空の場合は[EMPTY]
         */
        @JvmStatic
        fun create(item: ItemStack, fluid: FluidStack): HTItemAndFluidResult = when {
            item.isEmpty && fluid.isEmpty -> EMPTY
            else -> HTItemAndFluidResult(item, fluid)
        }

        /**
         * アイテムのみを持つ，新しい[HTItemAndFluidResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun create(item: ItemStack): HTItemAndFluidResult = create(item, FluidStack.EMPTY)

        /**
         * 液体のみを持つ，新しい[HTItemAndFluidResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun create(fluid: FluidStack): HTItemAndFluidResult = create(ItemStack.EMPTY, fluid)

        /**
         * アイテムと液体の両方を持つ，新しい[HTItemAndFluidResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun create(pair: Pair<ItemStack, FluidStack>): HTItemAndFluidResult = create(pair.first, pair.second)

        /**
         * アイテムまたは液体の片方を持つ，新しい[HTItemAndFluidResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun create(either: Either<ItemStack, FluidStack>): HTItemAndFluidResult = either.fold(::create, ::create)

        /**
         * 少なくともアイテムか液体の片方を持つ，新しい[HTItemAndFluidResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun create(ior: Ior<ItemStack, FluidStack>): HTItemAndFluidResult = ior.fold(::create, ::create, ::create)
    }

    /**
     * 完成品が空かどうか判定します。
     */
    fun isEmpty(): Boolean = this == EMPTY || item.isEmpty && fluid.isEmpty
}
