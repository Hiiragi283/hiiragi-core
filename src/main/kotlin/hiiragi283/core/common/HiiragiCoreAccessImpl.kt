package hiiragi283.core.common

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.config.HCConfig

class HiiragiCoreAccessImpl : HiiragiCoreAccess {
    override fun getModIdPriorityList(): List<String> = HCConfig.COMMON.tagOutputPriority.get()
}
