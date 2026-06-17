package hiiragi283.lib.transfer

import net.minecraft.core.Direction

/**
 * [HTResourceSlot]へのアクセスの種類を表すクラスです。
 *
 * 参照 : [Mekanism - AutomationType](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/AutomationType.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
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
