package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.text.HTTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [液体][FluidStack]の[完成品][HTRecipeResult]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTFluidResult(resource: HTFluidResourceType, private val amount: Int) : HTRecipeResult<FluidStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = BiCodec.composite(
            HTFluidResourceType.CODEC.toMap().forGetter(HTFluidResult::resource),
            BiCodecs.POSITIVE_INT.optionalFieldOf(HTConst.AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT).forGetter(HTFluidResult::amount),
            ::HTFluidResult,
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
    }

    private val resource: HTFluidResourceType = validate(resource)

    /**
     * 指定した[レジストリ][provider]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[FluidStack.EMPTY]
     */
    fun getStackOrEmpty(provider: HolderLookup.Provider?): FluidStack = getStackResult(provider).valueOrElse(FluidStack::EMPTY)

    override fun getStackResult(provider: HolderLookup.Provider?): HTTextResult<FluidStack> = HTTextResult.success(resource.toStack(amount))

    override fun getId(): ResourceLocation = resource.getId()

    override fun toString(): String = "HTItemResult(resource=$resource, amount=$amount)"
}
