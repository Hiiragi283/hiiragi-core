package hiiragi283.core.api.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.StackTemplate
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.toText
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.PatchedDataComponentMap
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack

@Suppress("DEPRECATION")
@JvmRecord
data class FluidStackTemplate(val fluid: Holder<Fluid>, val amount: Int, val components: DataComponentPatch) : StackTemplate<Fluid> {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<FluidStackTemplate> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    FluidStack.FLUID_NON_EMPTY_CODEC.fieldOf(HTConst.ID).forGetter(FluidStackTemplate::fluid),
                    HTCodecs.POSITIVE_INT.fieldOf(HTConst.AMOUNT).forGetter(FluidStackTemplate::amount),
                    DataComponentPatch.CODEC
                        .optionalFieldOf("components", DataComponentPatch.EMPTY)
                        .forGetter(FluidStackTemplate::components),
                ).apply(instance, ::FluidStackTemplate)
        }

        @JvmField
        val CODEC: Codec<FluidStackTemplate> = Codec.lazyInitialized(MAP_CODEC::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, FluidStackTemplate> = StreamCodec.composite(
            HTStreamCodecs.holder(Registries.FLUID),
            FluidStackTemplate::fluid,
            ByteBufCodecs.VAR_INT,
            FluidStackTemplate::amount,
            DataComponentPatch.STREAM_CODEC,
            FluidStackTemplate::components,
            ::FluidStackTemplate,
        )

        @JvmStatic
        fun fromStack(stack: FluidStack): HTTextResult<FluidStackTemplate> = when {
            stack.isEmpty -> HTTextResult.error("Stack must be non-empty".toText())
            else -> HTTextResult.success(FluidStackTemplate(stack.fluidHolder, stack.amount, stack.componentsPatch))
        }
    }

    init {
        check(!fluid.`is`(Fluids.EMPTY.builtInRegistryHolder()) && amount >= 0) { "Item must be non-empty" }
    }

    constructor(fluid: Fluid, amount: Int, components: DataComponentPatch = DataComponentPatch.EMPTY) : this(
        fluid.builtInRegistryHolder(),
        amount,
        components,
    )

    fun create(): FluidStack = FluidStack(typeHolder(), amount, components)

    fun apply(additionalPatch: DataComponentPatch): FluidStack = apply(this.amount, additionalPatch)

    fun apply(amount: Int, additionalPatch: DataComponentPatch): FluidStack {
        val stack = FluidStack(fluid, amount, additionalPatch)
        stack.applyComponents(components)
        return stack
    }

    override fun typeHolder(): Holder<Fluid> = fluid.delegate

    override fun getComponents(): DataComponentMap = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, components)
}
