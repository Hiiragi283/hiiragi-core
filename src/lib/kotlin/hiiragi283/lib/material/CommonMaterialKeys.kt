package hiiragi283.lib.material

import hiiragi283.lib.HTConstants

/**
 * 共通の素材について[HTMaterialKey]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
object CommonMaterialKeys {
    @JvmStatic
    private fun create(path: String): HTMaterialKey = HTMaterialKey(HTConstants.COMMON, path)

    //    Fuels    //

    @JvmStatic
    val COAL_COKE: HTMaterialKey = create("coal_coke")

    //    Minerals    //

    // Na
    @JvmStatic
    val SALT: HTMaterialKey = create("salt")

    @JvmStatic
    val SALTPETER: HTMaterialKey = create("saltpeter")

    // Al
    @JvmStatic
    val BAUXITE: HTMaterialKey = create("bauxite")

    // S
    @JvmStatic
    val SULFUR: HTMaterialKey = create("sulfur")

    // Hg
    @JvmStatic
    val CINNABAR: HTMaterialKey = create("cinnabar")

    // Pd
    @JvmStatic
    val GALENA: HTMaterialKey = create("galena")

    //    Gems    //

    /**
     * @since 26.1.1
     */
    @JvmStatic
    val AMBER: HTMaterialKey = create("amber")

    /**
     * @since 26.1.1
     */
    @JvmStatic
    val AQUAMARINE: HTMaterialKey = create("aquamarine")

    @JvmStatic
    val FLUORITE: HTMaterialKey = create("fluorite")

    @JvmStatic
    val PERIDOT: HTMaterialKey = create("peridot")

    @JvmStatic
    val RUBY: HTMaterialKey = create("ruby")

    @JvmStatic
    val SAPPHIRE: HTMaterialKey = create("sapphire")

    //    Metals    //

    // 2nd
    @JvmStatic
    val LITHIUM: HTMaterialKey = create("lithium")

    @JvmStatic
    val BERYLLIUM: HTMaterialKey = create("beryllium")

    // 3rd
    @JvmStatic
    val SODIUM: HTMaterialKey = create("sodium")

    @JvmStatic
    val MAGNESIUM: HTMaterialKey = create("magnesium")

    @JvmStatic
    val ALUMINUM: HTMaterialKey = create("aluminum")

    @JvmStatic
    val SILICON: HTMaterialKey = create("silicon")

    // 4th
    @JvmStatic
    val TITANIUM: HTMaterialKey = create("titanium")

    @JvmStatic
    val VANADIUM: HTMaterialKey = create("vanadium")

    @JvmStatic
    val CHROMIUM: HTMaterialKey = create("chromium")

    @JvmStatic
    val MANGANESE: HTMaterialKey = create("manganese")

    @JvmStatic
    val COBALT: HTMaterialKey = create("cobalt")

    @JvmStatic
    val NICKEL: HTMaterialKey = create("nickel")

    @JvmStatic
    val ZINC: HTMaterialKey = create("zinc")

    // 5th
    @JvmStatic
    val MOLYBDENUM: HTMaterialKey = create("molybdenum")

    @JvmStatic
    val RUTHENIUM: HTMaterialKey = create("ruthenium")

    @JvmStatic
    val RHODIUM: HTMaterialKey = create("rhodium")

    @JvmStatic
    val PALLADIUM: HTMaterialKey = create("palladium")

    @JvmStatic
    val SILVER: HTMaterialKey = create("silver")

    @JvmStatic
    val TIN: HTMaterialKey = create("tin")

    @JvmStatic
    val ANTIMONY: HTMaterialKey = create("antimony")

    // 6th
    @JvmStatic
    val TUNGSTEN: HTMaterialKey = create("tungsten")

    @JvmStatic
    val OSMIUM: HTMaterialKey = create("osmium")

    @JvmStatic
    val IRIDIUM: HTMaterialKey = create("iridium")

    @JvmStatic
    val PLATINUM: HTMaterialKey = create("platinum")

    @JvmStatic
    val LEAD: HTMaterialKey = create("lead")

    // 7th
    @JvmStatic
    val URANIUM: HTMaterialKey = create("uranium")

    @JvmStatic
    val PLUTONIUM: HTMaterialKey = create("plutonium")

    //    Alloys    //

    // Iron
    @JvmStatic
    val STEEL: HTMaterialKey = create("steel")

    @JvmStatic
    val INVAR: HTMaterialKey = create("invar")

    // Copper
    @JvmStatic
    val BRASS: HTMaterialKey = create("brass")

    @JvmStatic
    val CONSTANTAN: HTMaterialKey = create("constantan")

    @JvmStatic
    val BRONZE: HTMaterialKey = create("bronze")

    // Silver
    @JvmStatic
    val ELECTRUM: HTMaterialKey = create("electrum")

    // Thermal
    @JvmStatic
    val SIGNALUM: HTMaterialKey = create("signalum")

    @JvmStatic
    val LUMIUM: HTMaterialKey = create("lumium")

    @JvmStatic
    val ENDERIUM: HTMaterialKey = create("enderium")

    //    Others    //

    @JvmStatic
    val ASH: HTMaterialKey = create("ash")

    @JvmStatic
    val CARBON: HTMaterialKey = create("carbon")

    @JvmStatic
    val PLASTIC: HTMaterialKey = create("plastic")

    @JvmStatic
    val RUBBER: HTMaterialKey = create("rubber")
}
