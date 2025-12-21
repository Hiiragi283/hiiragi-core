package hiiragi283.core.api.stack

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * [ImmutableComponentStack]を[液体][Fluid]向けに実装したクラスです。
 * @param stack 内部で保持しているスタック
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmInline
value class ImmutableFluidStack private constructor(private val stack: FluidStack) : ImmutableComponentStack<Fluid, ImmutableFluidStack> {
    companion object {
        /**
         * 指定した[液体][Fluid]と[量][amount]を[ImmutableFluidStack]に変換します。
         * @return [FluidStack.isEmpty]が`true`の場合は`null`
         */
        @JvmStatic
        fun ofNullable(fluid: Fluid, amount: Int): ImmutableFluidStack? = FluidStack(fluid, amount).toImmutable()

        /**
         * 指定した[液体][Fluid]と[量][amount]を[ImmutableFluidStack]に変換します。
         * @throws [FluidStack.isEmpty]が`true`の場合
         */
        @JvmStatic
        fun of(fluid: Fluid, amount: Int): ImmutableFluidStack = FluidStack(fluid, amount).toImmutableOrThrow()

        /**
         * 指定した[stack]を[ImmutableFluidStack]に変換します。
         * @return [FluidStack.isEmpty]が`true`の場合は`null`
         */
        @JvmStatic
        fun of(stack: FluidStack): ImmutableFluidStack? = when {
            stack.isEmpty -> null
            else -> ImmutableFluidStack(stack)
        }
    }

    /**
     * 保持している[FluidStack][stack]のコピーを返します。
     */
    fun unwrap(): FluidStack = stack.copy()

    /**
     * 保持している[液体][type]の[FluidType]を返します。
     */
    fun fluidType(): FluidType = stack.fluidType

    //    ImmutableComponentStack    //

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun type(): Fluid = stack.fluid

    override fun amount(): Int = stack.amount

    override fun copyWithAmount(amount: Int): ImmutableFluidStack? = unwrap().copyWithAmount(amount).toImmutable()

    override fun getText(): Component = stack.hoverName

    override fun getHolder(): Holder<Fluid> = stack.fluidHolder

    override fun getComponents(): DataComponentMap = stack.components
}
