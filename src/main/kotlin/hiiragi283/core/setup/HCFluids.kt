package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.fluid.HTDragonBreathFluidType
import hiiragi283.core.common.fluid.HTDyedFluidType
import hiiragi283.core.common.fluid.HTPotionFluidType
import hiiragi283.core.common.item.HTPotionBucketItem
import hiiragi283.lib.HTConstants
import hiiragi283.lib.color.HTColoredContents
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.HTFluidContentRegister
import hiiragi283.lib.resource.toId
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.DyeColor
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType

data object HCFluids {
    @JvmField
    val REGISTER = HTFluidContentRegister(HiiragiCoreAPI.MOD_ID)

    init {
        DyeContents.values
    }

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Vanilla    //

    data object DyeContents : HTColoredContents<HTFluidContent.Flowing> {
        @JvmStatic
        private val map: Map<HTDefaultColor, HTFluidContent.Flowing> = HTDefaultColor.entries.associateWith { color: HTDefaultColor ->
            val name: String = color.serializedName
            REGISTER.registerFlowing("${name}_dye") {
                properties = liquid()
                typeFactory = { prop: FluidType.Properties -> HTDyedFluidType(color, prop) }
                fluidTag = HTConstants.COMMON.toId("dyes", name)
                bucketTag = HTConstants.COMMON.toId("buckets", "dye", name)
            }
        }

        val values: Collection<HTFluidContent> get() = map.values

        override fun get(color: HTDefaultColor): HTFluidContent.Flowing = map[color]!!

        override fun get(color: DyeColor): HTFluidContent.Flowing = HTDefaultColor.fromDye(color).let(::get)

        override fun iterator(): Iterator<Pair<HTDefaultColor, HTFluidContent.Flowing>> = map.toList().iterator()
    }

    @JvmField
    val EXPERIENCE: HTFluidContent.Flowing = REGISTER.registerFlowing("experience") {
        properties = liquid()
        // typeFactory = ::HTExperienceFluidType
        blockFactory = null
    }

    @JvmField
    val HONEY: HTFluidContent.Flowing = REGISTER.registerFlowing("honey") {
        properties = create(SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_BREAK)
    }

    @JvmField
    val MUSHROOM_STEW: HTFluidContent.Flowing = REGISTER.registerFlowing("mushroom_stew") { properties = liquid() }

    @JvmField
    val DRAGON_BREATH: HTFluidContent.Flowing = REGISTER.registerFlowing("dragon_breath") {
        properties = create(SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundEvents.BOTTLE_FILL_DRAGONBREATH).density(-1000)
        typeFactory = ::HTDragonBreathFluidType
        blockFactory = null
    }

    @JvmField
    val POTION: HTFluidContent.Virtual = REGISTER.registerVirtual("potion") {
        properties = liquid()
        typeFactory = ::HTPotionFluidType
        bucketFactory = ::HTPotionBucketItem
    }

    @JvmField
    val OMINOUS_FLUX: HTFluidContent.Flowing = REGISTER.registerFlowing("ominous_flux") { properties = molten() }

    //    Organic    //

    @JvmField
    val LATEX: HTFluidContent.Flowing = REGISTER.registerFlowing("latex") {
        properties = liquid()
    }

    @JvmField
    val MEAT: HTFluidContent.Flowing = REGISTER.registerFlowing("meat") { properties = liquid() }

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
