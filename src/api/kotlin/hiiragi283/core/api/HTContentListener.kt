package hiiragi283.core.api

/**
 * 変更をマークする処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.IContentsListener
 */
fun interface HTContentListener : Runnable {
    companion object {
        /**
         * 何も処理を行わない[HTContentListener]のインスタンス
         * @since 21.1.1.0
         */
        @JvmField
        val NOTHING = HTContentListener { }
    }

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
}
