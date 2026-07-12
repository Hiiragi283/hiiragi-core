package hiiragi283.lib.material

import hiiragi283.lib.item.HTSimpleItemLike
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.HTIdLike
import net.minecraft.references.BlockItemId
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item

/**
 * ブロックまたはアイテムを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed interface HTMaterialItemEntry :
    HTIdLike,
    HTSimpleItemLike

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
fun HTMaterialItemEntry(id: BlockItemId): HTMaterialItemEntry = HTMaterialItemEntry(HTSimpleDeferredBlockAndItem(id))

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
fun HTMaterialItemEntry(block: HTSimpleDeferredBlockAndItem): HTMaterialItemEntry = DefaultEntry(block)

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
fun HTMaterialItemEntry(key: ResourceKey<Item>): HTMaterialItemEntry = HTMaterialItemEntry(HTSimpleDeferredItem(key))

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
fun HTMaterialItemEntry(item: HTSimpleDeferredItem): HTMaterialItemEntry = DefaultEntry(item)

private class DefaultEntry<T>(delegate: T) :
    HTMaterialItemEntry,
    HTIdLike by delegate,
    HTSimpleItemLike by delegate
    where T : HTIdLike, T : HTSimpleItemLike
