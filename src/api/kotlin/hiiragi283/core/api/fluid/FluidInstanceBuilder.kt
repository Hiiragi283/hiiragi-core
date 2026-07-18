@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.fluid

import hiiragi283.core.api.data.HolderAcceptor
import hiiragi283.core.api.data.buildDataPatch
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * [FluidStack]向けのビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@HTBuilderMarker
class FluidInstanceBuilder : HolderAcceptor.FluidAcceptor {
    companion object {
        /**
         * [FluidStack]を作成します。
         */
        @JvmStatic
        inline fun buildStack(builderAction: FluidInstanceBuilder.() -> Unit): FluidStack {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return FluidInstanceBuilder().apply(builderAction).run { FluidStack(fluid, amount, patch) }
        }
    }

    @PublishedApi internal var fluid: Holder<Fluid> by HTDelegates.onceInitialize()
    var amount: Int = FluidType.BUCKET_VOLUME

    @PublishedApi internal var patch: DataComponentPatch = DataComponentPatch.EMPTY

    override operator fun Holder<Fluid>.unaryPlus() {
        fluid = this
    }

    operator fun HTFluidContent.unaryPlus() {
        +this.sourceHolder
    }

    operator fun DataComponentPatch.unaryPlus() {
        patch = this
    }

    inline fun components(builderAction: DataComponentPatch.Builder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        patch = buildDataPatch(builderAction)
    }
}
