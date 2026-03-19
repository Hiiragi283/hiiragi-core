package hiiragi283.core.api.recipe

import hiiragi283.core.api.HTConst
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

//    SizedIngredient    //

infix fun Ingredient.withSize(count: Int): SizedIngredient = SizedIngredient(this, count)

fun SizedIngredient.test(resource: ItemResource, count: Int = 1): Boolean = this.test(resource.toStack(count))

fun SizedIngredient.testOnlyType(resource: ItemResource): Boolean = this.testOnlyType(resource.toStack())

fun SizedIngredient.testOnlyType(stack: ItemStack): Boolean = this.ingredient().test(stack)

//    SizedFluidIngredient    //

infix fun FluidIngredient.withSize(amount: Int): SizedFluidIngredient = SizedFluidIngredient(this, amount)

fun SizedFluidIngredient.test(resource: FluidResource, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): Boolean =
    this.test(resource.toStack(amount))

fun SizedFluidIngredient.testOnlyType(resource: FluidResource): Boolean = this.testOnlyType(resource.toStack(HTConst.DEFAULT_FLUID_AMOUNT))

fun SizedFluidIngredient.testOnlyType(stack: FluidStack): Boolean = this.ingredient().test(stack)
