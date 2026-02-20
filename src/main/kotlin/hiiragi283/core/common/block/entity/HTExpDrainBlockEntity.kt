package hiiragi283.core.common.block.entity

import hiiragi283.core.api.registry.toStack
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.util.HTExperienceHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * @see mekanism.common.tile.TileEntityChargepad
 */
class HTExpDrainBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.EXP_DRAIN, pos, state) {
    companion object {
        @JvmStatic
        private fun canDrainExp(entity: LivingEntity): Boolean = !entity.isSpectator && entity is Player
    }

    private var tankCache: BlockCapabilityCache<IFluidHandler, Direction?>? = null

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (tankCache == null) {
            tankCache = HTFluidCapabilities.createCache(level, pos.below(), Direction.UP, listener = {
                tankCache = null
            })
        }
        val belowHandler: IFluidHandler = tankCache?.capability ?: return false
        val vec3: Vec3 = Vec3.atLowerCornerOf(pos)
        val entities: List<LivingEntity> = level.getEntitiesOfClass(
            LivingEntity::class.java,
            AABB(
                vec3,
                vec3.add(1.0, 0.4, 1.0),
            ),
            ::canDrainExp,
        )
        for (entity: LivingEntity in entities) {
            if (entity is Player) {
                val oldExp: Int = HTExperienceHelper.getPlayerExp(entity)
                val drainPerTick: Int = HTExperienceHelper.getExpRatio() * 10
                var pointToDrain: Int = minOf(oldExp, drainPerTick)
                var expAmountToFill: Int = HTExperienceHelper.fluidAmountFromExp(pointToDrain)
                val inserted: Int = belowHandler.fill(HCFluids.EXPERIENCE.toStack(expAmountToFill), IFluidHandler.FluidAction.SIMULATE)
                expAmountToFill = inserted - (inserted % HTExperienceHelper.getExpRatio())
                pointToDrain = HTExperienceHelper.expAmountFromFluid(expAmountToFill)
                belowHandler.fill(HCFluids.EXPERIENCE.toStack(expAmountToFill), IFluidHandler.FluidAction.EXECUTE)
                HTExperienceHelper.setPlayerExp(entity, (oldExp - pointToDrain).toLong())
            }
        }
        return false
    }
}
