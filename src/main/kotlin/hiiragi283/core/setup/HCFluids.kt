package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.common.fluid.HTEndFluidType
import hiiragi283.core.common.fluid.HTNetherFluidType
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

    //    Vanilla    //

    @JvmField
    val HONEY: HTSimpleFluidContent = REGISTER.registerSimple(
        "honey",
        create(SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_BREAK),
    ) { it.speedFactor(0.4f) }

    @JvmField
    val MUSHROOM_STEW: HTSimpleFluidContent = REGISTER.registerSimple("mushroom_stew", liquid())

    //    Saps    //

    @JvmField
    val LATEX: HTSimpleFluidContent = REGISTER.registerSimple("latex", liquid())

    @JvmField
    val CRIMSON_SAP: HTSimpleFluidContent = REGISTER.registerSimple("crimson_sap", liquid())

    @JvmField
    val WARPED_SAP: HTSimpleFluidContent = REGISTER.registerSimple("warped_sap", liquid())

    //    Molten    //

    @JvmField
    val CRIMSON_BLOOD: HTSimpleFluidContent =
        REGISTER.register("crimson_blood", molten().temperature(2300), ::HTNetherFluidType)

    @JvmField
    val DEW_OF_THE_WARP: HTSimpleFluidContent =
        REGISTER.register("dew_of_the_warp", molten().temperature(1300), ::HTNetherFluidType)

    @JvmField
    val ELDRITCH_FLUX: HTSimpleFluidContent =
        REGISTER.register("eldritch_flux", molten().temperature(1300), ::HTEndFluidType)

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
