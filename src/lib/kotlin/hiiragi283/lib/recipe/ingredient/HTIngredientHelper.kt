package hiiragi283.lib.recipe.ingredient

import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

data object HTIngredientHelper {
    //    Fluid    //

    @JvmName("createFluidStack")
    @JvmStatic
    fun createStack(instance: TypedInstance<Fluid>): FluidStack = when (instance) {
        is FluidInstance -> {
            when (instance) {
                is FluidStack -> instance
                is FluidStackTemplate -> instance.create()
                else -> FluidStack(instance.typeHolder(), instance.amount())
            }
        }
        is FluidResource -> instance.toStack(FluidType.BUCKET_VOLUME)
        else -> FluidStack(instance.typeHolder(), FluidType.BUCKET_VOLUME)
    }

    //    Item    //

    @JvmName("createItemStack")
    @JvmStatic
    fun createStack(instance: TypedInstance<Item>): ItemStack = when (instance) {
        is ItemInstance -> {
            when (instance) {
                is ItemStack -> instance
                is ItemStackTemplate -> instance.create()
                else -> ItemStack(instance.typeHolder(), instance.count())
            }
        }
        is ItemResource -> instance.toStack()
        else -> ItemStack(instance.typeHolder())
    }
}
