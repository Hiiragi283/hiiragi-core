package hiiragi283.core.api.data.holder

import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.holderLike
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

/**
 * 単一の[ItemStackTemplate]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTItemStackHolder : HTIdLike {
    /**
     * 保持している[ItemStackTemplate]
     */
    lateinit var template: ItemStackTemplate
        private set

    /**
     * 指定した[item]から[ItemStackTemplate]を追加します。
     */
    @JvmName("setItem")
    operator fun plusAssign(item: ItemLike) {
        this.plusAssign(ItemStackTemplate(item.asItem()))
    }

    /**
     * 指定した[pair]から[ItemStackTemplate]を追加します。
     */
    @JvmName("setItem")
    operator fun plusAssign(pair: Pair<ItemLike, Int>) {
        val (item: ItemLike, count: Int) = pair
        this.plusAssign(ItemStackTemplate(item.asItem(), count))
    }

    /**
     * 指定した[stack]を追加します。
     */
    @JvmName("setStack")
    operator fun plusAssign(stack: ItemStackTemplate) {
        check(!::template.isInitialized) { "Item Stack has already been initialized" }
        this.template = stack
    }

    override fun getId(): Identifier = template.holderLike().getId()

    fun createResult(chance: Float): HTItemResult = HTItemResult(template, chance)
}
