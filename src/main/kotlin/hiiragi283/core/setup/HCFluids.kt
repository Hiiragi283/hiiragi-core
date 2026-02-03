package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.common.fluid.HTEndFluidType
import hiiragi283.core.common.fluid.HTNetherFluidType
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.register.HTFluidContentRegister
import hiiragi283.core.common.registry.register.HTSimpleFluidContent
import hiiragi283.core.common.registry.register.HTVirtualFluidContent
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
    val EXPERIENCE: HTVirtualFluidContent = REGISTER.registerVirtual("experience", liquid(), ::FluidType)

    @JvmField
    val HONEY: HTSimpleFluidContent = REGISTER.registerSimpleFlowing(
        "honey",
        create(SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_BREAK),
    ) { it.speedFactor(0.4f) }

    @JvmField
    val MUSHROOM_STEW: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("mushroom_stew", liquid())

    @JvmField
    val DRAGON_BREATH: HTVirtualFluidContent = REGISTER.registerVirtual("dragon_breath", liquid().density(-1000))

    //    Organic    //

    @JvmField
    val LATEX: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("latex", liquid())

    @JvmField
    val BLOOD: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("blood", liquid())

    @JvmField
    val MEAT: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("meat", liquid())

    //    Material    //

    @JvmStatic
    private fun molten(
        key: HTMaterialKey,
        temp: Int = 1300,
        typeFactory: (FluidType.Properties) -> FluidType = ::FluidType,
    ): HTSimpleFluidContent = REGISTER.registerFlowing("molten_${key.asMaterialId().path}", molten().temperature(temp), typeFactory)

    // Vanilla
    @JvmField
    val MOLTEN_GLASS: HTSimpleFluidContent = molten(VanillaMaterialKeys.GLASS)

    // Common
    @JvmField
    val MOLTEN_PLASTIC: HTSimpleFluidContent = molten(CommonMaterialKeys.PLASTIC)

    @JvmField
    val MOLTEN_RUBBER: HTSimpleFluidContent = molten(CommonMaterialKeys.RUBBER)

    // Hiiragi Core
    @JvmField
    val MOLTEN_CRIMSON_CRYSTAL: HTSimpleFluidContent = molten(HCMaterialKeys.CRIMSON_CRYSTAL, 2300, ::HTNetherFluidType)

    @JvmField
    val MOLTEN_WARPED_CRYSTAL: HTSimpleFluidContent = molten(HCMaterialKeys.WARPED_CRYSTAL, typeFactory = ::HTNetherFluidType)

    @JvmField
    val MOLTEN_ELDRITCH: HTSimpleFluidContent = molten(HCMaterialKeys.ELDRITCH, typeFactory = ::HTEndFluidType)

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
