fun main() {
    abstract class Packet(val version: Int, val type: Int) {
        open fun getVersionSum() = version
        abstract fun getValue(): Long
    }

    class Literal(version: Int, type: Int, private val value: Long) : Packet(version, type) {
        override fun getValue() = value
    }

    class Operator(version: Int, type: Int, private val children: List<Packet>) : Packet(version, type) {
        override fun getVersionSum() = super.getVersionSum() + children.sumOf { it.getVersionSum() }
        override fun getValue() = when (type) {
            0 -> children.sumOf { it.getValue() }
            1 -> children.fold(1L) { agg, it -> agg * it.getValue() }
            2 -> children.minOf { it.getValue() }
            3 -> children.maxOf { it.getValue() }
            5 -> if (children[0].getValue() > children[1].getValue()) 1 else 0
            6 -> if (children[0].getValue() < children[1].getValue()) 1 else 0
            7 -> if (children[0].getValue() == children[1].getValue()) 1 else 0
            else -> 0L
        }
    }

    fun parseInput(s: String): Packet {
        fun Iterator<Char>.takeAsInt(n: Int) = buildString { repeat(n) { append(this@takeAsInt.next()) } }.toInt(2)

        fun Iterator<Char>.take(n: Int) = iterator { repeat(n) { yield(this@take.next()) } }

        fun parseLiteralValue(data: Iterator<Char>) = buildString {
                do {
                    val lastChunk = data.next() == '0'
                    append(data.take(4).asSequence().joinToString(""))
                } while (!lastChunk)
            }.toLong(2)

        fun parsePacket(data: Iterator<Char>): Packet {
            val version = data.takeAsInt(3)
            val type = data.takeAsInt(3)
            return if (type == 4) {
                Literal(version, type, parseLiteralValue(data))
            } else {
                val subPackets = arrayListOf<Packet>()
                when (data.next()) {
                    '0' -> {
                        val subIt = iterator { repeat(data.takeAsInt(15)) { yield(data.next()) } }
                        while (subIt.hasNext()) {
                            subPackets.add(parsePacket(subIt))
                        }
                    }
                    '1' -> repeat(data.takeAsInt(11)) { subPackets.add(parsePacket(data)) }
                }
                Operator(version, type, subPackets)
            }
        }

        val binaryIt = iterator {
            s.forEach {
                yieldAll(it.digitToInt(16).toString(2).padStart(4, '0').toCharArray().asSequence())
            }
        }
        return parsePacket(binaryIt)
    }

    fun part1(s: String) = parseInput(s).getVersionSum()

    fun part2(s: String) = parseInput(s).getValue()

    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)

    check(part2("C200B40A82") == 3L)
    check(part2("04005AC33890") == 54L)
    check(part2("880086C3E88112") == 7L)
    check(part2("CE00C43D881120") == 9L)
    check(part2("D8005AC2A8F0") == 1L)
    check(part2("F600BC2D8F") == 0L)
    check(part2("9C005AC2F8F0") == 0L)
    check(part2("9C0141080250320F1802104A08") == 1L)

    val input =
        "005410C99A9802DA00B43887138F72F4F652CC0159FE05E802B3A572DBBE5AA5F56F6B6A4600FCCAACEA9CE0E1002013A55389B064C0269813952F983595234002DA394615002A47E06C0125CF7B74FE00E6FC470D4C0129260B005E73FCDFC3A5B77BF2FB4E0009C27ECEF293824CC76902B3004F8017A999EC22770412BE2A1004E3DCDFA146D00020670B9C0129A8D79BB7E88926BA401BAD004892BBDEF20D253BE70C53CA5399AB648EBBAAF0BD402B95349201938264C7699C5A0592AF8001E3C09972A949AD4AE2CB3230AC37FC919801F2A7A402978002150E60BC6700043A23C618E20008644782F10C80262F005679A679BE733C3F3005BC01496F60865B39AF8A2478A04017DCBEAB32FA0055E6286D31430300AE7C7E79AE55324CA679F9002239992BC689A8D6FE084012AE73BDFE39EBF186738B33BD9FA91B14CB7785EC01CE4DCE1AE2DCFD7D23098A98411973E30052C012978F7DD089689ACD4A7A80CCEFEB9EC56880485951DB00400010D8A30CA1500021B0D625450700227A30A774B2600ACD56F981E580272AA3319ACC04C015C00AFA4616C63D4DFF289319A9DC401008650927B2232F70784AE0124D65A25FD3A34CC61A6449246986E300425AF873A00CD4401C8A90D60E8803D08A0DC673005E692B000DA85B268E4021D4E41C6802E49AB57D1ED1166AD5F47B4433005F401496867C2B3E7112C0050C20043A17C208B240087425871180C01985D07A22980273247801988803B08A2DC191006A2141289640133E80212C3D2C3F377B09900A53E00900021109623425100723DC6884D3B7CFE1D2C6036D180D053002880BC530025C00F700308096110021C00C001E44C00F001955805A62013D0400B400ED500307400949C00F92972B6BC3F47A96D21C5730047003770004323E44F8B80008441C8F51366F38F240"
    println(part1(input))
    println(part2(input))
}
