package hiiragi283.core.api.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.Text
import net.minecraft.core.component.DataComponentType

interface HTMaterialComponentAccess {
    companion object {
        @JvmField
        val INSTANCE: HTMaterialComponentAccess = HiiragiCoreAPI.getService()
    }

    fun materialName(): DataComponentType<Text>
}
