package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.monad.toIor
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import java.util.Optional

typealias RawPotionContents = Ior<Holder<Potion>, List<HTMobEffectInstance>>

/**
 * [PotionContents]と[HTBottleType]を束ねたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.1
 */
@ConsistentCopyVisibility
data class HTPotionContents private constructor(val contents: RawPotionContents, val bottleType: HTBottleType) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTPotionContents> = BiCodec.composite(
            MapBiCodecs
                .ior(
                    VanillaBiCodecs.holder(Registries.POTION).fieldOf("potion"),
                    HTMobEffectInstance.CODEC.listOrElement().fieldOf("instances"),
                ).forGetter(HTPotionContents::contents),
            HTBottleType.CODEC.fieldOf("bottle_type").forGetter(HTPotionContents::bottleType),
            ::HTPotionContents,
        )

        /**
         * [ポーション][contents]と[ポーション瓶の種類][bottleType]から[HTPotionContents]を作成します。
         * @return [contents]が空の場合は`null`
         */
        @JvmStatic
        fun fromVanilla(contents: PotionContents, bottleType: HTBottleType): HTPotionContents? {
            if (contents == PotionContents.EMPTY) return null
            val instances: List<HTMobEffectInstance> = contents.customEffects().map(::HTMobEffectInstance)
            return of(
                contents
                    .potion()
                    .map { Ior.Both(it, instances) as RawPotionContents }
                    .orElse(Ior.Right(instances)),
                bottleType,
            )
        }

        /**
         * [ポーション][potion]と[ポーション瓶の種類][bottleType]から[HTPotionContents]を作成します。
         * @return [potion]が空の場合は`null`
         */
        @JvmStatic
        fun of(potion: Holder<Potion>, bottleType: HTBottleType): HTPotionContents? = of(Ior.Left(potion), bottleType)

        /**
         * [ポーション][contents]と[ポーション瓶の種類][bottleType]から[HTPotionContents]を作成します。
         * @return [contents]が空の場合は`null`
         */
        @JvmStatic
        fun of(contents: RawPotionContents, bottleType: HTBottleType): HTPotionContents? {
            val isEmpty: Boolean = contents.map(
                { potion: Holder<Potion> -> potion == Potions.WATER },
                { instances: List<HTMobEffectInstance> -> instances.isEmpty() },
                { left: Boolean, right: Boolean -> left && right },
            )
            return when {
                isEmpty -> null
                else -> HTPotionContents(contents.mapLeft(Holder<Potion>::getDelegate), bottleType)
            }
        }

        @JvmStatic
        fun create(builderAction: Builder.() -> Unit): HTPotionContents? = Builder().apply(builderAction).build()
    }

    /**
     * ポーションのインスタンス
     */
    val potion: Holder<Potion>? = contents.getLeft()

    /**
     * バニラの[PotionContents]のインスタンス
     */
    val vanilla = PotionContents(
        Optional.ofNullable(contents.getLeft()),
        Optional.empty(),
        contents.getRight()?.map(HTMobEffectInstance::toMutable) ?: emptyList(),
    )

    //    Builder    //

    /**
     * [HTPotionContents]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.1
     */
    class Builder {
        var potion: Holder<Potion>? = null
        val instances: MutableList<HTMobEffectInstance> = mutableListOf()
        lateinit var bottleType: HTBottleType

        fun build(): HTPotionContents? {
            val contents: RawPotionContents = (potion?.delegate to instances).toIor() ?: return null
            return of(contents, bottleType)
        }
    }
}
