package hiiragi283.core.api.inventory.container

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import java.util.Optional

/**
 * GUIを開いている[手][hand]と[アイテム][stack]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
@JvmRecord
data class HTItemContainerContext(val hand: Optional<InteractionHand>, val stack: ItemStack) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemContainerContext> = BiCodec.composite(
            VanillaBiCodecs.HAND.optionalFieldOf("hand").forGetter(HTItemContainerContext::hand),
            VanillaBiCodecs.ITEM_STACK_NON_EMPTY.fieldOf("stack").forGetter(HTItemContainerContext::stack),
            ::HTItemContainerContext,
        )
    }

    constructor(hand: InteractionHand?, stack: ItemStack) : this(Optional.ofNullable(hand), stack)
}
