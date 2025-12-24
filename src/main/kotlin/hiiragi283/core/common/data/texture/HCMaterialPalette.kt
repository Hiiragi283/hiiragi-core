package hiiragi283.core.common.data.texture

import hiiragi283.core.api.data.texture.HTArrayColorPalette
import hiiragi283.core.api.data.texture.HTColorPalette
import hiiragi283.core.api.data.texture.HTGradientColorPalette
import java.awt.Color

object HCMaterialPalette {
    //    Fuels    //

    @JvmStatic
    val COAL: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0x463e43),
            Color(0x3e373d),
            Color(0x2e262e),
            Color(0x241e26),
            Color(0x17131b),
            Color(0x0f0c13),
        ),
    )

    @JvmStatic
    val CHARCOAL: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0x605543),
            Color(0x4e4536),
            Color(0x423b2f),
            Color(0x2b261d),
            Color(0x1d1a14),
            Color(0x13110d),
        ),
    )

    @JvmStatic
    val COAL_COKE: HTColorPalette = HTGradientColorPalette(Color(0x5a6869), Color(0x1a1a1c))

    @JvmStatic
    val CARBIDE: HTColorPalette = HTGradientColorPalette(Color(0xd0ad90), Color(0x2c363a))

    //    Minerals    //

    @JvmStatic
    val REDSTONE: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xff0000),
            Color(0xaa0f01),
            Color(0x720000),
            Color(0x5c0700),
            Color(0x410500),
            Color(0x2d0000),
        ),
    )

    @JvmStatic
    val GLOWSTONE: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xf9d49c),
            Color(0xebaa4e),
            Color(0xb47140),
            Color(0x8e562e),
            Color(0x7e4821),
            Color(0x5a391c),
        ),
    )

    @JvmStatic
    val CINNABAR: HTColorPalette = HTGradientColorPalette(Color(0xcc3333), Color(0x330000))

    @JvmStatic
    val SALT: HTColorPalette = HTGradientColorPalette(Color(0xffffff), Color(0x999999))

    @JvmStatic
    val SALTPETER: HTColorPalette = HTGradientColorPalette(Color(0xffffff), Color(0x996666))

    @JvmStatic
    val SULFUR: HTColorPalette = HTGradientColorPalette(Color(0xcccc66), Color(0x663300))

    //    Gems    //

    @JvmStatic
    val LAPIS: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0x7497ea),
            Color(0x5a82e2),
            Color(0x345ec3),
            Color(0x1c53a8),
            Color(0x12408b),
            Color(0x052463),
        ),
    )

    @JvmStatic
    val QUARTZ: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xdcdbd4),
            Color(0xbab3aa),
            Color(0x97887f),
            Color(0x6f5d5a),
            Color(0x4a3c3d),
            Color(0x251e20),
        ),
    )

    @JvmStatic
    val AMETHYST: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xfecbe6),
            Color(0xcfa0f3),
            Color(0xb38ef3),
            Color(0x8d6acc),
            Color(0x6f4fab),
            Color(0x54398a),
        ),
    )

    @JvmStatic
    val DIAMOND: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xadffb9),
            Color(0x5cff96),
            Color(0x0aff96),
            Color(0x00b890),
            Color(0x006666),
            Color(0x002833),
        ),
    )

    @JvmStatic
    val EMERALD: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xe4ffb3),
            Color(0xa8ff66),
            Color(0x4bff1a),
            Color(0x00cc00),
            Color(0x00881d),
            Color(0x00441d),
        ),
    )

    @JvmStatic
    val ECHO: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0x29dfeb),
            Color(0x009295),
            Color(0x0a5060),
            Color(0x034150),
            Color(0x052a32),
            Color(0x111b21),
        ),
    )

    @JvmStatic
    val CRIMSON_CRYSTAL: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xeec6bb),
            Color(0xdd8277),
            Color(0xcc3333),
            Color(0x992633),
            Color(0x66192a),
            Color(0x330d19),
        ),
    )

    @JvmStatic
    val WARPED_CRYSTAL: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xbbeee3),
            Color(0x77ddd2),
            Color(0x33cccc),
            Color(0x268d99),
            Color(0x195666),
            Color(0x0d2733),
        ),
    )

    //    Pearls    //

    @JvmStatic
    val ENDER: HTColorPalette = HTGradientColorPalette(Color(0x009999), Color(0x003333))

    @JvmStatic
    val ELDRITCH: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xd7bbee),
            Color(0xa477dd),
            Color(0x6633cc),
            Color(0x402699),
            Color(0x231966),
            Color(0x0d0d33),
        ),
    )

    //    Metals    //

    @JvmStatic
    val COPPER: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xfc9982),
            Color(0xe77c56),
            Color(0xc15a36),
            Color(0x9c4e31),
            Color(0x8a4129),
            Color(0x6d3421),
        ),
    )

    @JvmStatic
    val IRON: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xd8d8d8),
            Color(0xb2b2b2),
            Color(0x8b8b8b),
            Color(0x656565),
            Color(0x3e3e3e),
            Color(0x181818),
        ),
    )

    @JvmStatic
    val GOLD: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xffffff),
            Color(0xfdf55f),
            Color(0xfad64a),
            Color(0xe9b115),
            Color(0xb26411),
            Color(0x752802),
        ),
    )

    @JvmStatic
    val SILVER: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xc6ece6),
            Color(0x8cd3d9),
            Color(0x53a5c6),
            Color(0x336699),
            Color(0x223566),
            Color(0x111333),
        ),
    )

    @JvmStatic
    val NIGHT_METAL: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0x4e4b54),
            Color(0x3c3947),
            Color(0x312c36),
            Color(0x27221c),
            Color(0x20131c),
            Color(0x160f10),
        ),
    )

    //    Alloys    //

    @JvmStatic
    val NETHERITE: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0x737173),
            Color(0x5a575a),
            Color(0x3b393b),
            Color(0x31292a),
            Color(0x271c1d),
            Color(0x111111),
        ),
    )

    @JvmStatic
    val STEEL: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0x787878),
            Color(0x656565),
            Color(0x525252),
            Color(0x3e3e3e),
            Color(0x2b2b2b),
            Color(0x181818),
        ),
    )

    @JvmStatic
    val AZURE_STEEL: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xccd3ee),
            Color(0x99a0dd),
            Color(0x6666cc),
            Color(0x4639ac),
            Color(0x372673),
            Color(0x1f1339),
        ),
    )

    @JvmStatic
    val DEEP_STEEL: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xccddd9),
            Color(0x99bbb7),
            Color(0x669999),
            Color(0x4d6f73),
            Color(0x33474c),
            Color(0x1a2226),
        ),
    )

    //    Plates    //

    @JvmStatic
    val PLASTIC: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xe0ebe5),
            Color(0xc2d6ce),
            Color(0xa3c2ba),
            Color(0x85ada8),
            Color(0x85ada8),
            Color(0x5e898c),
        ),
    )

    @JvmStatic
    val RUBBER: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0x463e43),
            Color(0x3e373d),
            Color(0x2e262e),
            Color(0x241e26),
            Color(0x17131b),
            Color(0x0f0c13),
        ),
    )

    //    Others    //

    @JvmStatic
    val WOOD: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xefc79b),
            Color(0xd2aa80),
            Color(0xb89063),
            Color(0x84623b),
            Color(0x6f502c),
            Color(0x523615),
        ),
    )

    @JvmStatic
    val STONE: HTColorPalette = HTGradientColorPalette(Color(0x999999), Color(0x333333))

    @JvmStatic
    val OBSIDIAN: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0x573571),
            Color(0x482e63),
            Color(0x3b2754),
            Color(0x291d3f),
            Color(0x19132a),
            Color(0x0b0a15),
        ),
    )
}
