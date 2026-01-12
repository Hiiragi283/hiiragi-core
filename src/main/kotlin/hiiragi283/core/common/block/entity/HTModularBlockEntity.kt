package hiiragi283.core.common.block.entity

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.elements.FluidSlot
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

abstract class HTModularBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state) {
    abstract fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI

    protected fun createFluidSlot(index: Int): FluidSlot = FluidSlot().bind(this, index)
}
