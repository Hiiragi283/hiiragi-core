package hiiragi283.core.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.HTMaterialKey

data object HCIntegrationMaterialKeys {
    @JvmStatic
    private fun create(path: String): HTMaterialKey = HTMaterialKey(HiiragiCoreAPI.id(path))

    //    AE2    //

    // Gem
    @JvmStatic
    val CERTUS_QUARTZ: HTMaterialKey = create("certus_quartz")

    @JvmStatic
    val FLUIX: HTMaterialKey = create("fluix")

    // Other
    @JvmStatic
    val SKY_STONE: HTMaterialKey = create("sky_stone")

    //    Create    //

    // Gem
    @JvmStatic
    val ROSE_QUARTZ: HTMaterialKey = create("rose_quartz")

    // Alloy
    @JvmStatic
    val ANDESITE_ALLOY: HTMaterialKey = create("andesite_alloy")

    // Other
    @JvmStatic
    val CARDBOARD: HTMaterialKey = create("cardboard")

    //    Ender IO    //

    //    Immersive Engineering    //

    @JvmStatic
    val HOP_GRAPHITE: HTMaterialKey = create("hop_graphite")

    //    Just Dire Things    //

    //    Mekanism    //

    // Alloy
    @JvmStatic
    val REFINED_GLOWSTONE: HTMaterialKey = create("refined_glowstone")

    @JvmStatic
    val REFINED_OBSIDIAN: HTMaterialKey = create("refined_obsidian")

    //    Oritech    //

    // Gem
    @JvmStatic
    val FLUXITE: HTMaterialKey = create("fluxite")

    // Alloy
    @JvmStatic
    val ENERGITE: HTMaterialKey = create("energite")

    @JvmStatic
    val ADAMANT: HTMaterialKey = create("adamant")

    @JvmStatic
    val DURATIUM: HTMaterialKey = create("duratium")

    @JvmStatic
    val PROMETHEUM: HTMaterialKey = create("prometheum")

    //    Replication    //

    @JvmStatic
    val REPLICA: HTMaterialKey = create("replica")
}
