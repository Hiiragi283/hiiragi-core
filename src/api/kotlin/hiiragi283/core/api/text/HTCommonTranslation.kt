package hiiragi283.core.api.text

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.Util

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[翻訳][HTTranslation]を集めたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.text.APILang
 * @see mekanism.common.MekanismLang
 */
enum class HTCommonTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    ERROR("constants", "error"),
    INFINITE("constants", "infinite"),
    NONE("constants", "none"),
    EMPTY("constants", "empty"),

    TRUE("constants", "true"),
    FALSE("constants", "false"),

    // Block Type
    EMPTY_ENTRY("description", "empty"),

    // Error
    EMPTY_TAG_KEY("error", "empty.tag_key"),
    INVALID_PACKET_S2C("error", "invalid_packet", "s2c"),
    INVALID_PACKET_C2S("error", "invalid_packet", "c2s"),

    MISSING_SERVER("error", "missing", "server"),
    MISSING_REGISTRY("error", "missing", "registry"),
    MISSING_KEY("error", "missing", "key"),

    // GUI
    CAPACITY("gui", "capacity"),
    CAPACITY_MB("gui", "capacity", "mb"),
    CAPACITY_FE("gui", "capacity", "fe"),

    STORED("gui", "stored"),
    STORED_MB("gui", "stored", "mb"),
    STORED_FE("gui", "stored", "fe"),
    STORED_EXP("gui", "stored", "exp"),

    FRACTION("gui", "fraction"),
    TICK("gui", "tick"),
    SECONDS("gui", "seconds"),

    // Item Description
    TOOLTIP_BLOCK_POS("tooltip", "block_pos"),
    TOOLTIP_CHARGE_POWER("tooltip", "blast_power"),
    TOOLTIP_DIMENSION("tooltip", "dimension"),
    TOOLTIP_INTRINSIC_ENCHANTMENT("tooltip", "intrinsic_enchantment"),
    TOOLTIP_LOOT_TABLE_ID("tooltip", "loot_table_id"),
    TOOLTIP_UPGRADE_TARGET("tooltip", "upgrade", "target"),
    TOOLTIP_UPGRADE_EXCLUSIVE("tooltip", "upgrade", "exclusive"),

    TOOLTIP_SHOW_DESCRIPTION("tooltip", "show_description"),
    TOOLTIP_SHOW_DETAILS("tooltip", "show_details"),

    DATAPACK_WIP("datapack", "work_in_progress"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, HiiragiCoreAPI.id(path.joinToString(separator = ".")))
}
