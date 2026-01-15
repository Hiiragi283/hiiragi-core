package hiiragi283.core.common.block.entity

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import hiiragi283.core.api.gui.element.HTFluidSlotElement
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

abstract class HTModularBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state) {
    abstract fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI

    protected fun createFluidSlot(index: Int): HTFluidSlotElement = HTFluidSlotElement(this, index)
}
