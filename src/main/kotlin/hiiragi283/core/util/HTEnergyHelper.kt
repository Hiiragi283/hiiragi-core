package hiiragi283.core.util

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.energy.HTEnergyBattery

object HTEnergyHelper {
    @JvmStatic
    fun moveEnergy(
        from: HTEnergyBattery?,
        to: HTEnergyBattery?,
        listener: HTContentListener?,
        amount: Int = from?.getAmount() ?: 0,
        access: HTStorageAccess = HTStorageAccess.INTERNAL,
    ): Int? {
        if (from == null || to == null || amount <= 0) return null
        val simulatedExtracted: Int = from.extract(amount, HTStorageAction.SIMULATE, access)
        val simulatedInserted: Int = to.insert(simulatedExtracted, HTStorageAction.EXECUTE, access)
        val extracted: Int = from.extract(simulatedInserted, HTStorageAction.EXECUTE, access)
        if (extracted > 0) {
            listener?.onContentsChanged()
        }
        return extracted
    }
}
