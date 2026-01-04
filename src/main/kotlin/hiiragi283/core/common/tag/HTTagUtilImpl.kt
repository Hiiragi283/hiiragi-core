package hiiragi283.core.common.tag

import hiiragi283.core.api.tag.HTTagUtil
import hiiragi283.core.config.HCConfig

class HTTagUtilImpl : HTTagUtil {
    override fun getModIdPriorityList(): List<String> = HCConfig.COMMON.tagOutputPriority.get()
}
