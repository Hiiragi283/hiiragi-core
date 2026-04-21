package hiiragi283.core.api.resource

import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

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

    override fun getId(): ResourceLocation = getResourceKey().location()
}
