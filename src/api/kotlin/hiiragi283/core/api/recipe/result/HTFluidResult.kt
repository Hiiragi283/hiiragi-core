package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.registry.holderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.Identifier
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

class HTFluidResult(private val template: FluidStackTemplate) :
    HTRecipeResult<FluidStack>,
    FluidInstance by validate(template) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> =
            VanillaBiCodecs.FLUID_STACK_TEMPLATE.xmap(::HTFluidResult, HTFluidResult::template)

        /**
         * 液体流が指定されている場合，液体源に置き換える
         */
        @JvmStatic
        private fun validate(template: FluidStackTemplate): FluidStackTemplate {
            val fluid: Fluid = template.typeHolder().value()
            return if (!fluid.isSource(fluid.defaultFluidState()) && fluid is FlowingFluid) {
                FluidStackTemplate(fluid.source, template.amount, template.components)
            } else {
                template
            }
        }
    }

    override fun create(): FluidStack = template.create()

    override fun getId(): Identifier = this.holderLike().getId()
}
