package me.siooraen.lootpot

import jxl.Workbook
import me.siooraen.lootpot.LootPot.drops
import me.siooraen.lootpot.data.DropData
import me.siooraen.lootpot.data.WeightCategory
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import java.io.File

/**
 * @author Siooraen
 * @since 2022/7/23 19:54
 */
object LootPotExcel {

    @Awake(LifeCycle.LOAD)
    fun read() {
        drops.clear()
        val file = File(getDataFolder(), "config/pot/drops.xls")
        Workbook.getWorkbook(file).sheets.forEach { sheet ->
            (1 until sheet.rows).forEach {
                val row = sheet.getRow(it)
                val id = row[0].contents
                val weight = row[1].contents
                if (id.isNotEmpty() && weight.isNotEmpty()) {
                    val dropData = DropData(id)
                    val drop = WeightCategory(dropData, weight.toInt())
                    drops.add(drop)
                }
            }
        }
    }
}