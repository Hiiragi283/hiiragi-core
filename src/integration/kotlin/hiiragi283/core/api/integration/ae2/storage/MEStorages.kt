package hiiragi283.core.api.integration.ae2.storage

import appeng.api.config.Actionable
import hiiragi283.core.api.storage.HTStorageAction

fun Actionable.toAction(): HTStorageAction = when (this) {
    Actionable.MODULATE -> HTStorageAction.EXECUTE
    Actionable.SIMULATE -> HTStorageAction.SIMULATE
}
