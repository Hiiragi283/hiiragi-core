package hiiragi283.core.api.data.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [HTFluidResult]向けの[HTResultCreator]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
data object HTFluidResultCreator : HTResultCreator<Fluid, HTFluidResourceType, FluidStack, HTFluidResult>() {
    fun create(content: HTFluidContent<*, *, *>, amount: Int = defaultAmount()): HTFluidResult =
        create(content.get(), content.fluidTag, amount)

    fun water(amount: Int = defaultAmount()): HTFluidResult = create(Fluids.WATER, Tags.Fluids.WATER, amount)

    fun lava(amount: Int = defaultAmount()): HTFluidResult = create(Fluids.LAVA, Tags.Fluids.LAVA, amount)

    fun milk(amount: Int = defaultAmount()): HTFluidResult = create(NeoForgeMod.MILK.value(), Tags.Fluids.MILK, amount)

    //    HTResultCreator    //

    override fun defaultAmount(): Int = HTConst.DEFAULT_FLUID_AMOUNT

    override fun createResource(type: Fluid): HTFluidResourceType = HTFluidResourceType.of(type)

    override fun create(contents: Ior<HTFluidResourceType, TagKey<Fluid>>, amount: Int): HTFluidResult = HTFluidResult(contents, amount)
}
