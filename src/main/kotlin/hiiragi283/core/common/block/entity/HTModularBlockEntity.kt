package hiiragi283.core.common.block.entity

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.data.FillDirection
import com.lowdragmc.lowdraglib2.gui.ui.elements.FluidSlot
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.util.HTModularUIHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

abstract class HTModularBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state) {
    fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI = HTModularUIHelper.createUIWithInv(holder.player, name, ::setupElements)

    protected abstract fun setupElements(root: UIElement)

    protected fun createFluidSlot(index: Int): FluidSlot = FluidSlot().bind(this, 0)

    protected fun createFluidTank(index: Int): FluidSlot {
        val slot: FluidSlot = createFluidSlot(index)
        slot.layout.setHeight(18 * 3 - 2f)
        slot.slotStyle.fillDirection(FillDirection.DOWN_TO_UP)
        return slot
    }
}
