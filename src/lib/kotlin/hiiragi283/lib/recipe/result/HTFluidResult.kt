package hiiragi283.lib.recipe.result

import com.mojang.serialization.Codec
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.resource.HTIdLike
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

@JvmInline
value class HTFluidResult private constructor(@PublishedApi internal val template: FluidStackTemplate) : HTIdLike {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidResult> = FluidStackTemplate.CODEC.xmap(::create, HTFluidResult::template)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidResult> = FluidStackTemplate.STREAM_CODEC.map(::create, HTFluidResult::template)

        /**
         * 液体流が指定されている場合，液体源に置き換える
         */
        @JvmStatic
        private fun validate(template: FluidStackTemplate): FluidStackTemplate {
            val fluid: Fluid = template.typeHolder().value()
            return when {
                !fluid.isSource(fluid.defaultFluidState()) && fluid is FlowingFluid -> FluidStackTemplate(fluid.source, template.amount, template.components)
                else -> template
            }
        }

        @JvmStatic
        fun create(stack: FluidStack): HTFluidResult = stack.let(FluidStackTemplate::fromNonEmptyStack).let(::create)

        @JvmStatic
        fun create(template: FluidStackTemplate): HTFluidResult = template.let(::validate).let(::HTFluidResult)
    }

    inline val amount: Int get() = template.amount()

    fun copyWithAmount(newAmount: Int): HTFluidResult = create(template.withAmount(newAmount))

    fun create(): FluidStack = template.create()

    override fun getId(): Identifier = template.typeHolder().getKeyOrThrow().identifier()
}
