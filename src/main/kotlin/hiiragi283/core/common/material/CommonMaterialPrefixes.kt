package hiiragi283.core.common.material

import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike

enum class CommonMaterialPrefixes(customPattern: String? = null) : HTPrefixLike {
    // Block
    ORE,
    GLASS_BLOCK,
    STORAGE_BLOCK("%s_block"),
    RAW_STORAGE_BLOCK("raw_%s_block") {
        override val prefix: HTMaterialPrefix = HTMaterialPrefix("raw_storage_block", "c:storage_blocks", "c:storage_blocks/raw_%s")
    },

    // Item
    CROP("%s"),
    DUST,
    FOOD("%s"),
    GEM,
    GEAR,
    INGOT,
    NUGGET,
    PLATE,
    RAW_MATERIAL("raw_%s"),
    ROD,

    // Item - Custom
    DOUGH,
    FLOUR,
    FUEL("%s"),
    PEARL,
    RAW_MATERIAL_DYE {
        override val prefix: HTMaterialPrefix = HTMaterialPrefix("raw_materials/dye")
    },
    SCRAP,
    ;

    protected open val prefix: HTMaterialPrefix = HTMaterialPrefix(name.lowercase())
    open val idPattern: String = customPattern ?: "%s_${name.lowercase()}"

    override fun asMaterialPrefix(): HTMaterialPrefix = prefix
}
