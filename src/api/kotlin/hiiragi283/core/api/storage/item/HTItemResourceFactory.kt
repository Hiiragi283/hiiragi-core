package hiiragi283.core.api.storage.item

import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.storage.resource.HTResourceFactory
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * [HTItemResourceType]向けの[HTResourceFactory]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object HTItemResourceFactory : HTResourceFactory.DataComponent<Item, ItemStack, HTItemResourceType>() {
    override fun create(type: Item, patch: DataComponentPatch): HTItemResourceType? = create(createItemStack(type, patch = patch))

    override fun create(stack: ItemStack): HTItemResourceType? = HTItemResourceType.of(stack)

    override fun createStack(resource: HTItemResourceType?, amount: Int): ItemStack = resource?.toStack(amount) ?: ItemStack.EMPTY

    override fun getDefaultAmount(): Int = 1
}
