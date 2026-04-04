package hiiragi283.core.impl.material

import hiiragi283.core.api.material.HTMaterialComponentAccess
import hiiragi283.core.api.text.Text
import hiiragi283.core.setup.HCMaterialComponents
import net.minecraft.core.component.DataComponentType

class HTMaterialComponentAccessImpl : HTMaterialComponentAccess {
    override fun materialName(): DataComponentType<Text> = HCMaterialComponents.MATERIAL_NAME
}
