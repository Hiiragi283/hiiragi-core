package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.fixedFraction
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.gui.sync.HTFractionSyncSlot
import hiiragi283.core.common.gui.sync.HTItemSyncSlot
import hiiragi283.core.common.gui.widget.HTFillDirection
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.gui.widget.HTProgressWidget
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.setup.HCBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

class HTTestBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.TEST, pos, state) {
    private lateinit var tank1: HTBasicFluidTank
    private lateinit var tank2: HTBasicFluidTank

    override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank1 = HTBasicFluidTank.create(listener, HTConst.DEFAULT_FLUID_AMOUNT * 8)
        tank2 = HTBasicFluidTank.create(listener, HTConst.DEFAULT_FLUID_AMOUNT * 8)
        return object : HTFluidTankHolder {
            override fun getFluidTank(side: Direction?): List<HTFluidTank> = listOf(tank1, tank2)

            override fun canInsert(side: Direction?): Boolean = true

            override fun canExtract(side: Direction?): Boolean = true
        }
    }

    private lateinit var slot1: HTBasicItemSlot
    private lateinit var slot2: HTBasicItemSlot

    override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder {
        slot1 = HTBasicItemSlot.create(listener)
        slot2 = HTBasicItemSlot.create(listener, limit = 1)
        return object : HTItemSlotHolder {
            override fun getItemSlot(side: Direction?): List<HTItemSlot> = listOf(slot1, slot2)

            override fun canInsert(side: Direction?): Boolean = true

            override fun canExtract(side: Direction?): Boolean = true
        }
    }

    fun setup(widgetHolder: HTWidgetHolder) {
        // tank
        widgetHolder += HTFluidWidget
            .createTank(tank1, HTSlotHelper.getSlotPosX(0), HTSlotHelper.getSlotPosY(0))
            .setBackground(HTBackgroundType.EXTRA_INPUT)
        widgetHolder += HTFluidWidget
            .createSlot(tank2, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(2))
            .setBackground(HTBackgroundType.EXTRA_OUTPUT)
            .setGhost()

        // slot
        for (i in (0..2)) {
            widgetHolder += HTItemSlotWidget(
                slot1,
                HTSlotHelper.getSlotPosX(3),
                HTSlotHelper.getSlotPosY(i),
                HTBackgroundType.INPUT,
            )
        }
        widgetHolder += HTItemSlotWidget(
            HTItemSyncSlot(slot2),
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.OUTPUT,
        )

        // progress
        widgetHolder += HTProgressWidget
            .createArrow(
                HTFractionSyncSlot.create(
                    { fixedFraction(ticks, 20 * 5, true) },
                    { fraction: Fraction -> ticks },
                ),
                HTSlotHelper.getSlotPosX(4),
                HTSlotHelper.getSlotPosY(1),
            ).setDirection(HTFillDirection.END_TO_TOP)
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = true
}
