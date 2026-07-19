package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.fluid.HTFluidLike
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.util.HTBuilderMarker
import kotlin.properties.Delegates
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * [HTFluidResult]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@HTBuilderMarker
class HTFluidResultBuilder {
    @PublishedApi internal var result: HTFluidResult by Delegates.notNull()

    var amount: Int
        get() = result.amount
        set(value) {
            result = result.copyWithAmount(value)
        }

    operator fun FluidStack.unaryPlus() {
        result = HTFluidResult.create(this)
    }

    operator fun Fluid.unaryPlus() {
        +FluidStack(this, FluidType.BUCKET_VOLUME)
    }

    operator fun HTFluidLike<*>.unaryPlus() {
        +this.toStack()
    }

    operator fun HTFluidContent.unaryPlus() {
        +this.toStack()
    }

    fun water() {
        +VanillaFluidContents.WATER
    }

    fun lava() {
        +VanillaFluidContents.LAVA
    }

    fun milk() {
        +VanillaFluidContents.MILK
    }

    fun build(): HTFluidResult = result
}
