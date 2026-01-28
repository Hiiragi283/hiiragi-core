package hiiragi283.core.common.gui.factory

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.gui.menu.HTWidgetContainerMenu
import hiiragi283.core.setup.HCMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.Nameable
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

@JvmRecord
data class HTBlockWidgetHolderContext(val factory: Factory, val player: Player, val pos: BlockPos) :
    HTWidgetHolderContext,
    MenuProvider {
    companion object {
        @JvmStatic
        fun create(containerId: Int, inventory: Inventory, buffer: RegistryFriendlyByteBuf): HTWidgetContainerMenu {
            val player: Player = inventory.player
            val pos: BlockPos = buffer.readBlockPos()
            val block: Block = player.level().getBlockState(pos).block
            if (block is Factory) {
                val context: HTBlockWidgetHolderContext = block.createContext(player, pos)
                return HTWidgetContainerMenu(HCMenuTypes.BLOCK.get(), containerId, inventory, context)
            } else {
                error("Cannot create menu from $block at $pos")
            }
        }
    }

    val level: Level get() = player.level()
    val state: BlockState get() = level.getBlockState(pos)
    val blockEntity: BlockEntity? get() = level.getBlockEntity(pos)

    override fun setup(player: Player, widgetHolder: HTWidgetHolder) {
        factory.setup(this, widgetHolder)
    }

    override fun stillValid(player: Player): Boolean = factory.stillValid(this)

    override fun getDisplayName(): Component = factory.getDisplayName(this)

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTWidgetContainerMenu =
        HTWidgetContainerMenu(HCMenuTypes.BLOCK.get(), containerId, playerInventory, this)

    override fun writeClientSideData(menu: AbstractContainerMenu, buffer: RegistryFriendlyByteBuf) {
        buffer.writeBlockPos(pos)
    }

    //    Factory    //

    fun interface Factory {
        fun setup(context: HTBlockWidgetHolderContext, widgetHolder: HTWidgetHolder)

        fun createContext(player: Player, pos: BlockPos): HTBlockWidgetHolderContext = HTBlockWidgetHolderContext(this, player, pos)

        fun stillValid(context: HTBlockWidgetHolderContext): Boolean {
            if (!context.level.isInWorldBounds(context.pos)) return false
            val blockEntity: BlockEntity = context.blockEntity ?: return true
            return !blockEntity.isRemoved
        }

        fun getDisplayName(context: HTBlockWidgetHolderContext): Component =
            (context.blockEntity as? Nameable)?.displayName ?: context.state.block.name
    }
}
