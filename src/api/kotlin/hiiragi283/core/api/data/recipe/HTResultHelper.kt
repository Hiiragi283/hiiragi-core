package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.item.builtInRegistryHolder
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTHolderOrTagKey
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidWithTag
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

object HTResultHelper {
    //    Item    //

    @JvmStatic
    fun item(item: ItemLike, count: Int = 1, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult =
        itemHolder(item.builtInRegistryHolder(), count, components)

    @JvmStatic
    fun stack(stack: ItemStack): HTItemResult = itemHolder(stack.itemHolder, stack.count, stack.componentsPatch)

    @JvmStatic
    fun itemHolder(holder: Holder<Item>, count: Int = 1, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult =
        item(HTHolderOrTagKey(holder), count, components)

    @JvmStatic
    fun item(
        prefix: HTPrefixLike,
        material: HTMaterialLike,
        count: Int = 1,
        item: ItemLike? = null,
    ): HTItemResult = item(prefix, material, count, item?.builtInRegistryHolder())

    @JvmStatic
    fun item(
        prefix: HTPrefixLike,
        material: HTMaterialLike,
        count: Int = 1,
        holder: Holder<Item>? = null,
    ): HTItemResult = item(prefix.itemTagKey(material), count, holder)

    @JvmStatic
    fun item(tagKey: TagKey<Item>, count: Int = 1, holder: Holder<Item>? = null): HTItemResult =
        item(HTHolderOrTagKey(tagKey, holder), count)

    @JvmStatic
    fun item(entry: HTHolderOrTagKey<Item>, count: Int = 1, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult =
        HTItemResult(entry, count, components)

    //    Fluid    //

    @JvmStatic
    fun fluid(fluid: Fluid, amount: Int, components: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResult {
        val stack = FluidStack(fluid, amount)
        stack.applyComponents(components)
        return stack(stack)
    }

    @JvmStatic
    fun stack(stack: FluidStack): HTFluidResult = fluidHolder(stack.fluidHolder, stack.amount, stack.componentsPatch)

    @JvmStatic
    fun fluidHolder(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResult =
        fluid(HTHolderOrTagKey(holder), amount, components)

    @JvmStatic
    fun fluid(fluid: HTFluidWithTag<*>, amount: Int): HTFluidResult = fluid(fluid.getFluidTag(), amount, fluid.getHolder())

    @JvmStatic
    fun fluid(tagKey: TagKey<Fluid>, amount: Int, holder: Holder<Fluid>? = null): HTFluidResult =
        fluid(HTHolderOrTagKey(tagKey, holder), amount)

    @JvmStatic
    fun fluid(entry: HTHolderOrTagKey<Fluid>, amount: Int, components: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResult =
        HTFluidResult(entry, amount, components)
}
