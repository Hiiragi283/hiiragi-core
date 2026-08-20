package hiiragi283.core.api.item.alchemy

import com.mojang.serialization.Codec
import hiiragi283.core.api.item.HTItemInstanceLike
import hiiragi283.core.api.item.ItemStack
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentPatch
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
    StringRepresentable,
    ItemLike,
    HTItemInstanceLike {
    DEFAULT,
    SPLASH,
    LINGERING,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTBottleType> = HTCodecs.stringEnum(HTBottleType::getSerializedName)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTBottleType> = HTStreamCodecs.enum()

        @JvmStatic
        fun getBottleType(resource: HTItemResourceType): HTBottleType? = entries.firstOrNull { resource.isOf(it.asItem()) }
    }

    override fun asItem(): Item = when (this) {
        DEFAULT -> Items.POTION
        SPLASH -> Items.SPLASH_POTION
        LINGERING -> Items.LINGERING_POTION
    }

    override fun toStack(count: Int, patch: DataComponentPatch): ItemStack = ItemStack(this, count, patch)

    override fun getSerializedName(): String = name.lowercase()
}
