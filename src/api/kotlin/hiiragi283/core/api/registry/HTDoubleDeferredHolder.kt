package hiiragi283.core.api.registry

/**
 * 二つの値を保持する[HTDeferredHolder]の拡張クラスです。
 * @param R_FIRST 一番目のレジストリの要素のクラス
 * @param FIRST [R_FIRST]を継承した一番目の値のクラス
 * @param R_SECOND 二番目のレジストリの要素のクラス
 * @param SECOND [R_SECOND]を継承した二番目の値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.registration.DoubleWrappedRegistryObject
 */
open class HTDoubleDeferredHolder<R_FIRST : Any, FIRST : R_FIRST, R_SECOND : Any, SECOND : R_SECOND>(
    first: HTDeferredHolder<R_FIRST, FIRST>,
    protected val second: HTDeferredHolder<R_SECOND, SECOND>,
) : HTDeferredHolder<R_FIRST, FIRST>(first.key) {
    /**
     * 二番目の値を返します。
     */
    fun getSecond(): SECOND = second.get()
}
