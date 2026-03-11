package hiiragi283.core.api.resource

import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

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

    override fun getId(): ResourceLocation = getResourceKey().location()

    /**
     * [Holder]を保持する[HTKeyLike]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @Deprecated("Removed", level = DeprecationLevel.ERROR)
    fun interface HolderDelegate<R : Any> : HTKeyLike<R> {
        /**
         * 保持している[Holder]を返します。
         */
        fun getHolder(): Holder<R>

        /**
         * 指定した[value]が保持している[Holder]の値と一致するか判定します。
         * @since 0.6.0
         */
        fun isOf(value: R): Boolean = getHolder().value() == value

        /**
         * 指定した[tagKey]が保持している[Holder]に含まれるか判定します。
         * @since 0.6.0
         */
        fun isOf(tagKey: TagKey<R>): Boolean = getHolder().`is`(tagKey)

        /**
         * 指定した[holder]が保持している[Holder]と一致するか判定します。
         * @since 0.6.0
         */
        @Suppress("DEPRECATION")
        fun isOf(holder: Holder<R>): Boolean = getHolder().`is`(holder)

        /**
         * 指定した[holderSet]に保持している[Holder]が含まれるか判定します。
         * @since 0.6.0
         */
        fun isOf(holderSet: HolderSet<R>): Boolean = holderSet.contains(getHolder())

        override fun getResourceKey(): ResourceKey<R> = getHolder().unwrapKey().orElseThrow()
    }
}
