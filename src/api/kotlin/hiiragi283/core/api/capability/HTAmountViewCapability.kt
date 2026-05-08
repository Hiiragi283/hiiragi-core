package hiiragi283.core.api.capability

import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * [HTAmountView]を取得する[HTMultiCapability]の拡張インターフェース
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTAmountViewCapability<HANDLER : Any, ITEM_HANDLER : HANDLER> : HTMultiCapability<HANDLER, ITEM_HANDLER> {
    fun apply(handler: HANDLER, context: Direction?): List<HTAmountView>

    //    Block    //

    /**
     * 指定した引数から[HTAmountView]の一覧を返します。
     * @return [HTAmountView]の[List]
     */
    fun getCapabilityViews(level: Level, pos: BlockPos, side: Direction?): List<HTAmountView> = getCapability(level, pos, side)?.let { apply(it, side) } ?: emptyList()

    /**
     * 指定した引数から[index]に対応する[HTAmountView]を返します。
     * @return 見つからない場合は`null`
     */
    fun getCapabilityView(
        level: Level,
        pos: BlockPos,
        side: Direction?,
        index: Int,
    ): HTAmountView? = getCapabilityViews(level, pos, side).getOrNull(index)

    //    Entity    //

    fun getCapabilityViews(entity: Entity, side: Direction?): List<HTAmountView> = getCapability(entity, side)?.let { apply(it, side) } ?: emptyList()

    fun getCapabilityView(entity: Entity, side: Direction?, index: Int): HTAmountView? = getCapabilityViews(entity, side).getOrNull(index)

    fun getCapabilityAmounts(entity: Entity, side: Direction?): List<Int> = getCapabilityViews(entity, side).map(HTAmountView::getAmount)

    fun getCapabilityAmount(entity: Entity, side: Direction?, index: Int): Int? = getCapabilityView(entity, side, index)?.getAmount()

    //    Item    //

    /**
     * 指定した引数から[HTAmountView]の一覧を返します。
     * @return [HTAmountView]の[List]
     */
    fun getCapabilityViews(stack: ItemStack): List<HTAmountView> = getCapability(stack)?.let { apply(it, null) } ?: emptyList()

    /**
     * 指定した引数から[index]に対応する[HTAmountView]を返します。
     * @return 見つからない場合は`null`
     */
    fun getCapabilityView(stack: ItemStack, index: Int): HTAmountView? = getCapabilityViews(stack).getOrNull(index)

    fun getCapabilityAmounts(stack: ItemStack): List<Int> = getCapabilityViews(stack).map(HTAmountView::getAmount)

    fun getCapabilityAmount(stack: ItemStack, index: Int): Int? = getCapabilityView(stack, index)?.getAmount()

    // HTItemResourceType

    fun getCapabilityViews(resource: HTItemResourceType?): List<HTAmountView> = getCapability(resource)?.let { apply(it, null) } ?: emptyList()

    fun getCapabilityView(resource: HTItemResourceType?, index: Int): HTAmountView? = getCapabilityViews(resource).getOrNull(index)

    fun getCapabilityAmounts(resource: HTItemResourceType?): List<Int> = getCapabilityViews(resource).map(HTAmountView::getAmount)

    fun getCapabilityAmount(resource: HTItemResourceType?, index: Int): Int? = getCapabilityView(resource, index)?.getAmount()

    interface Simple<HANDLER : Any> : HTAmountViewCapability<HANDLER, HANDLER>
}
