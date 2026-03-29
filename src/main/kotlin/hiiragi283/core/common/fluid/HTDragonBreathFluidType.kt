package hiiragi283.core.common.fluid

import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.PowerParticleOption
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTDragonBreathFluidType(properties: Properties) : FluidType(properties) {
    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = true

    /**
     * @see net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball.onHit
     */
    override fun onVaporize(
        entity: LivingEntity?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        val vec: Vec3 = Vec3.atBottomCenterOf(pos)
        val cloud = AreaEffectCloud(level, vec.x, vec.y, vec.z)
        cloud.owner = entity
        cloud.setCustomParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1f))
        cloud.radius = 3f
        cloud.duration = 600
        cloud.radiusPerTick = (7f - cloud.radius) / cloud.duration.toFloat()
        cloud.addEffect(MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1))
        level.levelEvent(2006, pos, 1)
        level.addFreshEntity(cloud)
    }
}
