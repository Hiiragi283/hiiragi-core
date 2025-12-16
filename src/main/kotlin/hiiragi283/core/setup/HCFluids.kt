package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.common.registry.register.HTFluidContentRegister
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType

object HCFluids {
    @JvmField
    val REGISTER = HTFluidContentRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmField
    val HONEY: HTSimpleFluidContent = REGISTER.registerSimple(
        "honey",
        create(SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_BREAK),
    ) { it.speedFactor(0.4f) }

    @JvmField
    val MUSHROOM_STEW: HTSimpleFluidContent = REGISTER.registerSimple("mushroom_stew", liquid())

    //    Extensions    //

    @JvmStatic
    private fun create(fill: SoundEvent, empty: SoundEvent): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, fill)
        .sound(SoundActions.BUCKET_EMPTY, empty)

    @JvmStatic
    private fun liquid(): FluidType.Properties = create(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)

    @JvmStatic
    private fun molten(): FluidType.Properties = create(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA)
}
