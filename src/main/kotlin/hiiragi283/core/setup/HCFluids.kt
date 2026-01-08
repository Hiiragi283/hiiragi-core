package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.common.fluid.HTEndFluidType
import hiiragi283.core.common.fluid.HTNetherFluidType
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
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
    val EXPERIENCE: HTSimpleFluidContent = REGISTER.registerSimple("experience", liquid())

    @JvmField
    val HONEY: HTSimpleFluidContent = REGISTER.registerSimple(
        "honey",
        create(SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_BREAK),
    ) { it.speedFactor(0.4f) }

    @JvmField
    val MUSHROOM_STEW: HTSimpleFluidContent = REGISTER.registerSimple("mushroom_stew", liquid())

    //    Organic    //

    @JvmField
    val LATEX: HTSimpleFluidContent = REGISTER.registerSimple("latex", liquid())

    @JvmField
    val BLOOD: HTSimpleFluidContent = REGISTER.registerSimple("blood", liquid())

    @JvmField
    val MEAT: HTSimpleFluidContent = REGISTER.registerSimple("meat", liquid())

    //    Material    //

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTSimpleFluidContent> = buildTable {
        fun register(key: HTMaterialKey, properties: FluidType.Properties, typeFactory: (FluidType.Properties) -> FluidType = ::FluidType) {
            this[HCMaterialPrefixes.MOLTEN, key] = REGISTER.register("molten_${key.name}", properties, typeFactory)
        }

        // Vanilla
        register(VanillaMaterialKeys.COPPER, molten().temperature(1100))
        register(VanillaMaterialKeys.IRON, molten().temperature(1800))
        register(VanillaMaterialKeys.GOLD, molten().temperature(1300))
        register(VanillaMaterialKeys.NETHERITE, molten().temperature(2300))

        register(VanillaMaterialKeys.GLASS, molten().temperature(1300))
        // Common
        register(CommonMaterialKeys.STEEL, molten().temperature(1800))
        // Hiiragi Core
        register(HCMaterialKeys.CRIMSON_CRYSTAL, molten().temperature(2300), ::HTNetherFluidType)
        register(HCMaterialKeys.WARPED_CRYSTAL, molten().temperature(1300), ::HTNetherFluidType)
        register(HCMaterialKeys.ELDRITCH, molten().temperature(1300), ::HTEndFluidType)

        register(HCMaterialKeys.AZURE_STEEL, molten().temperature(1800))
        register(HCMaterialKeys.DEEP_STEEL, molten().temperature(2300))
    }.let(::HTMaterialTable)

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
