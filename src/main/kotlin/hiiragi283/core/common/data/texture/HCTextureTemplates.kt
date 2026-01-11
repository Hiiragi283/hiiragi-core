package hiiragi283.core.common.data.texture

import hiiragi283.core.api.material.property.HTTextureTemplate
import hiiragi283.core.common.material.HCMaterialPrefixes

object HCTextureTemplates {
    @JvmField
    val FUEL: HTTextureTemplate = HTTextureTemplate.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_fuel")

        add(HCMaterialPrefixes.FUEL)
        addCustom(HCMaterialPrefixes.DUST, "dust_dull")
    }

    @JvmField
    val DUST: HTTextureTemplate = HTTextureTemplate.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_dust")

        add(HCMaterialPrefixes.DUST)
    }

    @JvmField
    val DUST_DULL: HTTextureTemplate = HTTextureTemplate.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_dust")

        addCustom(HCMaterialPrefixes.DUST, "dust_dull")
    }

    @JvmField
    val DUST_SHINE: HTTextureTemplate = HTTextureTemplate.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_dust")

        addCustom(HCMaterialPrefixes.DUST, "dust_shine")
    }

    @JvmField
    val GEM_LAPIS: HTTextureTemplate = gem("lapis")

    @JvmField
    val GEM_QUARTZ: HTTextureTemplate = gem("quartz", true)

    @JvmField
    val GEM_AMETHYST: HTTextureTemplate = gem("amethyst")

    @JvmField
    val GEM_DIAMOND: HTTextureTemplate = gem("diamond", true)

    @JvmField
    val GEM_EMERALD: HTTextureTemplate = gem("emerald", true)

    @JvmField
    val GEM_ECHO: HTTextureTemplate = gem("echo")

    @JvmField
    val PEARL: HTTextureTemplate = HTTextureTemplate.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_pearl")

        add(HCMaterialPrefixes.PEARL)
        add(HCMaterialPrefixes.DUST)
    }

    @JvmField
    val METAL: HTTextureTemplate = HTTextureTemplate.create {
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
    val METAL_SHINE: HTTextureTemplate = HTTextureTemplate.create {
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
    private fun gem(suffix: String, altBlock: Boolean = false): HTTextureTemplate = HTTextureTemplate.create {
        if (altBlock) {
            addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_gem_$suffix")
        } else {
            addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_gem")
        }

        addCustom(HCMaterialPrefixes.GEM, "gem_$suffix")
        addCustom(HCMaterialPrefixes.DUST, "dust_shine")
    }
}
