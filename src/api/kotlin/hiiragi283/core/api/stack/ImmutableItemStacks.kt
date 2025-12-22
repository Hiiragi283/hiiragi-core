package hiiragi283.core.api.stack

import net.minecraft.world.item.ItemStack

/**
 * この[スタック][this]を[ImmutableItemStack]に変換します。
 * @return [ItemStack.isEmpty]が`true`の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ItemStack.toImmutable(): ImmutableItemStack? = ImmutableItemStack.of(this)

/**
 * この[スタック][this]を[ImmutableItemStack]に変換します。
 * @throws IllegalStateException [ItemStack.isEmpty]が`true`の場合
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ItemStack.toImmutableOrThrow(): ImmutableItemStack = this.toImmutable() ?: error("ItemStack must not be empty")

/**
 * この[スタック][this]の最大スタック数を返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ImmutableItemStack.maxStackSize(): Int = this.unwrap().maxStackSize
