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
 * アイテムと液体をやり取りするレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
sealed interface HTTankInteraction {
    /**
     * 必要な液体量
     */
    val amount: Int

    interface Emptying : HTTankInteraction {
        /**
         * 指定した[容器][container]から液体を搬出できるか判定します。
         * @return 搬出できる場合は`true`
         */
        fun canEmptyContainer(container: HTItemResourceType): Boolean

        /**
         * 指定した[容器][container]から液体を取り出します。
         * @return 空の容器と保持していた液体
         */
        fun emptyContainer(container: HTItemResourceType): Pair<ItemStack, FluidStack>
    }

    interface Filling : HTTankInteraction {
        /**
         * 指定した[容器][container]に[液体][fluidStack]を搬入できるか判定します。
         * @return 搬入できる場合は`true`
         */
        fun canFillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): Boolean

        /**
         * 指定した[容器][container]に[液体][fluidStack]を搬入します。
         * @return 液体入りの容器
         */
        fun fillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): ItemStack
    }

    /**
     * JSONに変換可能な[HTTankInteraction]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.14.0
     */
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
