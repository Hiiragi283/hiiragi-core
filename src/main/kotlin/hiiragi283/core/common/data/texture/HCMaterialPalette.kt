package hiiragi283.core.common.data.texture

import hiiragi283.core.api.data.texture.HTColorPalette
import java.awt.Color

object HCMaterialPalette {
    @JvmStatic
    val TEMPLATE: Map<Color, Int> = mapOf(
        Color(0xd8d8d8) to 0,
        Color(0xb2b2b2) to 1,
        Color(0x8b8b8b) to 2,
        Color(0x656565) to 3,
        Color(0x3e3e3e) to 4,
        Color(0x181818) to 5,
    )

    //    Fuels    //

    @JvmStatic
    val COAL: HTColorPalette = HTColorPalette(
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
    val CHARCOAL: HTColorPalette = HTColorPalette(
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
    val COAL_COKE: HTColorPalette = HTColorPalette(
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
    val CARBIDE: HTColorPalette = HTColorPalette(
        arrayOf(
            Color(0x669999),
            Color(0x55797f),
            Color(0x445c66),
            Color(0x33424c),
            Color(0x222933),
            Color(0x111319),
        ),
    )

    //    Minerals    //

    @JvmStatic
    val REDSTONE: HTColorPalette = HTColorPalette(
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
    val GLOWSTONE: HTColorPalette = HTColorPalette(
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
    val CINNABAR: HTColorPalette = HTColorPalette(
        arrayOf(
            Color(0xf4dfc4),
            Color(0xe9ab8a),
            Color(0xdf634f),
            Color(0xc42430),
            Color(0x831837),
            Color(0x410c27),
        ),
    )

    @JvmStatic
    val SALTPETER: HTColorPalette = HTColorPalette(
        arrayOf(
            Color(0xe0dbd8),
            Color(0xc1b4b2),
            Color(0xa28b8d),
            Color(0x81676e),
            Color(0x5a4851),
            Color(0x2d242a),
        ),
    )

    @JvmStatic
    val SULFUR: HTColorPalette = HTColorPalette(
        arrayOf(
            Color(0xe5ebc9),
            Color(0xd8d794),
            Color(0xc4ad5e),
            Color(0x9f723a),
            Color(0x6a3e27),
            Color(0x351813),
        ),
    )

    //    Gems    //

    @JvmStatic
    val LAPIS: HTColorPalette = HTColorPalette(
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
    val QUARTZ: HTColorPalette = HTColorPalette(
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
    val AMETHYST: HTColorPalette = HTColorPalette(
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
    val DIAMOND: HTColorPalette = HTColorPalette(
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
    val EMERALD: HTColorPalette = HTColorPalette(
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
    val ECHO: HTColorPalette = HTColorPalette(
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
    val CRIMSON_CRYSTAL: HTColorPalette = HTColorPalette(
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
    val WARPED_CRYSTAL: HTColorPalette = HTColorPalette(
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
    val ENDER: HTColorPalette = HTColorPalette(
        arrayOf(
            Color(0xc0eebb),
            Color(0x77dd83),
            Color(0x33cc66),
            Color(0x269965),
            Color(0x196654),
            Color(0x0d3332),
        ),
    )

    @JvmStatic
    val ELDRITCH: HTColorPalette = HTColorPalette(
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
    val COPPER: HTColorPalette = HTColorPalette(
        arrayOf(
            Color(0xffffff),
            Color(0xfbc3b6),
            Color(0xe77c56),
            Color(0xc15a36),
            Color(0x9c4529),
            Color(0x6d3421),
        ),
    )

    @JvmStatic
    val IRON: HTColorPalette = HTColorPalette(
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
    val GOLD: HTColorPalette = HTColorPalette(
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
    val SILVER: HTColorPalette = HTColorPalette(
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
    val NIGHT_METAL: HTColorPalette = HTColorPalette(
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
    val NETHERITE: HTColorPalette = HTColorPalette(
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
    val STEEL: HTColorPalette = HTColorPalette(
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
    val AZURE_STEEL: HTColorPalette = HTColorPalette(
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
    val DEEP_STEEL: HTColorPalette = HTColorPalette(
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
    val PLASTIC: HTColorPalette = HTColorPalette(
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
    val RUBBER: HTColorPalette = HTColorPalette(
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
    val WOOD: HTColorPalette = HTColorPalette(
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
    val STONE: HTColorPalette = HTColorPalette(
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
    val OBSIDIAN: HTColorPalette = HTColorPalette(
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
