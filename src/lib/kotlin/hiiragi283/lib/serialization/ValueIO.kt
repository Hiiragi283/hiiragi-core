package hiiragi283.lib.serialization

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.getOrElse
import hiiragi283.lib.util.kotlin
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack

//    ValueOutput    //

fun ValueOutput.putFluid(stack: FluidStack, alloyEmpty: Boolean = true) {
    this.store(HTConstants.FLUID, if (alloyEmpty) FluidStack.OPTIONAL_CODEC else FluidStack.CODEC, stack)
}

fun ValueOutput.putItem(stack: ItemStack, alloyEmpty: Boolean = true) {
    this.store(HTConstants.ITEM, if (alloyEmpty) ItemStack.OPTIONAL_CODEC else ItemStack.CODEC, stack)
}

//    ValueInput    //

/**
 * [read]を[Option]に変換して返します。
 * @param T 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any> ValueInput.readOption(name: String, codec: Codec<T>): Option<T> = this.read(name, codec).kotlin

fun ValueInput.getFluid(alloyEmpty: Boolean = true): Option<FluidStack> = this.readOption(HTConstants.FLUID, if (alloyEmpty) FluidStack.OPTIONAL_CODEC else FluidStack.CODEC)

fun ValueInput.getItem(alloyEmpty: Boolean = true): Option<ItemStack> = this.readOption(HTConstants.ITEM, if (alloyEmpty) ItemStack.OPTIONAL_CODEC else ItemStack.CODEC)

fun ValueInput.getFluidOrEmpty(alloyEmpty: Boolean = true): FluidStack = this.getFluid(alloyEmpty).getOrElse(FluidStack::EMPTY)

fun ValueInput.getItemOrEmpty(alloyEmpty: Boolean = true): ItemStack = this.getItem(alloyEmpty).getOrElse(ItemStack::EMPTY)
