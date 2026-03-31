package hiiragi283.core.common.network

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.network.HTCustomPayload
import hiiragi283.core.impl.block.entity.HTExtendedBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.util.ProblemReporter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.storage.TagValueInput
import net.minecraft.world.level.storage.ValueInput

/**
 * @see mekanism.common.network.to_client.PacketUpdateTile
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTUpdateBlockEntityPacket private constructor(val pos: BlockPos, val updateTag: CompoundTag) : HTCustomPayload.S2C {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTUpdateBlockEntityPacket>(HiiragiCoreAPI.id("update_block_entity"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateBlockEntityPacket> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTUpdateBlockEntityPacket::pos,
            ByteBufCodecs.TRUSTED_COMPOUND_TAG,
            HTUpdateBlockEntityPacket::updateTag,
            ::HTUpdateBlockEntityPacket,
        )

        @JvmStatic
        fun create(blockEntity: HTExtendedBlockEntity): HTUpdateBlockEntityPacket? {
            val access: RegistryAccess = blockEntity.getRegistryAccess() ?: return null
            return HTUpdateBlockEntityPacket(blockEntity.blockPos, blockEntity.getReducedUpdateTag(access))
        }
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateBlockEntityPacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        val level: Level = player.level()
        val blockEntity: BlockEntity = level.getBlockEntity(pos) ?: return
        val reporter = ProblemReporter.ScopedCollector(blockEntity.problemPath(), HiiragiCoreAPI.LOGGER)
        val input: ValueInput = TagValueInput.create(reporter, level.registryAccess(), updateTag)
        blockEntity.handleUpdateTag(input)
    }
}
