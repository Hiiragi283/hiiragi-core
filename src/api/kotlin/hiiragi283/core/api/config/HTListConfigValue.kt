package hiiragi283.core.api.config

import net.neoforged.neoforge.common.ModConfigSpec
import java.util.function.Supplier

class HTListConfigValue<T : Any>(value: ModConfigSpec.ConfigValue<List<T>>) :
    HTConfigValue<List<T>>(value),
    Supplier<List<T>> {
    override fun get(): List<T> = value.get()
}
