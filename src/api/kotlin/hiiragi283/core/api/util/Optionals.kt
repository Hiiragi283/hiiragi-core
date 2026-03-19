package hiiragi283.core.api.util

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

/**
 * 空の[Optional]を取得します。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <T : Any> emptyOptional(): Optional<T> = Optional.empty<T>()

/**
 * この[インスタンス][this]を[Optional]で包みます。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <T : Any> T?.wrapOptional(): Optional<T> = Optional.ofNullable(this)

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
inline fun <T : Any> Optional<T>.onPresent(action: (T) -> Unit): Optional<T> {
    if (this.isPresent) {
        action(this.get())
    }
    return this
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <T : Any, R : Any> Optional<T>.mapNotNull(transform: (T) -> R?): Optional<R> = this.flatMap { transform(it).wrapOptional() }

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
