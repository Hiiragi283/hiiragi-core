package hiiragi283.lib.material

/**
 * [HTMaterialKey]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTMaterialLike {
    /**
     * 保持している[素材キー][HTMaterialKey]を返します。
     */
    fun asMaterialKey(): HTMaterialKey
}
