package hiiragi283.core.setup

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.common.fluid.HTDragonBreathFluidType
import hiiragi283.core.common.fluid.HTDyedFluidType
import hiiragi283.core.common.fluid.HTEndFluidType
import hiiragi283.core.common.fluid.HTExperienceFluidType
import hiiragi283.core.common.fluid.HTLatexFluid
import hiiragi283.core.common.fluid.HTNetherFluidType
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
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
    val DYE: Map<HTDefaultColor, HTFluidContent> = HTDefaultColor.entries.associateWith { color: HTDefaultColor ->
        val name: String = color.serializedName
        REGISTER.registerFlowing("${name}_dye") {
            properties = liquid()
            typeFactory = ::HTDyedFluidType.partially1(color)
            fluidTag = "dyes/$name"
            bucketTag = "buckets/dye/$name"
        }
    }

    @JvmStatic
    fun getDye(color: HTDefaultColor): HTFluidContent = DYE[color]!!

    @JvmField
    val EXPERIENCE: HTFluidContent = REGISTER.registerFlowing("experience") {
        properties = liquid()
        typeFactory = ::HTExperienceFluidType
        blockFactory = null
    }

    @JvmField
    val HONEY: HTFluidContent = REGISTER.registerFlowing("honey") {
        properties = create(SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_BREAK)
    }

    @JvmField
    val MUSHROOM_STEW: HTFluidContent = REGISTER.registerFlowing("mushroom_stew") { properties = liquid() }

    @JvmField
    val DRAGON_BREATH: HTFluidContent = REGISTER.registerFlowing("dragon_breath") {
        properties = create(SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundEvents.BOTTLE_FILL_DRAGONBREATH).density(-1000)
        typeFactory = ::HTDragonBreathFluidType
        blockFactory = null
    }

    //    Organic    //

    @JvmField
    val LATEX: HTFluidContent = REGISTER.registerFlowing("latex") {
        properties = liquid()
        sourceFactory = ::HTLatexFluid
    }

    @JvmField
    val MEAT: HTFluidContent = REGISTER.registerFlowing("meat") { properties = liquid() }

    //    Material    //

    @JvmStatic
    private inline fun molten(
        key: HTMaterialKey,
        temp: Int = 1300,
        builderAction: HTFluidContentRegister.FlowingBuilder.() -> Unit = {},
    ): HTFluidContent = REGISTER.registerFlowing("molten_${key.asMaterialId().path}") {
        properties = molten().temperature(temp)
        builderAction()
    }

    // Vanilla
    @JvmField
    val MOLTEN_GLASS: HTFluidContent = molten(VanillaMaterialKeys.GLASS) {
        blockFactory = null
    }

    // Common
    @JvmField
    val MOLTEN_PLASTIC: HTFluidContent = molten(CommonMaterialKeys.PLASTIC) {
        blockFactory = null
    }

    @JvmField
    val MOLTEN_RUBBER: HTFluidContent = molten(CommonMaterialKeys.RUBBER) {
        blockFactory = null
    }

    // Hiiragi Core
    @JvmField
    val MOLTEN_CRIMSON_CRYSTAL: HTFluidContent = molten(HCMaterialKeys.CRIMSON_CRYSTAL, 2300) {
        typeFactory = ::HTNetherFluidType
    }

    @JvmField
    val MOLTEN_WARPED_CRYSTAL: HTFluidContent = molten(HCMaterialKeys.WARPED_CRYSTAL) {
        typeFactory = ::HTNetherFluidType
    }

    @JvmField
    val MOLTEN_ELDRITCH: HTFluidContent = molten(HCMaterialKeys.ELDRITCH) {
        typeFactory = ::HTEndFluidType
    }

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
