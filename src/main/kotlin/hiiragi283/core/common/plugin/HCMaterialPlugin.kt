package hiiragi283.core.common.plugin

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addBlockPrefixes
import hiiragi283.core.api.material.property.addFluidPrefixes
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.addToolPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.fluid.CommonFluidTagPrefixes
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.setup.HCToolMaterials
import net.minecraft.resources.ResourceLocation

@HTPlugin
object HCMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = -1000

    override fun getId(): ResourceLocation = HiiragiCoreAPI.id("material_plugin")

    override fun onModifyMaterial(builder: HTMaterialPlugin.MaterialBuilder) {
        gem(builder)
        pearl(builder)
    }

    @JvmStatic
    private fun gem(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(HCMaterialKeys.AZURE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Azure Shard", "紺碧の欠片")
            setTextureSet("amethyst", HTMaterialTextureSet.SHINE)
        }
        builder.getBuilder(HCMaterialKeys.CRIMSON_CRYSTAL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)

            setName("Crimson Crystal", "深紅のクリスタリル")
            setTextureSet("emerald")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 24)
        }
        builder.getBuilder(HCMaterialKeys.WARPED_CRYSTAL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)

            setName("Warped Crystal", "歪んだクリスタリル")
            setTextureSet("emerald")
        }
    }

    @JvmStatic
    private fun pearl(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(HCMaterialKeys.ELDRITCH).apply {
            setDefaultPart(HTDefaultPart.Prefixed.PEARL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PEARL)

            setName("Eldritch Pearl", "異質な真珠")
            setTextureSet("pearl", HTMaterialTextureSet.MYSTICAL)
        }
    }

    @JvmStatic
    private fun alloy(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(HCMaterialKeys.AZURE_STEEL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.INGOT,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
            )
            addToolPrefixes(HCToolMaterials.AZURE_STEEL, CommonToolTypes.VANILLA_SET)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Azure Steel", "紺碧鋼")
        }
    }
}
