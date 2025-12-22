package hiiragi283.core.api.resource

import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

/**
 * [ResourceKey]を保持する[HTIdLike]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTHolderLike
 */
fun interface HTKeyLike<T : Any> : HTIdLike {
    /**
     * 保持している[ResourceKey]を返します。
     */
    fun getResourceKey(): ResourceKey<T>

    override fun getId(): ResourceLocation = getResourceKey().location()

    /**
     * [Holder]を保持する[HTKeyLike]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     * @see HTDeferredHolder
     */
    fun interface HolderDelegate<T : Any> : HTKeyLike<T> {
        /**
         * 保持している[Holder]を返します。
         */
        fun getHolder(): Holder<T>

        override fun getResourceKey(): ResourceKey<T> = getHolder().unwrapKey().orElseThrow()
    }
}
