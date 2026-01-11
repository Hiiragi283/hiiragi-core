package hiiragi283.core.common

import com.lowdragmc.lowdraglib2.plugin.ILDLibPlugin
import com.lowdragmc.lowdraglib2.plugin.LDLibPlugin
import com.lowdragmc.lowdraglib2.syncdata.AccessorRegistries
import com.lowdragmc.lowdraglib2.syncdata.accessor.IAccessor
import com.lowdragmc.lowdraglib2.syncdata.accessor.direct.CustomDirectAccessor
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.resource.HTResourceType
import net.minecraft.network.RegistryFriendlyByteBuf

@LDLibPlugin
class HTLDLibPlugin : ILDLibPlugin {
    override fun onLoad() {
        // Fraction

        // Resource Type
        AccessorRegistries.registerAccessor(resourceType(HTFluidResourceType.CODEC))
        AccessorRegistries.registerAccessor(resourceType(HTItemResourceType.CODEC))
    }

    private inline fun <reified T : HTResourceType<*>> resourceType(codec: BiCodec<RegistryFriendlyByteBuf, T>): IAccessor<T> =
        CustomDirectAccessor
            .builder(T::class.java)
            .codec(codec)
            .build()

    private fun <T : Any> CustomDirectAccessor.Builder<T>.codec(
        codec: BiCodec<in RegistryFriendlyByteBuf, T>,
    ): CustomDirectAccessor.Builder<T> = this.codec(codec.codec).streamCodec(codec.streamCodec)
}
