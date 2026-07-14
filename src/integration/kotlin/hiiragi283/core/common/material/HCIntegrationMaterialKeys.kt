package hiiragi283.core.common.material

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.common.integration.HCIConstants

data object HCIntegrationMaterialKeys {
    //    AE2    //

    // Gem
    @JvmStatic
    val CERTUS_QUARTZ: HTMaterialKey = create(HCIConstants.AE2, "certus_quartz")

    @JvmStatic
    val FLUIX: HTMaterialKey = create(HCIConstants.AE2, "fluix")

    // Other
    @JvmStatic
    val SKY_STONE: HTMaterialKey = create(HCIConstants.AE2, "sky_stone")

    //    Create    //

    // Gem
    @JvmStatic
    val ROSE_QUARTZ: HTMaterialKey = create(HCIConstants.CREATE, "rose_quartz")

    // Alloy
    @JvmStatic
    val ANDESITE_ALLOY: HTMaterialKey = create(HCIConstants.CREATE, "andesite_alloy")

    // Other
    @JvmStatic
    val CARDBOARD: HTMaterialKey = create(HCIConstants.CREATE, "cardboard")

    //    Ender IO    //

    //    Immersive Engineering    //

    @JvmStatic
    val HOP_GRAPHITE: HTMaterialKey = create(HCIConstants.IMMERSIVE, "hop_graphite")

    //    Just Dire Things    //

    //    Mekanism    //

    // Alloy
    @JvmStatic
    val REFINED_GLOWSTONE: HTMaterialKey = create(HCIConstants.MEKANISM, "refined_glowstone")

    @JvmStatic
    val REFINED_OBSIDIAN: HTMaterialKey = create(HCIConstants.MEKANISM, "refined_obsidian")

    //    Oritech    //

    // Gem
    @JvmStatic
    val FLUXITE: HTMaterialKey = create(HCIConstants.ORITECH, "fluxite")

    // Alloy
    @JvmStatic
    val ENERGITE: HTMaterialKey = create(HCIConstants.ORITECH, "energite")

    @JvmStatic
    val ADAMANT: HTMaterialKey = create(HCIConstants.ORITECH, "adamant")

    @JvmStatic
    val DURATIUM: HTMaterialKey = create(HCIConstants.ORITECH, "duratium")

    @JvmStatic
    val PROMETHEUM: HTMaterialKey = create(HCIConstants.ORITECH, "prometheum")

    //    Replication    //

    @JvmStatic
    val REPLICA: HTMaterialKey = create(HCIConstants.REPLICATION, "replica")

    @JvmStatic
    private fun create(namespace: String, path: String): HTMaterialKey = HTMaterialKey(namespace, path)
}
