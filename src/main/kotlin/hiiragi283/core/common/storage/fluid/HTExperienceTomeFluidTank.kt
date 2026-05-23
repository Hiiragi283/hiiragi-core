package hiiragi283.core.common.storage.fluid

import com.google.common.primitives.Ints
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.impl.storage.fluid.HTItemFluidTank
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.util.HTExperienceHelper
import net.minecraft.world.item.ItemStack

class HTExperienceTomeFluidTank(override val container: ItemStack) : HTItemFluidTank {
    fun getExpRatio(): Int = HTExperienceHelper.getExpRatio()

    override fun isValid(resource: HTFluidResourceType): Boolean = resource.typeHolder().`is`(HCFluids.EXPERIENCE.fluidTag)

    override fun insert(
        resource: HTFluidResourceType?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int {
        if (resource == null || amount <= 0) return 0
        val needed: Int = getNeeded(resource)
        if (needed < getExpRatio() || !isValid(resource)) return amount

        val validAmount: Int = minOf(amount, needed)
        val fixedAmount: Int = validAmount - (validAmount % getExpRatio())
        if (fixedAmount <= 0) return amount
        if (action.execute()) {
            HTExperienceHelper.updateStoredExp(container) { it + HTExperienceHelper.expAmountFromFluid(fixedAmount) }
        }
        return amount - fixedAmount
    }

    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
        val resourceIn: HTFluidResourceType? = this.getResource()
        // 液体から経験値に変換したときに値が0になる場合はパス
        if (resourceIn == null || amount <= 0) return 0
        val validAmount: Int = minOf(amount, getAmount())
        val fixedAmount: Int = validAmount - (validAmount % getExpRatio())
        if (fixedAmount > 0 && action.execute()) {
            HTExperienceHelper.updateStoredExp(container) { maxOf(0, it - HTExperienceHelper.expAmountFromFluid(fixedAmount)) }
        }
        return fixedAmount
    }

    override fun getResource(): HTFluidResourceType? = HCFluids.EXPERIENCE.toStack().toResource()

    override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

    override fun getAmount(): Int = HTExperienceHelper
        .getStoredExp(container)
        .let(HTExperienceHelper::fluidAmountFromExp)
        .let(Ints::saturatedCast)
}
