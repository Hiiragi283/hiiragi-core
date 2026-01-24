package hiiragi283.core.api.serialization.value

import hiiragi283.core.api.serialization.codec.BiCodec
import java.util.Optional

//    HTValueInput    //

fun <T : Any> HTValueInput.read(key: String, codec: BiCodec<*, T>): T? = this.read(key, codec.codec)

fun <T : Any> HTValueInput.list(key: String, codec: BiCodec<*, T>): Iterable<T>? = this.list(key, codec.codec)

fun <T : Any> HTValueInput.listOrEmpty(key: String, codec: BiCodec<*, T>): Iterable<T> = this.listOrEmpty(key, codec.codec)

//    HTValueOutput    //

fun <T : Any> HTValueOutput.write(key: String, codec: BiCodec<*, T>, value: T?) {
    this.write(key, codec.codec, value)
}

fun <T : Any> HTValueOutput.writeOptional(key: String, codec: BiCodec<*, Optional<T>>, value: T?) {
    this.writeOptional(key, codec.codec, value)
}

fun <T : Any> HTValueOutput.list(key: String, codec: BiCodec<*, T>): HTValueOutput.TypedOutputList<T> = this.list(key, codec.codec)
