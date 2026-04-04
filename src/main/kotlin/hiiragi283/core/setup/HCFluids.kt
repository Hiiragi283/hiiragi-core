package hiiragi283.core.setup

import hiiragi283.core.api.HTDyeColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.common.fluid.HTDragonBreathFluidType
import hiiragi283.core.common.fluid.HTDyedFluidType
import hiiragi283.core.common.fluid.HTExperienceFluidType
import hiiragi283.core.common.fluid.HTLatexFluid
import hiiragi283.core.common.fluid.HTPotionFluidType
import hiiragi283.core.common.item.HTPotionBucketItem
import hiiragi283.core.impl.registry.HTFluidContentRegister
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
    val DYE: Map<HTDyeColor, HTFluidContent> = HTDyeColor.entries.associateWith { color: HTDyeColor ->
        val name: String = color.serializedName
        REGISTER.registerFlowing("${name}_dye") {
            properties = liquid()
            typeFactory = ::HTDyedFluidType.partially1(color)
            fluidTag = "dyes/$name"
            bucketTag = "buckets/dye/$name"
        }
    }

    @JvmStatic
    fun getDye(color: HTDyeColor): HTFluidContent = DYE[color]!!

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

    @JvmField
    val POTION: HTFluidContent = REGISTER.registerVirtual("potion") {
        properties = liquid()
        typeFactory = ::HTPotionFluidType
        bucketFactory = ::HTPotionBucketItem
    }

    @JvmField
    val OMINOUS_FLUX: HTFluidContent = REGISTER.registerFlowing("ominous_flux") { properties = molten() }

    //    Organic    //

    @JvmField
    val LATEX: HTFluidContent = REGISTER.registerFlowing("latex") {
        properties = liquid()
        sourceFactory = ::HTLatexFluid
    }

    @JvmField
    val MEAT: HTFluidContent = REGISTER.registerFlowing("meat") { properties = liquid() }

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
