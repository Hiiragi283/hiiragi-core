package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.entity.HTThrownCaptureEgg
import hiiragi283.core.common.registry.HTDeferredEntityType
import hiiragi283.core.common.registry.register.HTDeferredEntityTypeRegister
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

object HCEntityTypes {
    @JvmField
    val REGISTER = HTDeferredEntityTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        eventBus.addListener(::registerEntityCapabilities)

        REGISTER.register(eventBus)
    }

    @JvmField
    val ELDRITCH_EGG: HTDeferredEntityType<HTThrownCaptureEgg> = registerThrowable("eldritch_egg", ::HTThrownCaptureEgg)

    @JvmStatic
    private fun <T : Entity> registerThrowable(name: String, factory: EntityType.EntityFactory<T>): HTDeferredEntityType<T> =
        REGISTER.registerType(name, factory, MobCategory.MISC) { builder: EntityType.Builder<T> ->
            builder
                .sized(0.25f, 0.25f)
                .clientTrackingRange(4)
                .updateInterval(10)
        }

    //    Event    //

    // Capabilities
    @JvmStatic
    private fun registerEntityCapabilities(event: RegisterCapabilitiesEvent) {}
}
