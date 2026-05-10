package hiiragi283.core.api.util

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

/**
 * この[インスタンス][this]を[Optional]で包みます。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <T : Any> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun Optional<ItemStack>.getOrEmpty(): ItemStack = this.orElseGet(ItemStack::EMPTY)

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun Optional<FluidStack>.getOrEmpty(): FluidStack = this.orElseGet(FluidStack::EMPTY)
