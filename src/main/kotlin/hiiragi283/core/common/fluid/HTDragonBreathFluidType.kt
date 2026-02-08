package hiiragi283.core.common.fluid

import hiiragi283.core.api.fluid.HTFluidType
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.fluids.FluidStack

class HTDragonBreathFluidType(properties: Properties) : HTFluidType(properties) {
    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = true

    /**
     * @see net.minecraft.world.entity.projectile.DragonFireball.onHit
     */
    override fun onVaporize(
        player: Player?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        val vec: Vec3 = Vec3.atBottomCenterOf(pos)
        val cloud = AreaEffectCloud(level, vec.x, vec.y, vec.z)
        cloud.owner = player
        cloud.particle = ParticleTypes.DRAGON_BREATH
        cloud.radius = 3f
        cloud.duration = 600
        cloud.radiusPerTick = (7f - cloud.radius) / cloud.duration.toFloat()
        cloud.addEffect(MobEffectInstance(MobEffects.HARM, 1, 1))
        level.levelEvent(2006, pos, 1)
        level.addFreshEntity(cloud)
    }
}
