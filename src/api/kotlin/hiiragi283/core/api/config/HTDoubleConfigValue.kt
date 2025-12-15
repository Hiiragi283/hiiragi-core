package hiiragi283.core.api.config

import net.neoforged.neoforge.common.ModConfigSpec
import java.util.function.DoubleSupplier

/**
 * @see mekanism.common.config.value.CachedDoubleValue
 */
class HTDoubleConfigValue(value: ModConfigSpec.DoubleValue) :
    HTConfigValue<Double>(value),
    DoubleSupplier {
    fun getAsFloat(): Float = asDouble.toFloat()

    override fun getAsDouble(): Double = value.get()
}
