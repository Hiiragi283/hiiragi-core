package hiiragi283.lib.serialization

import com.mojang.serialization.Codec
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.kotlin
import net.minecraft.world.level.storage.ValueInput

fun <T : Any> ValueInput.readOption(name: String, codec: Codec<T>): Option<T> = this.read(name, codec).kotlin
