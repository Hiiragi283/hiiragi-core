package hiiragi283.core.api.storage.attachments

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack

/**
 * @see mekanism.common.attachments.containers.item.AttachedItems
 */
@JvmRecord
data class HTAttachedItems(override val containers: List<ItemStack>) : HTAttachedContainers<ItemStack, HTAttachedItems> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTAttachedItems> = VanillaBiCodecs.ITEM_STACK
            .listOf()
            .xmap(::HTAttachedItems, HTAttachedItems::containers)

        @JvmField
        val EMPTY = HTAttachedItems(listOf())

        @JvmStatic
        fun create(size: Int): HTAttachedItems = HTAttachedItems(List(size) { ItemStack.EMPTY })
    }

    override fun create(containers: List<ItemStack>): HTAttachedItems = HTAttachedItems(containers)

    override fun equals(other: Any?): Boolean {
        when {
            this === other -> return true
            other !is HTAttachedItems -> return false
            else -> {
                val otherContainers: List<ItemStack> = other.containers
                return when {
                    containers.size != otherContainers.size -> {
                        false
                    }
                    else -> {
                        for (i: Int in containers.indices) {
                            if (!ItemStack.matches(containers[i], otherContainers[i])) return false
                        }
                        true
                    }
                }
            }
        }
    }

    override fun hashCode(): Int {
        var hash = 0
        for (stack: ItemStack in containers) {
            hash = hash * 31 + stack.hashCode()
        }
        return hash
    }
}
