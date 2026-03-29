package hiiragi283.core.impl.transfer

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting

enum class HTSlotInfo(val canInsert: Boolean, val canExtract: Boolean, val color: ChatFormatting) {
    BOTH(true, true, ChatFormatting.LIGHT_PURPLE),
    INPUT(true, false, ChatFormatting.RED),
    OUTPUT(false, true, ChatFormatting.BLUE),
    EXTRA_INPUT(true, false, ChatFormatting.YELLOW),
    EXTRA_OUTPUT(false, true, ChatFormatting.GREEN),
    NONE(false, false, ChatFormatting.GRAY),
    ;

    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTSlotInfo> = BiCodecs.enum()
    }

    /*fun getText(side: Direction): Text = when (this) {
        BOTH -> RagiumTranslation.GUI_SLOT_BOTH
        INPUT -> RagiumTranslation.GUI_SLOT_INPUT
        OUTPUT -> RagiumTranslation.GUI_SLOT_OUTPUT
        EXTRA_INPUT -> RagiumTranslation.GUI_SLOT_EXTRA_INPUT
        EXTRA_OUTPUT -> RagiumTranslation.GUI_SLOT_EXTRA_OUTPUT
        NONE -> RagiumTranslation.GUI_SLOT_NONE
    }.translateColored(color, ChatFormatting.WHITE, side)*/
}
