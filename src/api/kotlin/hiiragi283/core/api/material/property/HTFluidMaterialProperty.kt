package hiiragi283.core.api.material.property

import hiiragi283.core.api.registry.HTFluidContent

@JvmRecord
data class HTFluidMaterialProperty(val fluid: HTFluidContent<*, *, *>)
