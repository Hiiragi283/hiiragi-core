package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.text.HTTextResult
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
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
@ConsistentCopyVisibility
@JvmRecord
data class HTFluidResult private constructor(private val resource: HTFluidResourceType, private val amount: Int) :
    HTRecipeResult<FluidStack> {
        companion object {
            @JvmField
            val CODEC: Codec<HTFluidResult> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        HTFluidResourceType.MAP_CODEC.forGetter(HTFluidResult::resource),
                        HTCodecs.POSITIVE_INT
                            .optionalFieldOf(
                                HTConst.AMOUNT,
                                HTConst.DEFAULT_FLUID_AMOUNT,
                            ).forGetter(HTFluidResult::amount),
                    ).apply(instance, ::create)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidResult> = StreamCodec.composite(
                HTFluidResourceType.STREAM_CODEC,
                HTFluidResult::resource,
                ByteBufCodecs.VAR_INT,
                HTFluidResult::amount,
                ::create,
            )

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
                return HTFluidResult(resource, stack.amount)
            }

            @JvmStatic
            fun create(resource: HTFluidResourceType, amount: Int): HTFluidResult = HTFluidResult(validate(resource), amount)
        }

        /**
         * 完成品を取得します。
         * @return 完成品を取得できなかった場合は[FluidStack.EMPTY]
         */
        fun getOrEmpty(): FluidStack = get().valueOrElse(FluidStack::EMPTY)

        override fun get(): HTTextResult<FluidStack> = HTTextResult.success(resource.toStack(amount))

        override fun getId(): ResourceLocation = resource.getId()
    }
