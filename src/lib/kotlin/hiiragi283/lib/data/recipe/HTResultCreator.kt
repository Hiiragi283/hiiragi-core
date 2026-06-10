package hiiragi283.lib.data.recipe

import hiiragi283.lib.item.HTItemInstanceBuilder
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.util.getOrThrow
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

data object HTResultCreator {
    //    Item    //

    fun create(item: ItemLike, count: Int = 1): HTItemResult = HTItemInstanceBuilder.buildTemplate {
        this.item += item.asItem()
        this.count = count
    }.map(::create).getOrThrow()

    fun create(template: ItemStackTemplate): HTItemResult = HTItemResult.Simple(template)

    //    Fluid    //

    fun create(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = create(FluidStackTemplate(fluid, amount))

    fun create(content: HTFluidContent, amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = content.toTemplate { this.amount = amount }.map(::create).getOrThrow()

    fun create(template: FluidStackTemplate): HTFluidResult = HTFluidResult.create(template)

    fun water(amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = create(Fluids.WATER, amount)

    fun lava(amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = create(Fluids.LAVA, amount)

    fun milk(amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = create(NeoForgeMod.MILK.get(), amount)
}
