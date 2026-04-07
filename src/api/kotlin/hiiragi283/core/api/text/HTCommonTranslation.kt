package hiiragi283.core.api.text

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.toDescriptionKey

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[翻訳][HTTranslation]を集めたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.text.APILang
 * @see mekanism.common.MekanismLang
 */
enum class HTCommonTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    ERROR(HTConst.CONSTANTS, "error"),
    INFINITE(HTConst.CONSTANTS, "infinite"),
    NONE(HTConst.CONSTANTS, "none"),
    EMPTY(HTConst.CONSTANTS, "empty"),

    TRUE(HTConst.CONSTANTS, "true"),
    FALSE(HTConst.CONSTANTS, "false"),

    DOWN("direction", "down"),
    UP("direction", "up"),
    NORTH("direction", "north"),
    SOUTH("direction", "south"),
    WEST("direction", "west"),
    EAST("direction", "east"),

    // Error
    EMPTY_TAG_KEY(HTConst.ERROR, "empty.tag_key"),
    INVALID_PACKET_S2C(HTConst.ERROR, "invalid_packet", "s2c"),
    INVALID_PACKET_C2S(HTConst.ERROR, "invalid_packet", "c2s"),

    MISSING_SERVER(HTConst.ERROR, "missing", "server"),
    MISSING_REGISTRY(HTConst.ERROR, "missing", "registry"),
    MISSING_KEY(HTConst.ERROR, "missing", "key"),

    // GUI
    CAPACITY(HTConst.GUI, "capacity"),
    CAPACITY_MB(HTConst.GUI, "capacity", "mb"),
    CAPACITY_FE(HTConst.GUI, "capacity", "fe"),

    STORED(HTConst.GUI, "stored"),
    STORED_MB(HTConst.GUI, "stored", "mb"),
    STORED_FE(HTConst.GUI, "stored", "fe"),
    STORED_EXP(HTConst.GUI, "stored", "exp"),

    FRACTION(HTConst.GUI, "fraction"),
    PERCENTAGE(HTConst.GUI, "percentage"),
    PROGRESS(HTConst.GUI, "progress"),

    TICK(HTConst.GUI, "tick"),
    SECONDS(HTConst.GUI, "seconds"),

    CHANCE_CONSUME(HTConst.GUI, "chance", "consume"),
    CHANCE_PRODUCE(HTConst.GUI, "chance", "produce"),

    RANGE_MIN(HTConst.GUI, "range", "min"),
    RANGE_MAX(HTConst.GUI, "range", "max"),
    RANGE_MIN_MAX(HTConst.GUI, "range", "min_max"),

    // Item Description
    TOOLTIP_INTRINSIC_ENCHANTMENT(HTConst.TOOLTIP, "intrinsic_enchantment"),
    TOOLTIP_SHOW_DESCRIPTION(HTConst.TOOLTIP, "show_description"),
    TOOLTIP_SHOW_DETAILS(HTConst.TOOLTIP, "show_details"),

    DATAPACK_WIP("datapack", "work_in_progress"),
    ;

    override val translationKey: String = HiiragiCoreAPI.id(path.joinToString(separator = ".")).toDescriptionKey(type)
}
