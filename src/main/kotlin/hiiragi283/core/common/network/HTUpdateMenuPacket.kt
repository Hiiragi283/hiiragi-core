package hiiragi283.core.common.network

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.core.api.network.HTCustomPayload
import hiiragi283.core.common.inventory.container.HTContainerMenu
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

/**
 * [HTSyncablePayload]による同期で使用される[HTCustomPayload]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.network.to_client.container.PacketUpdateContainer
 */
data class HTUpdateMenuPacket(val containerId: Int, val index: Int, val payload: HTSyncablePayload) :
    HTCustomPayload.S2C,
    HTCustomPayload.C2S {
    companion object {
        @JvmField
        val TYPE: CustomPacketPayload.Type<HTUpdateMenuPacket> = CustomPacketPayload.Type(HiiragiCoreAPI.id("update_menu"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateMenuPacket> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            HTUpdateMenuPacket::containerId,
            ByteBufCodecs.VAR_INT,
            HTUpdateMenuPacket::index,
            HTSyncablePayload.STREAM_CODEC,
            HTUpdateMenuPacket::payload,
            ::HTUpdateMenuPacket,
        )
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateMenuPacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        val container: HTContainerMenu = player.containerMenu as? HTContainerMenu ?: return
        if (container.containerId == this.containerId) {
            payload.setValue(container, index)
        }
    }

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        val container: HTContainerMenu = player.containerMenu as? HTContainerMenu ?: return
        if (container.containerId == this.containerId) {
            payload.setValue(container, index)
        }
    }
}
