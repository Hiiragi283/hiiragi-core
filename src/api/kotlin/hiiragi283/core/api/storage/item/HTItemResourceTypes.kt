package hiiragi283.core.api.storage.item

import net.minecraft.world.item.ItemStack

/**
 * この[ItemStack][this]を[HTItemResourceType]に変換します。
 * @return [ItemStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun ItemStack.toResource(): HTItemResourceType? = HTItemResourceType.of(this)

fun ItemStack.toResourcePair(): Pair<HTItemResourceType, Int>? {
    val resource: HTItemResourceType = this.toResource() ?: return null
    return resource to this.count
}
