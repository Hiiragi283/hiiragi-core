package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTDeferredEntityType
import hiiragi283.core.api.registry.HTDeferredEntityTypeRegister
import hiiragi283.core.common.entity.HTThrownBomb
import hiiragi283.core.common.entity.HTThrownCaptureEgg
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object HCEntityTypes {
    @JvmField
    val REGISTER = HTDeferredEntityTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val BOMB: HTDeferredEntityType<HTThrownBomb> = registerThrowable("bomb", ::HTThrownBomb)

    @JvmField
    val ELDRITCH_EGG: HTDeferredEntityType<HTThrownCaptureEgg> = registerThrowable("eldritch_egg", ::HTThrownCaptureEgg)

    @JvmStatic
    private fun <T : Entity> registerThrowable(name: String, factory: EntityType.EntityFactory<T>): HTDeferredEntityType<T> = REGISTER.registerType(name, factory, MobCategory.MISC) { builder: EntityType.Builder<T> ->
        builder
            .sized(0.25f, 0.25f)
            .clientTrackingRange(4)
            .updateInterval(10)
    }
}
