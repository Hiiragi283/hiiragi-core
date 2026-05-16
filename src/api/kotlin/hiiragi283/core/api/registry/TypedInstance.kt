package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import java.util.stream.Stream
import org.jetbrains.annotations.ApiStatus

/**
 * 最新版のMinecraftからバックポートされたインターフェースです。
 * @since 0.15.2
 * @see net.minecraft.world.entity.Entity
 * @see net.minecraft.world.item.ItemStack
 * @see net.minecraft.world.level.block.state.BlockState
 * @see net.minecraft.world.level.material.FluidState
 */
interface TypedInstance<T : Any> {
    @ApiStatus.OverrideOnly
    fun typeHolder(): Holder<T> = throw AssertionError()

    fun tags(): Stream<TagKey<T>> = typeHolder().tags()

    fun isOf(tagKey: TagKey<T>): Boolean = typeHolder().`is`(tagKey)

    fun isOf(holderSet: HolderSet<T>): Boolean = typeHolder() in holderSet

    fun isOf(other: T): Boolean = typeHolder().value() == other
}
