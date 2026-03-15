package hiiragi283.core.api.registry

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

typealias HTSimpleFluidHolderLike = HTFluidHolderLike<Fluid>

interface HTFluidHolderLike<FLUID : Fluid> : HTHolderLike<Fluid, FLUID> {
    fun getHolder(): Holder<Fluid> = getHolder(BuiltInRegistries.FLUID::getHolderOrThrow)

    fun getBucket(): HTSimpleItemHolderLike

    fun getFluidType(): FluidType

    // FluidStack
    fun isOf(stack: FluidStack): Boolean = stack.`is`(this.get())

    fun toStack(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack = FluidStack(this.get(), amount)

    // HTFluidResourceType
    fun toResource(): HTFluidResourceType? = toStack().toResource()

    fun toResource(patch: DataComponentPatch): HTFluidResourceType? {
        val stack: FluidStack = toStack()
        stack.applyComponents(patch)
        return stack.toResource()
    }

    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTSimpleFluidHolderLike> =
            VanillaBiCodecs.holderLike(Registries.FLUID).xmap(HTSimpleHolderLike<Fluid>::toFluidLike, identity())
    }

    interface Simple<FLUID : Fluid> : HTFluidHolderLike<FLUID> {
        override fun getBucket(): HTSimpleItemHolderLike = get().bucket.toItemLike()

        override fun getFluidType(): FluidType = get().fluidType
    }
}

//    Extensions    //

@Suppress("DEPRECATION")
fun <FLUID : Fluid> FLUID.toLike(): HTFluidHolderLike<FLUID> = object : HTFluidHolderLike.Simple<FLUID> {
    override fun unwrap(): Either<ResourceKey<Fluid>, Holder<Fluid>> = Either.Right(this@toLike.builtInRegistryHolder())

    override fun get(): FLUID = this@toLike
}

fun <FLUID : Fluid> HTHolderLike<Fluid, FLUID>.toFluidLike(): HTFluidHolderLike<FLUID> = object : HTFluidHolderLike.Simple<FLUID> {
    override fun unwrap(): Either<ResourceKey<Fluid>, Holder<Fluid>> = this@toFluidLike.unwrap()

    override fun get(): FLUID = this@toFluidLike.get()
}

fun FluidStack.getHolderLike(): HTSimpleFluidHolderLike = this.fluidHolder.toLike().toFluidLike()
