package hiiragi283.core.api.stack

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

fun ItemLike.toImmutableStack(count: Int): ImmutableItemStack? = ImmutableItemStack.ofNullable(this, count)

fun ItemStack.toImmutable(): ImmutableItemStack? = ImmutableItemStack.of(this)

fun ItemStack.toImmutableOrThrow(): ImmutableItemStack = this.toImmutable() ?: error("ItemStack must not be empty")

fun ImmutableItemStack.maxStackSize(): Int = this.unwrap().maxStackSize

fun ImmutableItemStack.hasCraftingRemainingItem(): Boolean = this.unwrap().hasCraftingRemainingItem()

fun ImmutableItemStack.getCraftingRemainingItem(): ItemStack = this.unwrap().craftingRemainingItem

fun ImmutableItemStack.getFoodProperties(entity: LivingEntity?): FoodProperties? = this.unwrap().getFoodProperties(entity)
