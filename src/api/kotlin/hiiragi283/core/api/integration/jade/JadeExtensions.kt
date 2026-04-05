package hiiragi283.core.api.integration.jade

import com.google.common.primitives.Ints
import net.neoforged.neoforge.fluids.FluidStack
import snownee.jade.api.fluid.JadeFluidObject

//    JadeFluidObject    //

/**
 * この[FluidStack][this]を[JadeFluidObject]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
fun FluidStack.toJade(): JadeFluidObject = JadeFluidObject.of(this.fluid, this.amount.toLong(), this.componentsPatch)

/**
 * この[JadeFluidObject][this]を[FluidStack]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
@Suppress("DEPRECATION")
fun JadeFluidObject.toStack(): FluidStack = FluidStack(this.type.builtInRegistryHolder(), Ints.saturatedCast(this.amount), this.components)
