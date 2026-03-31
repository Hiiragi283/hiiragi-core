package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

@JvmInline
value class HTFluidResult private constructor(val template: FluidStackTemplate) : FluidInstance by template {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = VanillaBiCodecs.FLUID_STACK_TEMPLATE
            .xmap(::create, HTFluidResult::template)

        @JvmStatic
        fun create(template: FluidStackTemplate): HTFluidResult {
            val fluid: Fluid = template.fluid().value()
            val template1: FluidStackTemplate = if (!fluid.isSource(fluid.defaultFluidState()) && fluid is FlowingFluid) {
                FluidStackTemplate(fluid.source, template.amount(), template.components())
            } else {
                template
            }
            return HTFluidResult(template1)
        }
    }

    fun create(): FluidStack = template.create()
}
