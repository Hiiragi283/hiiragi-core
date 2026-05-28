package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.fixedFraction
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.gui.sync.HTFluidSyncSlot
import hiiragi283.core.common.gui.sync.HTItemSyncSlot
import hiiragi283.core.common.gui.widget.HTFillDirection
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.gui.widget.HTProgressWidget
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.setup.HCBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidType

class HTTestBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.TEST, pos, state) {
    private lateinit var tank1: HTBasicFluidTank
    private lateinit var tank2: HTBasicFluidTank

    override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank1 = HTBasicFluidTank.create(listener, FluidType.BUCKET_VOLUME * 8)
        tank2 = HTBasicFluidTank.create(listener, FluidType.BUCKET_VOLUME * 8)
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
        widgetHolder += HTFluidWidget.Tank(tank1, HTSlotHelper.getSlotPosX(0), HTSlotHelper.getSlotPosY(0), HTBackgroundType.EXTRA_INPUT, false)
        widgetHolder += HTFluidWidget.Slot(tank1, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(2), HTBackgroundType.EXTRA_OUTPUT, true)
        widgetHolder.track(HTFluidSyncSlot(tank1), HTSyncType.BOTH)
        // slot
        for (i: Int in (0..2)) {
            widgetHolder += HTItemWidget.Container(
                slot1,
                HTSlotHelper.getSlotPosX(3),
                HTSlotHelper.getSlotPosY(i),
                HTBackgroundType.INPUT,
            )
        }
        widgetHolder += HTItemWidget.Fake(
            slot2,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(HTItemSyncSlot(slot2), HTSyncType.C2S)

        // progress
        widgetHolder += HTProgressWidget
            .createArrow(
                { fixedFraction(ticks, 20 * 5, true) },
                HTSlotHelper.getSlotPosX(4),
                HTSlotHelper.getSlotPosY(1),
            ).setDirection(HTFillDirection.END_TO_TOP)
            .setSupportedRecipeTypes(HCRecipeViewerTypes.BREWING)
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = true
}
