package hiiragi283.lib.registry

import hiiragi283.lib.fluid.createFluidStack
import hiiragi283.lib.fluid.createFluidTemplate
import hiiragi283.lib.resource.SupplierWithId
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

interface HTFluidHolderLike<out FLUID : Fluid> : SupplierWithId<FLUID> {
    /**
     * 保持している液体に対応するバケツを取得します。
     */
    fun getBucket(): SupplierWithId<*>

    /**
     * 保持している液体に対応する[FluidType]を取得します。
     */
    fun getFluidType(): FluidType

    fun toTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): Result<FluidStackTemplate> = createFluidTemplate(this.get(), count, patch)

    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): FluidStack = createFluidStack(this.get(), count, patch)
}
