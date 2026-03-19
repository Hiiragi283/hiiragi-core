package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.unwrap
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

typealias HTSimpleHolderLike<R> = HTHolderLike<R, R>

/**
 * [ResourceKey]と値を保持する[HTKeyLike]の拡張インターフェースです。
 * @param R レジストリの要素のクラス
 * @param T 保持している値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTHolderLike<R : Any, T : R> :
    HTKeyLike<R>,
    Supplier<T> {
    /**
     * @since 0.13.0
     */
    fun unwrap(): Either<ResourceKey<R>, Holder<R>>

    /**
     * @since 0.13.0
     */
    fun getHolder(holderGetter: (ResourceKey<R>) -> Holder<R>): Holder<R> = unwrap().mapLeft(holderGetter).unwrap()

    override fun getResourceKey(): ResourceKey<R> = unwrap().mapRight(Holder<R>::unwrapKey.andThen { it.orElseThrow() }).unwrap()
}

//    Extensions    //

/**
 * この[Holder][this]を[HTHolderLike]に変換します。
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@Suppress("UNCHECKED_CAST")
fun <R : Any> Holder<R>.toLike(): HTSimpleHolderLike<R> = object : HTSimpleHolderLike<R> {
    override fun unwrap(): Either<ResourceKey<R>, Holder<R>> = Either.Right(this@toLike.delegate)

    override fun get(): R = this@toLike.value()

    override fun toString(): String = this@toLike.toString()
}

fun <R : Any> TypedInstance<R>.holderLike(): HTSimpleHolderLike<R> = this.typeHolder().toLike()

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <R : Any, T : R> DeferredHolder<R, T>.toLike(): HTHolderLike<R, T> = object : HTHolderLike<R, T> {
    override fun unwrap(): Either<ResourceKey<R>, Holder<R>> = Either.Right(this@toLike.delegate)

    override fun get(): T = this@toLike.get()

    override fun toString(): String = this@toLike.toString()
}

//    Block    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
typealias HTBlockHolderLike<BLOCK> = HTHolderLike<Block, BLOCK>

/**
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
typealias HTSimpleBlockHolderLike = HTBlockHolderLike<Block>

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <BLOCK : Block> BLOCK.toLike(): HTBlockHolderLike<BLOCK> = object : HTBlockHolderLike<BLOCK> {
    @Suppress("DEPRECATION")
    override fun unwrap(): Either<ResourceKey<Block>, Holder<Block>> = Either.Right(get().builtInRegistryHolder())

    override fun get(): BLOCK = this@toLike

    override fun toString(): String = this@toLike.toString()
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTBlockHolderLike<*>.getDefaultState(): BlockState = this.get().defaultBlockState()
