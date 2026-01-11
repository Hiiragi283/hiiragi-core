package hiiragi283.core.common.block.entity

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.data.FillDirection
import com.lowdragmc.lowdraglib2.gui.ui.elements.FluidSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots
import com.lowdragmc.lowdraglib2.gui.ui.style.LayoutStyle
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager
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
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import org.appliedenergistics.yoga.YogaEdge
import org.appliedenergistics.yoga.YogaFlexDirection
import org.appliedenergistics.yoga.YogaGutter
import org.appliedenergistics.yoga.YogaJustify

class HTTestBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.TEST, pos, state) {
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

    /**
     * @see com.lowdragmc.lowdraglib2.test.TestBlockEntity.createUI
     */
    override fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI {
        val root: UIElement = UIElement()
            .layout { style: LayoutStyle ->
                style
                    .setPadding(YogaEdge.ALL, 4f)
                    .setGap(YogaGutter.ALL, 2f)
                    .setJustifyContent(YogaJustify.CENTER)
            }.addClass("panel_bg")

        root.addChildren(Label().setText(displayName))
        root.addChild(
            UIElement()
                .layout { style: LayoutStyle -> style.setFlexDirection(YogaFlexDirection.ROW) }
                .addChild(slot.toSlot())
                .addChild(
                    FluidSlot().bind(this, 0).slotStyle { style: FluidSlot.SlotStyle ->
                        style.fillDirection(FillDirection.DOWN_TO_UP)
                    },
                ),
        )
        root.addChild(InventorySlots())
        return ModularUI(
            UI.of(
                root,
                StylesheetManager.INSTANCE.getStylesheetSafe(StylesheetManager.MC),
            ),
            holder.player,
        )
    }
}
