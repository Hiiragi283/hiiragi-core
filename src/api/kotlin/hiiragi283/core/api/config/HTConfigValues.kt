package hiiragi283.core.api.config

import hiiragi283.core.api.text.HTHasTranslationKey
import net.neoforged.neoforge.common.ModConfigSpec

fun ModConfigSpec.Builder.translation(hasKey: HTHasTranslationKey): ModConfigSpec.Builder = this.translation(hasKey.translationKey)

fun ModConfigSpec.Builder.definePositiveInt(path: String, defaultValue: Int, min: Int = 1): ModConfigSpec.IntValue =
    defineInRange(path, defaultValue, min, Int.MAX_VALUE)

fun ModConfigSpec.Builder.definePositiveDouble(
    path: String,
    defaultValue: Double,
    min: Number,
    max: Number,
): ModConfigSpec.DoubleValue = defineInRange(path, defaultValue, min.toDouble(), max.toDouble())
