package hiiragi283.core.api

import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.world.item.DyeColor

interface HTColoredContents<T : HTHolderLike<*, *>> : Iterable<Pair<HTDefaultColor, T>> {
    operator fun get(color: HTDefaultColor): T?

    operator fun get(color: DyeColor): T?

    fun interface Simple<T : HTHolderLike<*, *>> : HTColoredContents<T> {
        override fun get(color: HTDefaultColor): T? = color.dyeColor.let(::get)

        override fun iterator(): Iterator<Pair<HTDefaultColor, T>> =
            HTDefaultColor.entries.mapNotNull { color: HTDefaultColor -> get(color)?.let { color to it } }.iterator()
    }
}
