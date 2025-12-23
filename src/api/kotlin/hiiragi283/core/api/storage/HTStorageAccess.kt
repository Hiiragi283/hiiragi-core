package hiiragi283.core.api.storage

import net.minecraft.core.Direction
import java.util.function.Predicate

/**
 * スロットへのアクセスの種類を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.AutomationType
 */
enum class HTStorageAccess {
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
         * 内部でのアクセスのみを通すフィルタ
         */
        @JvmField
        val INTERNAL_ONLY: Predicate<HTStorageAccess> = Predicate { it == INTERNAL }

        /**
         * GUIを介したアクセスのみを通すフィルタ
         */
        @JvmField
        val MANUAL_ONLY: Predicate<HTStorageAccess> = Predicate { it == MANUAL }

        /**
         * 外部からのアクセス以外を通すフィルタ
         */
        @JvmField
        val NOT_EXTERNAL: Predicate<HTStorageAccess> = Predicate { it != EXTERNAL }

        /**
         * 指定した[面][side]から[HTStorageAccess]を返します。
         * @return [side]が`null`の場合は[INTERNAL]，それ以外は[EXTERNAL]
         */
        @JvmStatic
        fun forHandler(side: Direction?): HTStorageAccess = when (side) {
            null -> INTERNAL
            else -> EXTERNAL
        }
    }
}
