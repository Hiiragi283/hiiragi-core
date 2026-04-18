package hiiragi283.core.impl.recipe.handler

import hiiragi283.core.api.recipe.handler.HTAmountInputHandler
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.energy.HTEnergyBattery

class HTEnergyInputHandler(battery: HTEnergyBattery) :
    HTAmountInputHandler,
    HTEnergyBattery by battery {
    override fun consume(amount: Int) {
        if (amount > 0) {
            this.extract(amount, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }
}
