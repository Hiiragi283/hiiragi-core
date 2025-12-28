package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.storage.resource.HTResourceType
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * [HTResourceType.DataComponent]を[液体][Fluid]向けに実装したクラスです。
 * @param stack 内部で保持しているスタック
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTFluidResourceType private constructor(private val stack: FluidStack) : HTResourceType.DataComponent<Fluid> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResourceType> =
            BiCodec
                .of(FluidStack.fixedAmountCodec(FluidType.BUCKET_VOLUME), FluidStack.STREAM_CODEC)
                .xmap(::HTFluidResourceType, HTFluidResourceType::stack)

        @JvmStatic
        fun of(stack: FluidStack): HTFluidResourceType? = when {
            stack.isEmpty -> null
            else -> HTFluidResourceType(stack.copyWithAmount(1))
        }
    }

    /**
     * 保持している[液体][type]の[FluidType]を返します。
     */
    fun fluidType(): FluidType = stack.fluidType

    fun toStack(amount: Int): FluidStack = stack.copyWithAmount(amount)

    override fun equals(other: Any?): Boolean = when (other) {
        is HTFluidResourceType -> FluidStack.isSameFluidSameComponents(this.stack, other.stack)
        else -> false
    }

    override fun hashCode(): Int = FluidStack.hashFluidAndComponents(stack)

    //    HTResourceType    //

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun type(): Fluid = stack.fluid

    override fun getText(): Component = stack.hoverName

    override fun getHolder(): Holder<Fluid> = stack.fluidHolder

    override fun getComponents(): DataComponentMap = stack.components
}
