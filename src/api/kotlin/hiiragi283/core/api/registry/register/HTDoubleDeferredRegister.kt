package hiiragi283.core.api.registry.register

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTDoubleDeferredHolder
import hiiragi283.core.api.registry.IdToFunction
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import java.util.function.Supplier

/**
 * 二つの値を同時に登録する[HTDeferredRegister]の拡張クラスです。
 * @param FIRST 一番目のレジストリの要素のクラス
 * @param SECOND 二番目のレジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.registration.DoubleDeferredRegister
 */
open class HTDoubleDeferredRegister<FIRST : Any, SECOND : Any> protected constructor(
    protected open val firstRegister: HTDeferredRegister<FIRST>,
    protected open val secondRegister: HTDeferredRegister<SECOND>,
) {
    /**
     * 二つの値をそれぞれ独立して登録します。
     * @param F [FIRST]を継承した一番目の値のクラス
     * @param S [SECOND]を継承した一番目の値のクラス
     * @param H [HTDoubleDeferredHolder]を継承したクラス
     * @param name 登録する値のIDのパス
     * @param first 一番目の値を渡すブロック
     * @param second 一番目の値を渡すブロック
     * @param combiner 一番目の値と二番目の値を[H]に変換するブロック
     */
    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerEach(
        name: String,
        first: Supplier<out F>,
        second: Supplier<out S>,
        combiner: HolderCombiner<FIRST, SECOND, F, S, H>,
    ): H = combiner.combine(firstRegister.register(name, first), secondRegister.register(name, second))

    /**
     * 二つの値をそれぞれ独立して登録します。
     * @param F [FIRST]を継承した一番目の値のクラス
     * @param S [SECOND]を継承した一番目の値のクラス
     * @param H [HTDoubleDeferredHolder]を継承したクラス
     * @param name 登録する値のIDのパス
     * @param first 一番目の値を渡すブロック
     * @param second 一番目の値を渡すブロック
     * @param combiner 一番目の値と二番目の値を[H]に変換するブロック
     */
    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerEach(
        name: String,
        first: IdToFunction<F>,
        second: IdToFunction<S>,
        combiner: HolderCombiner<FIRST, SECOND, F, S, H>,
    ): H = combiner.combine(firstRegister.register(name, first), secondRegister.register(name, second))

    /**
     * 一番目の値に基づいて，二番目の値を登録します。
     * @param F [FIRST]を継承した一番目の値のクラス
     * @param S [SECOND]を継承した一番目の値のクラス
     * @param H [HTDoubleDeferredHolder]を継承したクラス
     * @param name 登録する値のIDのパス
     * @param first 一番目の値を渡すブロック
     * @param second [F]から[S]に変換するブロック
     * @param combiner 一番目の値と二番目の値を[H]に変換するブロック
     */
    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> register(
        name: String,
        first: IdToFunction<F>,
        second: (F) -> S,
        combiner: HolderCombiner<FIRST, SECOND, F, S, H>,
    ): H = registerAdvanced(name, first, HTDeferredHolder<FIRST, F>::get.andThen(second), combiner)

    /**
     * 一番目の値に基づいて，二番目の値を登録します。
     * @param F [FIRST]を継承した一番目の値のクラス
     * @param S [SECOND]を継承した一番目の値のクラス
     * @param H [HTDoubleDeferredHolder]を継承したクラス
     * @param name 登録する値のIDのパス
     * @param first 一番目の値を渡すブロック
     * @param second [F]の[HTDeferredHolder]から[S]に変換するブロック
     * @param combiner 一番目の値と二番目の値を[H]に変換するブロック
     */
    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerAdvanced(
        name: String,
        first: IdToFunction<F>,
        second: (HTDeferredHolder<FIRST, F>) -> S,
        combiner: HolderCombiner<FIRST, SECOND, F, S, H>,
    ): H {
        val firstHolder: HTDeferredHolder<FIRST, F> = firstRegister.register(name, first)
        return combiner.combine(firstHolder, secondRegister.register(name) { _ -> second(firstHolder) })
    }

    fun register(bus: IEventBus) {
        firstRegister.register(bus)
        secondRegister.register(bus)
    }

    /**
     * IDのエイリアスを登録します。
     * @param from 変更前のIDの[パス][ResourceLocation.getPath]
     * @param to 変更後のIDの[パス][ResourceLocation.getPath]
     */
    fun addAlias(from: String, to: String) {
        firstRegister.addAlias(from, to)
        secondRegister.addAlias(from, to)
    }

    /**
     * 二つの[HTDeferredHolder]を[HTDoubleDeferredHolder]に変換する処理を表すインターフェースです。
     * @param FIRST 一番目のレジストリの要素のクラス
     * @param F [FIRST]を継承した一番目の値のクラス
     * @param SECOND 二番目のレジストリの要素のクラス
     * @param S [SECOND]を継承した二番目の値のクラス
     * @param H [HTDoubleDeferredHolder]を継承したクラス
     */
    fun interface HolderCombiner<FIRST : Any, SECOND : Any, F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> {
        /**
         * 指定した[first]と[second]を変換します。
         */
        fun combine(first: HTDeferredHolder<FIRST, F>, second: HTDeferredHolder<SECOND, S>): H
    }
}
