package hiiragi283.core.api

/**
 * 変化が起きた時に呼び出されるインターフェース
 * @see mekanism.api.IContentsListener
 */
fun interface HTContentListener : Runnable {
    fun onContentsChanged()

    override fun run() {
        onContentsChanged()
    }

    interface Empty : HTContentListener {
        override fun onContentsChanged() {}
    }
}
