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

    /**
     * @suppress
     */
    override fun run() {
        onContentsChanged()
    }

    /**
     * 何も変更をマークしないことを表すインターフェースです。
     */
    interface Empty : HTContentListener {
        override fun onContentsChanged() {}
    }
}
