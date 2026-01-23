package hiiragi283.core.api.serialization.value

import hiiragi283.core.api.serialization.codec.BiCodec

//    HTValueInput    //

fun <T : Any> HTValueInput.read(key: String, codec: BiCodec<*, T>): T? = this.read(key, codec.codec)

fun <T : Any> HTValueInput.list(key: String, codec: BiCodec<*, T>): Iterable<T>? = this.list(key, codec.codec)

fun <T : Any> HTValueInput.listOrEmpty(key: String, codec: BiCodec<*, T>): Iterable<T> = this.listOrEmpty(key, codec.codec)
