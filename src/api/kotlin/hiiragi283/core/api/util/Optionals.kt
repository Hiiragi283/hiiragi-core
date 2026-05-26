package hiiragi283.core.api.util

import java.util.Optional

/**
 * この[インスタンス][this]を[Optional]で包みます。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <T : Any> T?.toOptional(): Optional<T> = Optional.ofNullable(this)
