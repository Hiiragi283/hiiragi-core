package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.text.HTTextResult
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [液体][FluidStack]の[完成品][HTRecipeResult]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
@JvmRecord
data class HTFluidResult(private val stack: FluidStack) : HTRecipeResult<FluidStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidResult> = FluidStack.CODEC.xmap(::create, HTFluidResult::stack)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidResult> = FluidStack.STREAM_CODEC.map(::create, HTFluidResult::stack)

        /**
         * 液体流が指定されている場合，液体源に置き換える
         */
        @JvmStatic
        private fun validate(resource: HTFluidResourceType): HTFluidResourceType {
            val (holder: Holder<Fluid>, patch: DataComponentPatch) = resource
            val fluid: Fluid = holder.value()
            return if (!fluid.isSource(fluid.defaultFluidState()) && fluid is FlowingFluid) {
                fluid.source.toResource(patch) ?: resource
            } else {
                resource
            }
        }

        @JvmStatic
        fun create(stack: FluidStack): HTFluidResult {
            val resource: HTFluidResourceType = stack.toResource() ?: error("Cannot create HTFluidResult from empty stack")
            return resource.let(::validate).toStack(stack.amount).let(::HTFluidResult)
        }
    }

    /**
     * 完成品を取得します。
     * @return 完成品を取得できなかった場合は[FluidStack.EMPTY]
     */
    @Suppress("DEPRECATION")
    fun create(): FluidStack = get().valueOrElse(FluidStack::EMPTY)

    @Deprecated("Use 'getOrEmpty()' instead", ReplaceWith("this.create()"))
    override fun get(): HTTextResult<FluidStack> = HTTextResult.success(stack)

    override fun getId(): ResourceLocation = stack.fluidHolder.toLike().getId()
}
