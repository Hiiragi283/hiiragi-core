package hiiragi283.core.api.storage.item

import hiiragi283.core.api.item.createItemStack
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * この[ItemLike][this]を[HTItemResourceType]に変換します。
 * @param patch コンポーネントの差分
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun ItemLike?.toResource(patch: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResourceType? =
    createItemStack(this, patch = patch).toResource()

/**
 * この[ItemStack][this]を[HTItemResourceType]に変換します。
 * @return [ItemStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun ItemStack.toResource(): HTItemResourceType? = HTItemResourceType.of(this)

/**
 * この[ItemStack][this]を[HTItemResourceType]と数量に展開します。
 * @return [ItemStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
fun ItemStack.toResourcePair(): Pair<HTItemResourceType, Int>? {
    val resource: HTItemResourceType = this.toResource() ?: return null
    return resource to this.count
}

/**
 * この[HTItemResourceType][this]を[ItemStack]に変換します
 * @return この[HTItemResourceType]が`null`の場合は[ItemStack.EMPTY]
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun HTItemResourceType?.toStackOrEmpty(count: Int = 1): ItemStack = this?.toStack(count) ?: ItemStack.EMPTY
