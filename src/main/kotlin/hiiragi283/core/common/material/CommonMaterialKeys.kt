package hiiragi283.core.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.HTMaterialKey

object CommonMaterialKeys {
    @JvmStatic
    private fun create(path: String): HTMaterialKey = HTMaterialKey.of((HiiragiCoreAPI.id(path)))

    //    Fuels    //

    @JvmStatic
    val COAL_COKE: HTMaterialKey = create("coal_coke")

    //    Minerals    //

    @JvmStatic
    val BAUXITE: HTMaterialKey = create("bauxite")

    @JvmStatic
    val CINNABAR: HTMaterialKey = create("cinnabar")

    @JvmStatic
    val SALT: HTMaterialKey = create("salt")

    @JvmStatic
    val SALTPETER: HTMaterialKey = create("saltpeter")

    @JvmStatic
    val SULFUR: HTMaterialKey = create("sulfur")

    //    Gems    //

    @JvmStatic
    val FLUORITE: HTMaterialKey = create("fluorite")

    @JvmStatic
    val PERIDOT: HTMaterialKey = create("peridot")

    @JvmStatic
    val RUBY: HTMaterialKey = create("ruby")

    @JvmStatic
    val SAPPHIRE: HTMaterialKey = create("sapphire")

    //    Metals    //

    // 3rd
    @JvmStatic
    val ALUMINUM: HTMaterialKey = create("aluminum")

    @JvmStatic
    val SILICON: HTMaterialKey = create("silicon")

    // 4th
    @JvmStatic
    val TITANIUM: HTMaterialKey = create("titanium")
    
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

    @JvmStatic
    val STEEL: HTMaterialKey = create("steel")

    @JvmStatic
    val INVAR: HTMaterialKey = create("invar")

    @JvmStatic
    val ELECTRUM: HTMaterialKey = create("electrum")

    @JvmStatic
    val BRASS: HTMaterialKey = create("brass")

    @JvmStatic
    val CONSTANTAN: HTMaterialKey = create("constantan")

    @JvmStatic
    val BRONZE: HTMaterialKey = create("bronze")

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
    val PLASTIC: HTMaterialKey = create("plastic")

    @JvmStatic
    val RUBBER: HTMaterialKey = create("rubber")
}
