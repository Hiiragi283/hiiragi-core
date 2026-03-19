package hiiragi283.core.api.resource

import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * [ResourceKey]を保持する[HTIdLike]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTHolderLike
 */
fun interface HTKeyLike<R : Any> : HTIdLike {
    /**
     * 保持している[ResourceKey]を返します。
     */
    fun getResourceKey(): ResourceKey<R>

    /**
     * 指定した[key]が保持している[ResourceKey]と一致するか判定します。
     * @since 0.6.0
     */
    fun isOf(key: ResourceKey<R>): Boolean = key == getResourceKey()

    /**
     * 指定した[other]と保持している[ResourceKey]が一致するか判定します。
     * @since 0.6.0
     */
    fun isOf(other: HTKeyLike<R>): Boolean = isOf(other.getResourceKey())

    override fun getId(): Identifier = getResourceKey().identifier()
}
