package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.api.serialization.network.listOf
import hiiragi283.core.util.HTShapelessRecipeHelper
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.FluidStack

@JvmInline
value class HTListFluidResult(val results: List<HTFluidResult>) : Iterable<FluidStack> {
    companion object {
        @JvmStatic
        fun codec(maxSize: Int): Codec<HTListFluidResult> =
            HTFluidResult.CODEC.listOrElement(1, maxSize).xmap(::HTListFluidResult, HTListFluidResult::results)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTListFluidResult> =
            HTFluidResult.STREAM_CODEC.listOf().map(::HTListFluidResult, HTListFluidResult::results)
    }

    constructor(vararg results: HTFluidResult) : this(results.toList())

    constructor(results: Sequence<HTFluidResult>) : this(results.toList())

    override fun iterator(): Iterator<FluidStack> = results.map(HTFluidResult::create).let(HTShapelessRecipeHelper::mergeStacks).iterator()
}
