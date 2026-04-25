package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.item.HTBlueprintItem
import hiiragi283.core.common.registry.register.HTDeferredDataComponentRegister
import hiiragi283.core.common.text.HTSimpleTranslation
import net.minecraft.core.GlobalPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.neoforge.fluids.SimpleFluidContent

object HCDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, HiiragiCoreAPI.MOD_ID)

    @JvmField
    val BLUEPRINT_NUMBER: DataComponentType<Int> =
        REGISTER.registerType("blueprint_number", HTBlueprintItem.CODEC, ByteBufCodecs.VAR_INT)

    @JvmField
    val BOTTLE_TYPE: DataComponentType<HTBottleType> =
        REGISTER.registerType("bottle_type", HTBottleType.CODEC, HTBottleType.STREAM_CODEC)

    @JvmField
    val COLOR: DataComponentType<HTDefaultColor> =
        REGISTER.registerType("color", HTDefaultColor.CODEC, HTDefaultColor.STREAM_CODEC)

    @JvmField
    val DESCRIPTION: DataComponentType<HTTranslation> =
        REGISTER.registerType("description", HTSimpleTranslation.CODEC, HTSimpleTranslation.STREAM_CODEC)

    @JvmField
    val LOCATION: DataComponentType<GlobalPos> =
        REGISTER.registerType("location", GlobalPos.CODEC, GlobalPos.STREAM_CODEC)

    @JvmField
    val EXPERIENCE: DataComponentType<Long> =
        REGISTER.registerType("experience", HTCodecs.NON_NEGATIVE_LONG, ByteBufCodecs.VAR_LONG)

    //    Storage    //

    @JvmField
    val ENERGY: DataComponentType<Int> =
        REGISTER.registerType(HTConst.ENERGY, HTCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT)

    @JvmField
    val FLUID: DataComponentType<SimpleFluidContent> =
        REGISTER.registerType(HTConst.FLUID, SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)
}
