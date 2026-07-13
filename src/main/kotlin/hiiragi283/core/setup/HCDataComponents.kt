package hiiragi283.core.setup

import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.data.DataComponentType
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.item.HTBlueprintItem
import hiiragi283.core.common.text.HTSimpleTranslation
import net.minecraft.core.GlobalPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.neoforge.fluids.SimpleFluidContent

object HCDataComponents {
    @JvmField
    val BLUEPRINT_NUMBER: DataComponentType<Int> = DataComponentType(HTBlueprintItem.CODEC, ByteBufCodecs.VAR_INT)

    @JvmField
    val BOTTLE_TYPE: DataComponentType<HTBottleType> = DataComponentType(HTBottleType.CODEC, HTBottleType.STREAM_CODEC)

    @JvmField
    val COLOR: DataComponentType<HTDefaultColor> = DataComponentType(HTDefaultColor.CODEC, HTDefaultColor.STREAM_CODEC)

    @JvmField
    val DESCRIPTION: DataComponentType<HTTranslation> = DataComponentType(HTSimpleTranslation.CODEC, HTSimpleTranslation.STREAM_CODEC)

    @JvmField
    val LOCATION: DataComponentType<GlobalPos> = DataComponentType(GlobalPos.CODEC, GlobalPos.STREAM_CODEC)

    @JvmField
    val EXPERIENCE: DataComponentType<Long> = DataComponentType(HTCodecs.NON_NEGATIVE_LONG, ByteBufCodecs.VAR_LONG)

    //    Storage    //

    @JvmField
    val ENERGY: DataComponentType<Int> = DataComponentType(HTCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT)

    @JvmField
    val FLUID: DataComponentType<SimpleFluidContent> = DataComponentType(SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)
}
