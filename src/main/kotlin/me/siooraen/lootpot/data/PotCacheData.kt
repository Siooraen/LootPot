package me.siooraen.lootpot.data

import org.bukkit.Location
import org.bukkit.Material


/**
 * @author Siooraen
 * @since 2022/7/19 19:37
 */
class PotCacheData(
    val location: Location,
    val type: Material,
    val time: Long,
)