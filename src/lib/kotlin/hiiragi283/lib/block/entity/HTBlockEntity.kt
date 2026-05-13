package hiiragi283.lib.block.entity

import hiiragi283.lib.HTConstants
import hiiragi283.lib.level.HTItemDropHelper
import hiiragi283.lib.text.Text
import hiiragi283.lib.transfer.HTHandlerProvider
import hiiragi283.lib.transfer.ItemResourceHandler
import hiiragi283.lib.transfer.indices
import java.util.UUID
import net.minecraft.core.BlockPos
import net.minecraft.core.UUIDUtil
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Nameable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemUtil

abstract class HTBlockEntity(type: BlockEntityType<*>, worldPosition: BlockPos, blockState: BlockState) :
    HTExtendedBlockEntity(type, worldPosition, blockState),
    Nameable,
    HTHandlerProvider,
    HTOwnedBlockEntity,
    HTSoundPlayerBlockEntity {

    var ticks: Int = 0
        protected set

    protected open fun onUpdateClient(level: Level, pos: BlockPos, state: BlockState) {}

    protected abstract fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

    open fun onBlockRemoved(state: BlockState, level: Level, pos: BlockPos) {
        if (shouldDrop(state, level, pos)) {
            val handler: ItemResourceHandler = getItemHandler(null) ?: return
            for (i: Int in handler.indices) {
                ItemUtil.getStack(handler, i).let { HTItemDropHelper.dropStackAt(level, pos, it) }
            }
        }
    }

    protected open fun shouldDrop(state: BlockState, level: Level, pos: BlockPos): Boolean = true

    //    Save & Read    //

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        // Custom Name
        output.storeNullable("custom_name", ComponentSerialization.CODEC, this.customName)
        // Owner
        output.storeNullable(HTConstants.OWNER, UUIDUtil.CODEC, ownerId)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        // Custom Name
        input.read("custom_name", ComponentSerialization.CODEC).ifPresent(::customName::set)
        // Owner
        input.read(HTConstants.OWNER, UUIDUtil.CODEC).ifPresent(::ownerId::set)
    }

    //    Nameable    //

    private var customName: Text? = null

    final override fun getName(): Text = customName ?: blockState.block.name

    final override fun getCustomName(): Text? = customName

    //    HTOwnedBlockEntity    //

    var ownerId: UUID? = null

    override fun getOwner(): UUID? = ownerId
}
