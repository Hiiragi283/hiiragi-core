package hiiragi283.core.common.storage.energy

import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.energy.HTEnergyHandler

data object HTInfiniteEnergyHandler : HTEnergyHandler, HTValueSerializable.Empty {
    override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = 0

    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

    override fun getAmount(): Int = Int.MAX_VALUE

    override fun getCapacity(): Int = Int.MAX_VALUE
}
