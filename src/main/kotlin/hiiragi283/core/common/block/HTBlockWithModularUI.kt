package hiiragi283.core.common.block

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import hiiragi283.core.common.block.entity.HTModularBlockEntity
import hiiragi283.core.util.HTModularUIHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * [HTModularBlockEntity]に基づいてUIを提供する[BlockUIMenuType.BlockUI]の拡張インターフェースです。
 */
interface HTBlockWithModularUI : BlockUIMenuType.BlockUI {
    override fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI {
        val player: Player = holder.player
        val blockEntity: BlockEntity? = player.level().getBlockEntity(holder.pos)
        if (blockEntity is HTModularBlockEntity) {
            return blockEntity.createUI(holder)
        }
        return HTModularUIHelper.createEmptyUI(player)
    }
}
