package hiiragi283.core.common.entity

import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCItems
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.event.EventHooks

class HTThrownBomb : ThrowableItemProjectile {
    constructor(entityType: EntityType<out HTThrownBomb>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(HCEntityTypes.BOMB.get(), shooter, level)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        HCEntityTypes.BOMB.get(),
        x,
        y,
        z,
        level,
    )

    /**
     * @see net.minecraft.world.entity.projectile.LargeFireball.onHit
     */
    override fun onHit(result: HitResult) {
        super.onHit(result)
        if (!level().isClientSide) {
            val flag: Boolean = EventHooks.canEntityGrief(level(), owner)
            // 爆発する
            level().explode(this, x, y, z, 2f, flag, Level.ExplosionInteraction.MOB)
            // 自身を消す
            discard()
        }
    }

    override fun getDefaultItem(): Item = HCItems.BOMB.get()
}
