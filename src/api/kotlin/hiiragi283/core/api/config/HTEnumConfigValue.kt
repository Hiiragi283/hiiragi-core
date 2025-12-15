package hiiragi283.core.api.config

import net.neoforged.neoforge.common.ModConfigSpec
import java.util.function.Supplier

/**
 * @see mekanism.common.config.value.CachedBooleanValue
 */
class HTEnumConfigValue<T : Enum<T>>(value: ModConfigSpec.EnumValue<T>) :
    HTConfigValue<T>(value),
    Supplier<T> {
    override fun get(): T = value.get()
}
