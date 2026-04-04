package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import kotlin.jvm.optionals.getOrNull

/**
 * [PotionContents]と[HTBottleType]を束ねたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
@JvmRecord
data class BottledPotionContents(val contents: PotionContents, val bottleType: HTBottleType) : HTHasText {
    companion object {
        @JvmStatic
        private val CONTENTS_CODEC: BiCodec<RegistryFriendlyByteBuf, PotionContents> = BiCodec.of(
            PotionContents.CODEC,
            PotionContents.STREAM_CODEC,
        )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, BottledPotionContents> = BiCodec.composite(
            CONTENTS_CODEC.fieldOf("contents").forGetter(BottledPotionContents::contents),
            HTBottleType.CODEC.fieldOf("bottle_type").forGetter(BottledPotionContents::bottleType),
            ::BottledPotionContents,
        )
    }

    constructor(potion: Holder<Potion>) : this(potion, HTBottleType.DEFAULT)

    constructor(potion: Holder<Potion>, bottleType: HTBottleType) : this(PotionContents(potion), bottleType)

    /**
     * ポーションのインスタンス
     */
    val potion: Holder<Potion>? get() = contents.potion().getOrNull()
    val customColor: Int? get() = contents.customColor().getOrNull()
    val customEffects: List<MobEffectInstance> get() = contents.customEffects()

    val allEffects: Iterable<MobEffectInstance> get() = contents.allEffects

    /**
     * 保持しているエフェクトが空かどうか
     * @since 0.13.0
     */
    val isEmpty: Boolean get() = contents == PotionContents.EMPTY || allEffects.none()

    /**
     * 保持しているエフェクトが水に一致するかどうか
     * @since 0.13.0
     */
    val isWater: Boolean get() = potion == Potions.WATER && bottleType == HTBottleType.DEFAULT

    override fun getText(): Text = contents.getName("${bottleType.asItem().descriptionId}.effect.")
}
