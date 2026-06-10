package hiiragi283.lib.data.recipe

import hiiragi283.lib.fluid.HTFluidInstanceBuilder
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.VanillaFluidContents
import hiiragi283.lib.util.HTBuilderMarker
import kotlin.properties.Delegates
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStackTemplate

@HTBuilderMarker
class HTFluidResultBuilder {
    @PublishedApi internal var result: HTFluidResult by Delegates.notNull()

    var amount: Int
        get() = result.amount
        set(value) {
            result = result.copyWithAmount(value)
        }

    operator fun FluidStackTemplate.unaryPlus() {
        result = HTFluidResult.create(this)
    }

    operator fun Fluid.unaryPlus() {
        HTFluidInstanceBuilder.buildTemplate { fluid += this@unaryPlus }.onRight { +it }
    }

    operator fun HTFluidContent.unaryPlus() {
        this.toTemplate().onRight { +it }
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
