package hiiragi283.core.api.integration.jade

import com.google.common.primitives.Ints
import net.neoforged.neoforge.fluids.FluidStack
import snownee.jade.api.fluid.JadeFluidObject

//    JadeFluidObject    //

fun FluidStack.toJade(): JadeFluidObject = JadeFluidObject.of(this.fluid, this.amount.toLong(), this.componentsPatch)

fun JadeFluidObject.toStack(): FluidStack = FluidStack(this.typeHolder(), Ints.saturatedCast(this.amount), this.components)
