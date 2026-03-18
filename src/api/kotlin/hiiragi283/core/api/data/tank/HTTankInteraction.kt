package hiiragi283.core.api.data.tank

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
interface HTTankInteraction {
    val amount: Int

    fun canEmptyContainer(container: HTItemResourceType): Boolean

    fun emptyContainer(container: HTItemResourceType): Pair<ItemStack, FluidStack>

    fun canFillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): Boolean

    fun fillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): ItemStack

    interface Serializable : HTTankInteraction {
        companion object {
            @JvmField
            val CODEC: Codec<Serializable> = HCRegistries.TANK_INTERACTION_TYPE
                .byNameCodec()
                .dispatch(Serializable::type, identity())
        }

        fun type(): MapCodec<out Serializable>
    }
}
