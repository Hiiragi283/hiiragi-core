package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.HTConst
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
 * [液体][Fluid]向けの[HTResourceType.DataComponent]の実装クラスです。
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
                .of(FluidStack.fixedAmountCodec(HTConst.DEFAULT_FLUID_AMOUNT), FluidStack.STREAM_CODEC)
                .xmap(::HTFluidResourceType, HTFluidResourceType::stack)

        /**
         * 指定した[fluid]を[HTFluidResourceType]に変換します。
         * @return [FluidStack.isEmpty]が`true`の場合は`null`
         */
        @JvmStatic
        fun ofNullable(fluid: Fluid): HTFluidResourceType? = FluidStack(fluid, 1).let(::of)

        /**
         * 指定した[fluid]を[HTFluidResourceType]に変換します。
         * @throws IllegalStateException [FluidStack.isEmpty]が`true`の場合
         */
        @JvmStatic
        fun of(fluid: Fluid): HTFluidResourceType = ofNullable(fluid) ?: error("Fluid must not be empty")

        /**
         * 指定した[stack]を[HTFluidResourceType]に変換します。
         * @return [FluidStack.isEmpty]が`true`の場合は`null`
         */
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
