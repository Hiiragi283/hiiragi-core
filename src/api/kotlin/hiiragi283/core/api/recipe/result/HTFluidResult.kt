package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [液体][FluidStack]の完成品を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
@JvmInline
value class HTFluidResult private constructor(private val stack: FluidStack) : HTIdLike {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidResult> = FluidStack.CODEC.xmap(::create, HTFluidResult::stack)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidResult> = FluidStack.STREAM_CODEC.map(::create, HTFluidResult::stack)

        /**
         * 液体流が指定されている場合，液体源に置き換える
         */
        @JvmStatic
        private fun validate(stack: FluidStack): FluidStack {
            check(!stack.isEmpty) { "Cannot create HTFluidResult from empty stack" }
            val fluid: Fluid = stack.fluid
            return when {
                !fluid.isSource(fluid.defaultFluidState()) && fluid is FlowingFluid -> {
                    val stack1 = FluidStack(fluid.source, stack.amount)
                    stack1.applyComponents(stack.componentsPatch)
                    stack1
                }
                else -> stack
            }
        }

        @JvmStatic
        fun create(stack: FluidStack): HTFluidResult = stack.let(::validate).let(::HTFluidResult)
    }

    fun create(): FluidStack = stack.copy()

    override fun getId(): ResourceLocation = stack.fluidHolder.toLike().getId()
}
