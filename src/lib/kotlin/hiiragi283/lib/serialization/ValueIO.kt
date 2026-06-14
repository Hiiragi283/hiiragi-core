package hiiragi283.lib.serialization

import com.mojang.serialization.Codec
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.kotlin
import net.minecraft.world.level.storage.ValueInput

/**
 * [ValueInput.read]を[Option]に変換して返します。
 * @param T 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any> ValueInput.readOption(name: String, codec: Codec<T>): Option<T> = this.read(name, codec).kotlin
