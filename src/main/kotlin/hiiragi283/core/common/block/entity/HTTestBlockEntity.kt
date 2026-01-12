package hiiragi283.core.common.block.entity

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.style.LayoutStyle
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.gui.HTModularUIHelper
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.setup.HCBlockEntityTypes
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

    override fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI = HTModularUIHelper.createVanillaUI(holder.player, name) {
        addRowChild { alineCenter() }
            .addChild(HTItemSlotElement(slot))
            .addChild(HTModularUIHelper.rightArrowIcon().layout { style: LayoutStyle -> style.marginHorizontalPercent(5f) })
            .addChild(createFluidSlot(0))
    }
}
