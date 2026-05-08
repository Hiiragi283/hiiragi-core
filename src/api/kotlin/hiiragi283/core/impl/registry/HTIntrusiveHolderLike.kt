package hiiragi283.core.impl.registry

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * [Item]や[Block]のように[Holder]を保持するクラス向けの[HTHolderLike]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
abstract class HTIntrusiveHolderLike<R : Any, T : R> : HTHolderLike<R, T> {
    /**
     * [value]が保持している[Holder]を取得します。
     */
    protected abstract fun getHolder(value: R): Holder<R>

    final override fun unwrap(): Either<ResourceKey<R>, Holder<R>> = get().let(::getHolder).let { Either.right(it) }
}
