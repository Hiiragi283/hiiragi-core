package hiiragi283.lib.color

import hiiragi283.lib.collection.mutableEnumMapOf

/**
 * 色のバリエーションを持つ要素をまとめるクラスです。
 * @param T 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
@JvmRecord
data class HTColoredCollection<out T>(
    val white: T,
    val orange: T,
    val magenta: T,
    val lightBlue: T,
    val yellow: T,
    val lime: T,
    val pink: T,
    val gray: T,
    val lightGray: T,
    val cyan: T,
    val purple: T,
    val blue: T,
    val brown: T,
    val green: T,
    val red: T,
    val black: T,
) : Iterable<T> {
    companion object {
        @JvmField
        val VALUES: HTColoredCollection<HTDefaultColor> = HTColoredCollection(
            HTDefaultColor.WHITE,
            HTDefaultColor.ORANGE,
            HTDefaultColor.MAGENTA,
            HTDefaultColor.LIGHT_BLUE,
            HTDefaultColor.YELLOW,
            HTDefaultColor.LIME,
            HTDefaultColor.PINK,
            HTDefaultColor.GRAY,
            HTDefaultColor.LIGHT_GRAY,
            HTDefaultColor.CYAN,
            HTDefaultColor.PURPLE,
            HTDefaultColor.BLUE,
            HTDefaultColor.BROWN,
            HTDefaultColor.GREEN,
            HTDefaultColor.RED,
            HTDefaultColor.BLACK,
        )
    }

    operator fun get(color: HTDefaultColor): T = when (color) {
        HTDefaultColor.WHITE -> white
        HTDefaultColor.ORANGE -> orange
        HTDefaultColor.MAGENTA -> magenta
        HTDefaultColor.LIGHT_BLUE -> lightBlue
        HTDefaultColor.YELLOW -> yellow
        HTDefaultColor.LIME -> lime
        HTDefaultColor.PINK -> pink
        HTDefaultColor.GRAY -> gray
        HTDefaultColor.LIGHT_GRAY -> lightGray
        HTDefaultColor.CYAN -> cyan
        HTDefaultColor.PURPLE -> purple
        HTDefaultColor.BLUE -> blue
        HTDefaultColor.BROWN -> brown
        HTDefaultColor.GREEN -> green
        HTDefaultColor.RED -> red
        HTDefaultColor.BLACK -> black
    }

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var index: Int = 0

        override fun next(): T = HTDefaultColor.entries[index++].let(::get)

        override fun hasNext(): Boolean = index < HTDefaultColor.entries.size
    }

    inline fun <R> map(transform: (T) -> R): HTColoredCollection<R> = HTColoredCollection(
        transform(white),
        transform(orange),
        transform(magenta),
        transform(lightBlue),
        transform(yellow),
        transform(lime),
        transform(pink),
        transform(gray),
        transform(lightGray),
        transform(cyan),
        transform(purple),
        transform(blue),
        transform(brown),
        transform(green),
        transform(red),
        transform(black),
    )

    inline fun <U, R> zip(other: HTColoredCollection<U>, transform: (T, U) -> R): HTColoredCollection<R> = HTColoredCollection(
        transform(white, other.white),
        transform(orange, other.orange),
        transform(magenta, other.magenta),
        transform(lightBlue, other.lightBlue),
        transform(yellow, other.yellow),
        transform(lime, other.lime),
        transform(pink, other.pink),
        transform(gray, other.gray),
        transform(lightGray, other.lightGray),
        transform(cyan, other.cyan),
        transform(purple, other.purple),
        transform(blue, other.blue),
        transform(brown, other.brown),
        transform(green, other.green),
        transform(red, other.red),
        transform(black, other.black),
    )

    fun asMap(): Map<HTDefaultColor, T> = VALUES.associateWithTo(mutableEnumMapOf(), ::get)
}
