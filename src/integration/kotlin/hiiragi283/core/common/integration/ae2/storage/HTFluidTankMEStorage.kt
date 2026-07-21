package hiiragi283.core.common.integration.ae2.storage

import appeng.api.config.Actionable
import appeng.api.networking.security.IActionSource
import appeng.api.stacks.AEFluidKey
import appeng.api.stacks.AEKey
import appeng.api.stacks.KeyCounter
import appeng.api.storage.MEStorage
import com.google.common.primitives.Ints
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text

class HTFluidTankMEStorage(private val tank: HTFluidTank, title: HTHasText) :
    MEStorage,
    HTHasText by title {
    constructor(tank: HTFluidTank, title: Text) : this(tank, HTHasText { title })

    override fun isPreferredStorageFor(what: AEKey, source: IActionSource?): Boolean = when (what) {
        is AEFluidKey -> tank.getResource() == what.toResource()
        else -> super.isPreferredStorageFor(what, source)
    }

    override fun insert(
        what: AEKey,
        amount: Long,
        mode: Actionable,
        source: IActionSource,
    ): Long {
        MEStorage.checkPreconditions(what, amount, mode, source)
        when (what) {
            is AEFluidKey -> {
                val remainder: Int = tank.insert(what.toResource(), Ints.saturatedCast(amount), mode.toAction(), HTStorageAccess.EXTERNAL)
                return amount - remainder
            }

            else -> return super.insert(what, amount, mode, source)
        }
    }

    override fun extract(
        what: AEKey,
        amount: Long,
        mode: Actionable,
        source: IActionSource,
    ): Long {
        MEStorage.checkPreconditions(what, amount, mode, source)
        return when (what) {
            is AEFluidKey ->
                tank.extract(what.toResource(), Ints.saturatedCast(amount), mode.toAction(), HTStorageAccess.EXTERNAL).toLong()

            else -> super.extract(what, amount, mode, source)
        }
    }

    override fun getAvailableStacks(out: KeyCounter) {
        tank.getResource()?.toAEKey()?.let { out[it] = tank.getAmount().toLong() }
    }

    override fun getDescription(): Text = getText()
}
