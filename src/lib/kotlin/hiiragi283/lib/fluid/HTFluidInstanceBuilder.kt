@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.fluid

import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.getOrElse
import hiiragi283.lib.util.right
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

@HTBuilderMarker
class HTFluidInstanceBuilder {
    companion object {
        @JvmStatic
        inline fun buildTemplate(builderAction: HTFluidInstanceBuilder.() -> Unit): HTTextResult<FluidStackTemplate> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTFluidInstanceBuilder().apply(builderAction).buildTemplate()
        }

        @JvmStatic
        inline fun buildStack(builderAction: HTFluidInstanceBuilder.() -> Unit): FluidStack {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return buildTemplate(builderAction).map(FluidStackTemplate::create).getOrElse { FluidStack.EMPTY }
        }
    }

    val fluid: FluidHolder = FluidHolder()
    var amount: Int = FluidType.BUCKET_VOLUME
    var patch: DataComponentPatch = DataComponentPatch.EMPTY

    fun buildTemplate(): HTTextResult<FluidStackTemplate> {
        val item: Holder<Fluid> = this.fluid.value ?: return HTTextResult("Fluid must be non-empty")
        check(amount >= 0) { "Amount must not be negative" }
        if (amount == 0) return HTTextResult("Amount must be positive")
        return FluidStackTemplate(item, amount, patch).right()
    }

    @Suppress("DEPRECATION")
    @HTBuilderMarker
    class FluidHolder {
        var value: Holder<Fluid>? = null

        operator fun plusAssign(fluid: Fluid) {
            plusAssign(fluid.builtInRegistryHolder())
        }

        operator fun plusAssign(holder: Holder<Fluid>) {
            check(value == null) { "Fluid has already initialized" }
            if (holder.`is`(Fluids.EMPTY.builtInRegistryHolder())) return
            value = holder
        }
    }
}
