package hiiragi283.core.common.block.entity

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.gui.slot.toSlot
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.util.HTModularUIHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

class HTTestBlockEntity(pos: BlockPos, state: BlockState) : HTModularBlockEntity(HCBlockEntityTypes.TEST, pos, state) {
    @DescSynced
    @Persisted(subPersisted = true)
    private val tank: HTBasicFluidTank = HTBasicFluidTank.create(HTConst.DEFAULT_FLUID_AMOUNT * 8)

    override fun createFluidHandler(): HTFluidTankHolder = object : HTFluidTankHolder {
        override fun getFluidTank(side: Direction?): List<HTFluidTank> = listOf(tank)

        override fun canInsert(side: Direction?): Boolean = true

        override fun canExtract(side: Direction?): Boolean = true
    }

    @DescSynced
    @Persisted(subPersisted = true)
    private val slot: HTBasicItemSlot = HTBasicItemSlot.create()

    override fun createItemHandler(): HTItemSlotHolder = object : HTItemSlotHolder {
        override fun getItemSlot(side: Direction?): List<HTItemSlot> = listOf(slot)

        override fun canInsert(side: Direction?): Boolean = true

        override fun canExtract(side: Direction?): Boolean = true
    }

    //    UI    //

    override fun setupElements(root: UIElement) {
        root.addChild(
            HTModularUIHelper
                .createRow()
                .addChild(slot.toSlot())
                .addChild(createFluidTank(0)),
        )
    }
}
