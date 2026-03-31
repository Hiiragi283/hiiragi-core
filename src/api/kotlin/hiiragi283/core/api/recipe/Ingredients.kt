package hiiragi283.core.api.recipe

import hiiragi283.core.api.registry.toHolderSet
import hiiragi283.core.api.util.Either
import net.minecraft.client.Minecraft
import net.minecraft.core.HolderSet
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.SlotDisplayContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SimpleFluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.fluids.crafting.display.FluidStackContentsFactory
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

//    SizedIngredient    //

infix fun Ingredient.withSize(count: Int): SizedIngredient = SizedIngredient(this, count)

fun SizedIngredient.test(resource: ItemResource, count: Int = 1): Boolean = this.test(resource.toStack(count))

fun SizedIngredient.testOnlyType(resource: ItemResource): Boolean = this.testOnlyType(resource.toStack())

fun SizedIngredient.testOnlyType(stack: ItemStack): Boolean = this.ingredient().test(stack)

fun SizedIngredient.unwrap(): Either<HolderSet<Item>, List<ItemResource>> = this.unwrap(Minecraft.getInstance().level)

fun SizedIngredient.unwrap(level: Level?): Either<HolderSet<Item>, List<ItemResource>> {
    val contextMap: ContextMap = level?.let(SlotDisplayContext::fromLevel) ?: ContextMap.EMPTY
    val ingredient: Ingredient = this.ingredient()
    val custom: ICustomIngredient? = ingredient.customIngredient
    return when {
        custom != null -> Either.Right(custom.display().resolveForStacks(contextMap).map(ItemResource::of))
        else -> Either.Left(ingredient.values)
    }
}

//    SizedFluidIngredient    //

infix fun FluidIngredient.withSize(amount: Int): SizedFluidIngredient = SizedFluidIngredient(this, amount)

fun SizedFluidIngredient.test(resource: FluidResource, amount: Int = FluidType.BUCKET_VOLUME): Boolean = this.test(resource.toStack(amount))

fun SizedFluidIngredient.testOnlyType(resource: FluidResource): Boolean = this.testOnlyType(resource.toStack(FluidType.BUCKET_VOLUME))

fun SizedFluidIngredient.testOnlyType(stack: FluidStack): Boolean = this.ingredient().test(stack)

fun SizedFluidIngredient.unwrap(): Either<HolderSet<Fluid>, List<FluidResource>> = this.unwrap(Minecraft.getInstance().level)

fun SizedFluidIngredient.unwrap(level: Level?): Either<HolderSet<Fluid>, List<FluidResource>> {
    val contextMap: ContextMap = level?.let(SlotDisplayContext::fromLevel) ?: ContextMap.EMPTY
    val ingredient: FluidIngredient = this.ingredient()
    return when {
        ingredient is SimpleFluidIngredient -> Either.Left(ingredient.fluidSet())
        ingredient.isSimple -> Either.Left(ingredient.fluids().toHolderSet())
        else -> Either.Right(
            ingredient
                .display()
                .resolve(contextMap, FluidStackContentsFactory.INSTANCE)
                .map(FluidResource::of)
                .toList(),
        )
    }
}
