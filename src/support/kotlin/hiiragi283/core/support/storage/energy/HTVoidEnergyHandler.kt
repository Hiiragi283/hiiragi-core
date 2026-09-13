package hiiragi283.core.support.storage.energy

import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.energy.HTEnergyHandler

data object HTVoidEnergyHandler : HTEnergyHandler, HTValueSerializable by HTValueSerializable.NOTHING {
    override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = 0

    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = 0

    override fun getAmount(): Int = 0

    override fun getCapacity(): Int = Int.MAX_VALUE
}
