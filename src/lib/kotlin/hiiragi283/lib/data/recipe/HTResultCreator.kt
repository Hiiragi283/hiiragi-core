package hiiragi283.lib.data.recipe

import hiiragi283.lib.item.createItemTemplate
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.registry.HTFluidHolderLike
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

data object HTResultCreator {
    //    Item    //

    fun create(item: ItemLike, amount: Int = 1): HTItemResult = createItemTemplate(item, amount).map(::create).getOrThrow()

    fun create(template: ItemStackTemplate): HTItemResult = HTItemResult.Simple(template)

    fun create(tagKey: TagKey<Item>, count: Int = 1): HTItemResult = HTItemResult.Tagged(tagKey, count)

    //    Fluid    //

    fun create(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = create(FluidStackTemplate(fluid, amount))

    fun create(fluid: HTFluidHolderLike<*>, amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = fluid.toTemplate(amount).map(::create).getOrThrow()

    fun create(template: FluidStackTemplate): HTFluidResult = HTFluidResult.create(template)

    fun water(amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = create(Fluids.WATER, amount)

    fun lava(amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = create(Fluids.LAVA, amount)

    fun milk(amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = create(NeoForgeMod.MILK.get(), amount)
}
