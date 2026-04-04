package hiiragi283.core.client.integration.jade

import hiiragi283.core.api.integration.jade.HTJadeProvider
import hiiragi283.core.api.integration.jade.toJade
import hiiragi283.core.api.transfer.getStack
import hiiragi283.core.api.util.fixedFraction
import hiiragi283.core.common.block.entity.HTCrucibleBlockEntity
import hiiragi283.core.setup.HCBlocks
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import snownee.jade.addon.universal.FluidStorageProvider
import snownee.jade.addon.universal.ItemStorageProvider
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.JadeIds
import snownee.jade.api.StreamServerDataProvider
import snownee.jade.api.config.IPluginConfig
import snownee.jade.api.ui.JadeUI

sealed interface HCCrucibleJadeProvider : HTJadeProvider {
    override fun getUid(): Identifier = HCBlocks.CRUCIBLE.getId()

    @JvmRecord
    data class Data(
        val progress: Int,
        val maxProgress: Int,
        val item: ItemStack,
        val fluid: FluidStack,
    ) {
        companion object {
            @JvmField
            val CODEC: StreamCodec<RegistryFriendlyByteBuf, Data> = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                Data::progress,
                ByteBufCodecs.VAR_INT,
                Data::maxProgress,
                ItemStack.OPTIONAL_STREAM_CODEC,
                Data::item,
                FluidStack.OPTIONAL_STREAM_CODEC,
                Data::fluid,
                ::Data,
            )
        }
    }

    data object Server : HCCrucibleJadeProvider, StreamServerDataProvider<BlockAccessor, Data> {
        override fun streamData(accessor: BlockAccessor): Data {
            val crucible: HTCrucibleBlockEntity = accessor.typedBlockEntity()
            return Data(
                crucible.handler.progress,
                crucible.handler.maxProgress,
                crucible.getItemHandler(null).getStack(0),
                crucible.getFluidHandler(null).getStack(0),
            )
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, Data> = Data.CODEC
    }

    /**
     * @see snownee.jade.addon.vanilla.FurnaceProvider.Client
     */
    data object Client : HCCrucibleJadeProvider, IBlockComponentProvider {
        override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
            Server.decodeFromData(accessor).ifPresent { (progress: Int, maxProgress: Int, item: ItemStack, fluid: FluidStack) ->
                if (item.isEmpty && fluid.isEmpty) return@ifPresent
                tooltip.remove(JadeIds.UNIVERSAL_ITEM_STORAGE)
                tooltip.remove(JadeIds.UNIVERSAL_FLUID_STORAGE)

                tooltip.add(JadeUI.item(item).alignSelfCenter())
                tooltip.append(
                    JadeUI
                        .progressArrow(fixedFraction(progress, maxProgress, true))
                        .alignSelfCenter()
                        .settings { it.paddingHorizontal(3) },
                )
                tooltip.append(JadeUI.fluid(fluid.toJade()).alignSelfCenter())
            }
        }

        override fun getDefaultPriority(): Int =
            maxOf(ItemStorageProvider.BLOCK.defaultPriority, FluidStorageProvider.BLOCK.defaultPriority) + 1
    }
}
