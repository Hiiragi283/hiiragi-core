package hiiragi283.core.api.data.holder

import hiiragi283.core.api.registry.getHolderLike
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
    /**
     * 保持している[ItemStack]
     */
    lateinit var stack: ItemStack
        private set

    /**
     * 指定した[item]から[ItemStack]を追加します。
     */
    @JvmName("setItem")
    operator fun plusAssign(item: ItemLike) {
        this.plusAssign(ItemStack(item))
    }

    /**
     * 指定した[pair]から[ItemStack]を追加します。
     */
    @JvmName("setItem")
    operator fun plusAssign(pair: Pair<ItemLike, Int>) {
        val (item: ItemLike, count: Int) = pair
        this.plusAssign(ItemStack(item, count))
    }

    /**
     * 指定した[resource]から[ItemStack]を追加します。
     */
    @JvmName("setResource")
    operator fun plusAssign(resource: HTItemResourceType) {
        this.plusAssign(resource.toStack())
    }

    /**
     * 指定した[pair]から[ItemStack]を追加します。
     */
    @JvmName("setResource")
    operator fun plusAssign(pair: Pair<HTItemResourceType, Int>) {
        val (resource: HTItemResourceType, count: Int) = pair
        this.plusAssign(resource.toStack(count))
    }

    /**
     * 指定した[stack]を追加します。
     */
    @JvmName("setStack")
    operator fun plusAssign(stack: ItemStack) {
        check(!::stack.isInitialized) { "Item Stack has already been initialized" }
        this.stack = stack
    }

    override fun getId(): ResourceLocation = stack.getHolderLike().getId()
}
