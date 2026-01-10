package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.common.fluid.HTEndFluidType
import hiiragi283.core.common.fluid.HTMoltenMetalFluid
import hiiragi283.core.common.fluid.HTMoltenMetalFluidType
import hiiragi283.core.common.fluid.HTNetherFluidType
import hiiragi283.core.common.item.HTMoltenMetalBucketItem
import hiiragi283.core.common.registry.register.HTFluidContentRegister
import hiiragi283.core.common.registry.register.HTSimpleFluidContent
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
    val EXPERIENCE: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("experience", liquid())

    @JvmField
    val HONEY: HTSimpleFluidContent = REGISTER.registerSimpleFlowing(
        "honey",
        create(SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_BREAK),
    ) { it.speedFactor(0.4f) }

    @JvmField
    val MUSHROOM_STEW: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("mushroom_stew", liquid())

    //    Organic    //

    @JvmField
    val LATEX: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("latex", liquid())

    @JvmField
    val BLOOD: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("blood", liquid())

    @JvmField
    val MEAT: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("meat", liquid())

    //    Material    //

    @JvmField
    val MOLTEN_METAL: HTFluidContent<HTMoltenMetalFluidType, HTMoltenMetalFluid, HTMoltenMetalBucketItem> = REGISTER.register(
        "molten_metal",
        molten(),
        ::HTMoltenMetalFluidType,
        ::HTMoltenMetalFluid,
        ::HTMoltenMetalBucketItem,
    )

    @JvmField
    val MOLTEN_GLASS: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("molten_glass", molten())

    @JvmField
    val MOLTEN_CRIMSON_CRYSTAL: HTSimpleFluidContent =
        REGISTER.registerFlowing("molten_crimson_crystal", molten().temperature(2300), ::HTNetherFluidType)

    @JvmField
    val MOLTEN_WARPED_CRYSTAL: HTSimpleFluidContent =
        REGISTER.registerFlowing("molten_warped_crystal", molten().temperature(1300), ::HTNetherFluidType)

    @JvmField
    val MOLTEN_ELDRITCH: HTSimpleFluidContent =
        REGISTER.registerFlowing("molten_eldritch", molten().temperature(1300), ::HTEndFluidType)

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
