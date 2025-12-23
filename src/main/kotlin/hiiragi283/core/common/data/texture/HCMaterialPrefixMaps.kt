package hiiragi283.core.common.data.texture

import hiiragi283.core.api.material.prefix.HTPrefixTemplateMap
import hiiragi283.core.common.material.HCMaterialPrefixes

object HCMaterialPrefixMaps {
    @JvmField
    val FUEL: HTPrefixTemplateMap = HTPrefixTemplateMap.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_fuel")

        add(HCMaterialPrefixes.FUEL)
        addCustom(HCMaterialPrefixes.DUST, "dust_dull")
        addCustom(HCMaterialPrefixes.TINY_DUST, "tiny_dust_dull")
    }

    @JvmField
    val DUST: HTPrefixTemplateMap = HTPrefixTemplateMap.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_dust")

        add(HCMaterialPrefixes.DUST)
        add(HCMaterialPrefixes.TINY_DUST)
    }
    
    @JvmField
    val DUST_DULL: HTPrefixTemplateMap = HTPrefixTemplateMap.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_dust")

        addCustom(HCMaterialPrefixes.DUST, "dust_dull")
        addCustom(HCMaterialPrefixes.TINY_DUST, "tiny_dust_dull")
    }

    @JvmField
    val DUST_SHINE: HTPrefixTemplateMap = HTPrefixTemplateMap.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_dust")

        addCustom(HCMaterialPrefixes.DUST, "dust_shine")
        addCustom(HCMaterialPrefixes.TINY_DUST, "tiny_dust_shine")
    }

    @JvmField
    val GEM_LAPIS: HTPrefixTemplateMap = gem("lapis")

    @JvmField
    val GEM_QUARTZ: HTPrefixTemplateMap = gem("quartz")

    @JvmField
    val GEM_AMETHYST: HTPrefixTemplateMap = gem("amethyst")

    @JvmField
    val GEM_DIAMOND: HTPrefixTemplateMap = gem("diamond")

    @JvmField
    val GEM_EMERALD: HTPrefixTemplateMap = gem("emerald")

    @JvmField
    val GEM_ECHO: HTPrefixTemplateMap = gem("echo")

    @JvmField
    val PEARL: HTPrefixTemplateMap = HTPrefixTemplateMap.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_pearl")

        add(HCMaterialPrefixes.PEARL)
        add(HCMaterialPrefixes.DUST)
        add(HCMaterialPrefixes.TINY_DUST)
    }

    @JvmField
    val METAL: HTPrefixTemplateMap = HTPrefixTemplateMap.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_ingot")
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW)

        add(HCMaterialPrefixes.RAW_MATERIAL)
        add(HCMaterialPrefixes.SCRAP)
        add(HCMaterialPrefixes.INGOT)
        add(HCMaterialPrefixes.DUST)
        add(HCMaterialPrefixes.TINY_DUST)
        add(HCMaterialPrefixes.GEAR)
        add(HCMaterialPrefixes.NUGGET)
        add(HCMaterialPrefixes.PLATE)
        add(HCMaterialPrefixes.ROD)
        add(HCMaterialPrefixes.WIRE)
    }

    @JvmField
    val METAL_SHINE: HTPrefixTemplateMap = HTPrefixTemplateMap.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_ingot_shine")
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW)

        add(HCMaterialPrefixes.RAW_MATERIAL)
        add(HCMaterialPrefixes.SCRAP)
        addCustom(HCMaterialPrefixes.INGOT, "ingot_shine")
        addCustom(HCMaterialPrefixes.DUST, "dust_shine")
        addCustom(HCMaterialPrefixes.TINY_DUST, "tiny_dust_shine")
        add(HCMaterialPrefixes.GEAR)
        addCustom(HCMaterialPrefixes.NUGGET, "nugget_shine")
        addCustom(HCMaterialPrefixes.PLATE, "plate_shine")
        addCustom(HCMaterialPrefixes.ROD, "rod_shine")
        addCustom(HCMaterialPrefixes.WIRE, "wire_shine")
    }

    @JvmStatic
    private fun gem(suffix: String): HTPrefixTemplateMap = HTPrefixTemplateMap.create {
        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_gem")

        addCustom(HCMaterialPrefixes.GEM, "gem_$suffix")
        addCustom(HCMaterialPrefixes.DUST, "dust_shine")
        addCustom(HCMaterialPrefixes.TINY_DUST, "tiny_dust_shine")
    }
}
