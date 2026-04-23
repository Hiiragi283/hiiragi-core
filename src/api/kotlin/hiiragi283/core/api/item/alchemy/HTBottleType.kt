package hiiragi283.core.api.item.alchemy

import com.mojang.serialization.Codec
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

/**
 * ポーション瓶の種類を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
enum class HTBottleType :
    ItemLike,
    StringRepresentable {
    DEFAULT,
    SPLASH,
    LINGERING,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTBottleType> = HTCodecs.stringEnum(HTBottleType::getSerializedName)

        @JvmField
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, HTBottleType> = HTStreamCodecs.enum()

        /**
         * @since 0.14.0
         */
        @JvmStatic
        fun getBottleType(stack: ItemStack): HTBottleType? = entries.firstOrNull { stack.`is`(it.asItem()) }

        /**
         * @since 0.14.0
         */
        @JvmStatic
        fun getBottleType(resource: HTItemResourceType): HTBottleType? = entries.firstOrNull { resource.isOf(it.asItem()) }
    }

    override fun asItem(): Item = when (this) {
        DEFAULT -> Items.POTION
        SPLASH -> Items.SPLASH_POTION
        LINGERING -> Items.LINGERING_POTION
    }

    override fun getSerializedName(): String = name.lowercase()
}
