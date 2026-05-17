package hiiragi283.lib.material

@JvmInline
value class HTMaterialPartKey(val name: String) : Comparable<HTMaterialPartKey> {
    override fun compareTo(other: HTMaterialPartKey): Int = this.name.compareTo(other.name)
}
