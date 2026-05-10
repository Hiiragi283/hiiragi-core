package hiiragi283.lib.resource

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

    override fun getId(): Identifier = getResourceKey().identifier()
}
