package hiiragi283.core.api.data.holder

import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * 単一の[ItemStack]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTItemStackHolder : HTIdLike {
    lateinit var stack: ItemStack
        private set

    @JvmName("setItem")
    operator fun plusAssign(item: ItemLike) {
        this.plusAssign(ItemStack(item))
    }

    @JvmName("setItem")
    operator fun plusAssign(pair: Pair<ItemLike, Int>) {
        val (item: ItemLike, count: Int) = pair
        this.plusAssign(ItemStack(item, count))
    }

    @JvmName("setResource")
    operator fun plusAssign(resource: HTItemResourceType) {
        this.plusAssign(resource.toStack())
    }

    @JvmName("setResource")
    operator fun plusAssign(pair: Pair<HTItemResourceType, Int>) {
        val (resource: HTItemResourceType, count: Int) = pair
        this.plusAssign(resource.toStack(count))
    }

    @JvmName("setStack")
    operator fun plusAssign(stack: ItemStack) {
        check(!::stack.isInitialized) { "Item Stack has already been initialized" }
        this.stack = stack
    }

    override fun getId(): ResourceLocation = stack.itemHolder.toLike().getId()
}
