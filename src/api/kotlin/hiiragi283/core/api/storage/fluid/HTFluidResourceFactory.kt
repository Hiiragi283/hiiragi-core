package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.fluid.createFluidStack
import hiiragi283.core.api.storage.resource.HTResourceFactory
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [HTFluidResourceType]向けの[HTResourceFactory]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object HTFluidResourceFactory : HTResourceFactory.DataComponent<Fluid, FluidStack, HTFluidResourceType>() {
    override fun create(type: Fluid, patch: DataComponentPatch): HTFluidResourceType? = create(createFluidStack(type, patch = patch))

    override fun create(stack: FluidStack): HTFluidResourceType? = HTFluidResourceType.of(stack)

    override fun createStack(resource: HTFluidResourceType?, amount: Int): FluidStack = resource?.toStack(amount) ?: FluidStack.EMPTY

    override fun getDefaultAmount(): Int = HTConst.DEFAULT_FLUID_AMOUNT
}
