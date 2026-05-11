package hiiragi283.lib.transfer

import net.minecraft.core.Direction

@Deprecated("Use 'HTHandlerAccess' instead", ReplaceWith("HTHandlerAccess"), DeprecationLevel.ERROR)
typealias HTStorageAccess = HTHandlerAccess

/**
 * スロットへのアクセスの種類を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.AutomationType
 */
enum class HTHandlerAccess {
    /**
     * 外部からのアクセス
     */
    EXTERNAL,

    /**
     * 内部でのアクセス
     */
    INTERNAL,

    /**
     * GUIを介したアクセス
     */
    MANUAL,
    ;

    companion object {
        /**
         * 指定した[面][side]から[HTHandlerAccess]を返します。
         * @return [side]が`null`の場合は[INTERNAL]，それ以外は[EXTERNAL]
         */
        @JvmStatic
        fun forHandler(side: Direction?): HTHandlerAccess = when (side) {
            null -> INTERNAL
            else -> EXTERNAL
        }
    }
}
