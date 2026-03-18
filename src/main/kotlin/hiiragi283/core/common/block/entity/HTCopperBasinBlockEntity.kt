package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

class HTCopperBasinBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.COPPER_BASIN, pos, state) {
    lateinit var tank: HTBasicFluidTank
        private set

    override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTBasicFluidTank.create(listener, 4000)
        return object : HTFluidTankHolder {
            override fun getFluidTank(side: Direction?): List<HTFluidTank> = listOf(tank)

            override fun canInsert(side: Direction?): Boolean = true

            override fun canExtract(side: Direction?): Boolean = true
        }
    }

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStackSlotHelper.calculateRedstoneLevel(tank)

    private var oldScale: Fraction = Fraction.ZERO

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val scale: Fraction = tank.getLevelAsFraction()
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        tank.serialize(output)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        tank.deserialize(input)
    }
}
