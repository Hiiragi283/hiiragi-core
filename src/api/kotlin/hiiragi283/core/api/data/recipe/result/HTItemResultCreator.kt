package hiiragi283.core.api.data.recipe.result

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.storage.item.HTItemResourceFactory
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * [HTItemResult]向けの[HTResultCreator]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
data object HTItemResultCreator : HTResultCreator<Item, HTItemResourceType, ItemStack, HTItemResult>() {
    fun create(item: ItemLike, amount: Int = defaultAmount()): HTItemResult = create(item.asItem(), amount)

    fun create(item: ItemLike, tagKey: TagKey<Item>, amount: Int = defaultAmount()): HTItemResult = create(item.asItem(), tagKey, amount)

    fun create(prefix: HTTagPrefix, material: HTMaterialLike, amount: Int = defaultAmount()): HTItemResult =
        create(prefix.itemTagKey(material), amount)

    fun create(
        item: ItemLike,
        prefix: HTTagPrefix,
        material: HTMaterialLike,
        amount: Int = defaultAmount(),
    ): HTItemResult = create(item, prefix.itemTagKey(material), amount)

    fun create(stack: ItemStack): HTItemResult = create(resourceFactory().fromStackOrThrow(stack), stack.count)

    //    HTResultCreator    //

    override fun resourceFactory(): HTItemResourceFactory = HTItemResourceFactory

    override fun create(contents: Ior<HTItemResourceType, TagKey<Item>>, amount: Int): HTItemResult = HTItemResult(contents, amount)
}
