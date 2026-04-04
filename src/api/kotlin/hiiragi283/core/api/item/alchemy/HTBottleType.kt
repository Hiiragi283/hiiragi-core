package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.TypedInstance
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
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
        val CODEC: BiCodec<ByteBuf, HTBottleType> = BiCodecs.stringEnum(HTBottleType::getSerializedName)

        @JvmStatic
        fun getBottleType(resource: TypedInstance<Item>): HTBottleType? = entries.firstOrNull { resource.`is`(it.asItem()) }
    }

    override fun asItem(): Item = when (this) {
        DEFAULT -> Items.POTION
        SPLASH -> Items.SPLASH_POTION
        LINGERING -> Items.LINGERING_POTION
    }

    override fun getSerializedName(): String = name.lowercase()
}
