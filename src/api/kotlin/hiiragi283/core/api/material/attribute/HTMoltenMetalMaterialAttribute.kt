package hiiragi283.core.api.material.attribute

import hiiragi283.core.api.registry.HTFluidContent

class HTMoltenMetalMaterialAttribute private constructor(val enabled: Boolean, val custom: HTFluidContent<*, *, *>?) :
    HTMaterialAttribute {
        companion object {
            @JvmStatic
            fun disable(): HTMoltenMetalMaterialAttribute = HTMoltenMetalMaterialAttribute(false, null)

            @JvmStatic
            fun enable(): HTMoltenMetalMaterialAttribute = HTMoltenMetalMaterialAttribute(true, null)

            @JvmStatic
            fun custom(content: HTFluidContent<*, *, *>): HTMoltenMetalMaterialAttribute = HTMoltenMetalMaterialAttribute(true, content)
        }
    }
