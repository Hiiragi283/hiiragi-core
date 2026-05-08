package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.unwrap
import hiiragi283.core.impl.registry.HTDeferredHolderLike
import hiiragi283.core.impl.registry.HTIntrusiveHolderLike
import hiiragi283.core.impl.registry.HTRegistryHolderLike
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredHolder

typealias HTSimpleHolderLike<R> = HTHolderLike<R, R>

/**
 * [ResourceKey]と値を保持する[HTKeyLike]の拡張インターフェースです。
 * @param R レジストリの要素のクラス
 * @param T 保持している値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTHolderLike<R : Any, T : R> :
    SupplierWithId<T>,
    HTKeyLike<R> {
    /**
     * @since 0.13.0
     */
    fun unwrap(): Either<ResourceKey<R>, Holder<R>>

    override fun getResourceKey(): ResourceKey<R> = unwrap().mapRight(Holder<R>::getKeyOrThrow).unwrap()
}

//    Extensions    //

/**
 * この[Holder][this]を[HTHolderLike]に変換します。
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@Suppress("UNCHECKED_CAST")
fun <R : Any> Holder<R>.toLike(): HTSimpleHolderLike<R> = HTRegistryHolderLike(this)

fun <R : Any> Holder<R>.getKeyOrThrow(): ResourceKey<R> = this.unwrapKey().orElseThrow { error("Unregistered holder: $this") }

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <R : Any, T : R> DeferredHolder<R, T>.toLike(): HTHolderLike<R, T> = HTDeferredHolderLike(this)

fun <T : Any> TypedInstance<T>.getHolderLike(): HTSimpleHolderLike<T> = this.typeHolder().toLike()

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
fun <BLOCK : Block> BLOCK.toLike(): HTBlockHolderLike<BLOCK> = object : HTIntrusiveHolderLike<Block, BLOCK>() {
    @Suppress("DEPRECATION")
    override fun getHolder(value: Block): Holder<Block> = value.builtInRegistryHolder()

    override fun get(): BLOCK = this@toLike

    override fun toString(): String = this@toLike.toString()
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTBlockHolderLike<*>.getDefaultState(): BlockState = this.get().defaultBlockState()

//    EntityType    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
typealias HTEntityHolderLike<ENTITY> = HTHolderLike<EntityType<*>, EntityType<ENTITY>>

/**
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
fun <ENTITY : Entity> EntityType<ENTITY>.toLike(): HTEntityHolderLike<ENTITY> = object : HTIntrusiveHolderLike<EntityType<*>, EntityType<ENTITY>>() {
    @Suppress("DEPRECATION")
    override fun getHolder(value: EntityType<*>): Holder<EntityType<*>> = value.builtInRegistryHolder()

    override fun get(): EntityType<ENTITY> = this@toLike
}
