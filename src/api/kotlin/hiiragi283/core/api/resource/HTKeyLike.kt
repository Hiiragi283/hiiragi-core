package hiiragi283.core.api.resource

import hiiragi283.core.api.registry.keyOrThrow
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

fun interface HTKeyLike<T : Any> : HTIdLike {
    fun getResourceKey(): ResourceKey<T>

    override fun getId(): ResourceLocation = getResourceKey().location()

    fun interface HolderDelegate<T : Any> : HTKeyLike<T> {
        fun getHolder(): Holder<T>

        override fun getResourceKey(): ResourceKey<T> = getHolder().keyOrThrow
    }
}
