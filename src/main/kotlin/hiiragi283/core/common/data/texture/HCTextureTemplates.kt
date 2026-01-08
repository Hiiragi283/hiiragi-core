package hiiragi283.core.common.data.texture

import hiiragi283.core.api.material.attribute.HTTextureTemplateMaterialAttribute
import hiiragi283.core.common.material.HCMaterialPrefixes

object HCTextureTemplates {
    @JvmField
    val FUEL: HTTextureTemplateMaterialAttribute = HTTextureTemplateMaterialAttribute.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_fuel")

        add(HCMaterialPrefixes.FUEL)
        addCustom(HCMaterialPrefixes.DUST, "dust_dull")
    }

    @JvmField
    val DUST: HTTextureTemplateMaterialAttribute = HTTextureTemplateMaterialAttribute.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_dust")

        add(HCMaterialPrefixes.DUST)
    }

    @JvmField
    val DUST_DULL: HTTextureTemplateMaterialAttribute = HTTextureTemplateMaterialAttribute.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_dust")

        addCustom(HCMaterialPrefixes.DUST, "dust_dull")
    }

    @JvmField
    val DUST_SHINE: HTTextureTemplateMaterialAttribute = HTTextureTemplateMaterialAttribute.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_dust")

        addCustom(HCMaterialPrefixes.DUST, "dust_shine")
    }

    @JvmField
    val GEM_LAPIS: HTTextureTemplateMaterialAttribute = gem("lapis")

    @JvmField
    val GEM_QUARTZ: HTTextureTemplateMaterialAttribute = gem("quartz", true)

    @JvmField
    val GEM_AMETHYST: HTTextureTemplateMaterialAttribute = gem("amethyst")

    @JvmField
    val GEM_DIAMOND: HTTextureTemplateMaterialAttribute = gem("diamond", true)

    @JvmField
    val GEM_EMERALD: HTTextureTemplateMaterialAttribute = gem("emerald", true)

    @JvmField
    val GEM_ECHO: HTTextureTemplateMaterialAttribute = gem("echo")

    @JvmField
    val PEARL: HTTextureTemplateMaterialAttribute = HTTextureTemplateMaterialAttribute.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_pearl")

        add(HCMaterialPrefixes.PEARL)
        add(HCMaterialPrefixes.DUST)
    }

    @JvmField
    val METAL: HTTextureTemplateMaterialAttribute = HTTextureTemplateMaterialAttribute.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_ingot")
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW)

        add(HCMaterialPrefixes.RAW_MATERIAL)
        add(HCMaterialPrefixes.SCRAP)
        add(HCMaterialPrefixes.INGOT)
        add(HCMaterialPrefixes.DUST)
        add(HCMaterialPrefixes.GEAR)
        add(HCMaterialPrefixes.NUGGET)
        add(HCMaterialPrefixes.PLATE)
        add(HCMaterialPrefixes.ROD)
        add(HCMaterialPrefixes.WIRE)
    }

    @JvmField
    val METAL_SHINE: HTTextureTemplateMaterialAttribute = HTTextureTemplateMaterialAttribute.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_ingot_shine")
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW)

        add(HCMaterialPrefixes.RAW_MATERIAL)
        add(HCMaterialPrefixes.SCRAP)
        addCustom(HCMaterialPrefixes.INGOT, "ingot_shine")
        addCustom(HCMaterialPrefixes.DUST, "dust_shine")
        add(HCMaterialPrefixes.GEAR)
        addCustom(HCMaterialPrefixes.NUGGET, "nugget_shine")
        addCustom(HCMaterialPrefixes.PLATE, "plate_shine")
        addCustom(HCMaterialPrefixes.ROD, "rod_shine")
        addCustom(HCMaterialPrefixes.WIRE, "wire_shine")
    }

    @JvmStatic
    private fun gem(suffix: String, altBlock: Boolean = false): HTTextureTemplateMaterialAttribute =
        HTTextureTemplateMaterialAttribute.create {
            if (altBlock) {
                addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_gem_$suffix")
            } else {
                addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_gem")
            }

            addCustom(HCMaterialPrefixes.GEM, "gem_$suffix")
            addCustom(HCMaterialPrefixes.DUST, "dust_shine")
        }
}
