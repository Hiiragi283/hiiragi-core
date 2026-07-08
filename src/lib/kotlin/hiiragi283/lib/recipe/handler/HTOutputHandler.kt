package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.fluid.toResourcePair
import hiiragi283.lib.transfer.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの出力スロットを表すインターフェースです。
 *
 * 参照 : [Mekanism - IOutputHandler](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/inputs/IOutputHandler.java)
 * @param T 搬入するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun interface HTOutputHandler<T : Resource> {
    companion object {
        @JvmStatic
        fun <T : Resource> single(handler: ResourceHandler<T>, index: Int): HTOutputHandler<T> = Single(handler, index)

        @JvmStatic
        fun <T : Resource> multiple(handler: ResourceHandler<T>): HTOutputHandler<T> = Multiple(handler)
    }

    /**
     * 完成品を搬入します。
     * @return 実際に搬入される数量
     */
    fun insert(resource: T, amount: Int, transaction: TransactionContext): Result<Int>

    /**
     * 単一のスロットに対する[HTOutputHandler]の実装クラスです。
     */
    private class Single<T : Resource>(private val handler: ResourceHandler<T>, private val index: Int) : HTOutputHandler<T> {
        override fun insert(resource: T, amount: Int, transaction: TransactionContext): Result<Int> = runCatching { handler.insert(index, resource, amount, transaction) }
    }

    /**
     * 複数のスロットに対する[HTOutputHandler]の実装クラスです。
     */
    private class Multiple<T : Resource>(private val handler: ResourceHandler<T>) : HTOutputHandler<T> {
        override fun insert(resource: T, amount: Int, transaction: TransactionContext): Result<Int> = runCatching { handler.insert(resource, amount, transaction) }
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun HTOutputHandler<ItemResource>.insert(stack: ItemStack, transaction: TransactionContext): Result<Int> {
    val (resource: ItemResource, amount: Int) = stack.toResourcePair()
    return this.insert(resource, amount, transaction)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun HTOutputHandler<FluidResource>.insert(stack: FluidStack, transaction: TransactionContext): Result<Int> {
    val (resource: FluidResource, amount: Int) = stack.toResourcePair()
    return this.insert(resource, amount, transaction)
}
