package hiiragi283.core.api

/**
 * 変更をマークする処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.IContentsListener
 */
fun interface HTContentListener : Runnable {
    /**
     * 変更をマークします。
     */
    fun onContentsChanged()

    override fun run() {
        onContentsChanged()
    }

    /**
     * 変更をマークする時，何もしない処理を表すインターフェースです。
     */
    interface Empty : HTContentListener {
        override fun onContentsChanged() {}
    }
}
