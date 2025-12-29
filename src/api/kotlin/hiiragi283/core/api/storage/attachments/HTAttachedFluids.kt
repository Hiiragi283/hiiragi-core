package hiiragi283.core.api.storage.attachments

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @see mekanism.common.attachments.containers.fluid.AttachedFluids
 */
@JvmRecord
data class HTAttachedFluids(override val containers: List<FluidStack>) : HTAttachedContainers<FluidStack, HTAttachedFluids> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTAttachedFluids> = VanillaBiCodecs.FLUID_STACK
            .listOf()
            .xmap(::HTAttachedFluids, HTAttachedFluids::containers)

        @JvmField
        val EMPTY = HTAttachedFluids(listOf())

        @JvmStatic
        fun create(size: Int): HTAttachedFluids = HTAttachedFluids(List(size) { FluidStack.EMPTY })
    }

    override fun create(containers: List<FluidStack>): HTAttachedFluids = HTAttachedFluids(containers)

    override fun equals(other: Any?): Boolean {
        when {
            this === other -> return true
            other !is HTAttachedFluids -> return false
            else -> {
                val otherContainers: List<FluidStack> = other.containers
                return when {
                    containers.size != otherContainers.size -> {
                        false
                    }
                    else -> {
                        for (i: Int in containers.indices) {
                            if (!FluidStack.matches(containers[i], otherContainers[i])) return false
                        }
                        true
                    }
                }
            }
        }
    }

    override fun hashCode(): Int {
        var hash = 0
        for (stack: FluidStack in containers) {
            hash = hash * 31 + stack.hashCode()
        }
        return hash
    }
}
