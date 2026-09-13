package hiiragi283.core.api.item.alchemy

import com.mojang.serialization.Codec
import hiiragi283.core.api.fluid.FluidStack
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.serialization.codec.HTCodecs
import kotlin.jvm.optionals.getOrNull
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * [PotionContents]と[HTBottleType]を束ねたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
@JvmRecord
data class BottledPotionContents(val contents: PotionContents, val bottleType: HTBottleType) {
    companion object {
        @JvmField
        val CODEC: Codec<BottledPotionContents> = HTCodecs.record { instance ->
            instance
                .group(
                    PotionContents.CODEC.fieldOf("contents").forGetter(BottledPotionContents::contents),
                    HTBottleType.CODEC
                        .optionalFieldOf("bottle_type", HTBottleType.DEFAULT)
                        .forGetter(BottledPotionContents::bottleType),
                ).apply(instance, ::BottledPotionContents)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BottledPotionContents> = StreamCodec.composite(
            PotionContents.STREAM_CODEC,
            BottledPotionContents::contents,
            HTBottleType.STREAM_CODEC,
            BottledPotionContents::bottleType,
            ::BottledPotionContents,
        )
    }

    constructor(potion: Holder<Potion>) : this(potion, HTBottleType.DEFAULT)

    constructor(potion: Holder<Potion>, bottleType: HTBottleType) : this(PotionContents(potion), bottleType)

    /**
     * ポーションの値
     */
    val potion: Holder<Potion>? get() = contents.potion().getOrNull()

    /**
     * カスタム色の値
     */
    val customColor: Int? get() = contents.customColor().getOrNull()

    /**
     * カスタムエフェクトの一覧
     */
    val customEffects: List<MobEffectInstance> get() = contents.customEffects()

    /**
     * ポーションも含めたすべてのエフェクトの一覧
     */
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

    /**
     * @since 21.1.1.0
     */
    fun toFluidStack(amount: Int = FluidType.BUCKET_VOLUME): FluidStack = when (this.isWater) {
        true -> VanillaFluidContents.WATER.toStack(amount)
        false -> {
            val content: HTFluidContent = HTPotionAccess.INSTANCE.fluidContent
            content.toStack(patch = HTPotionHelper.createFluidPatch(content.get(), this))
        }
    }

    /**
     * @since 21.1.1.0
     */
    fun toFluidStack(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): FluidStack = when (this.isWater) {
        true -> VanillaFluidContents.WATER.toStack(amount)
        false -> FluidStack(fluid, amount, HTPotionHelper.createFluidPatch(fluid, this))
    }

    /**
     * @since 21.1.1.0
     */
    fun toBucketStack(): ItemStack = when (this.isWater) {
        true -> VanillaFluidContents.WATER.bucketHolder.toStack()
        false -> HTPotionAccess.INSTANCE.fluidContent.bucketHolder.toStack(patch = HTPotionHelper.createItemPatch(this))
    }

    /**
     * @since 21.1.1.0
     */
    fun toBottleItem(): ItemStack = createItemStack(bottleType, DataComponents.POTION_CONTENTS, contents)
}
