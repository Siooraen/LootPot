package me.siooraen.lootpot

import me.siooraen.lootpot.data.DropData
import me.siooraen.lootpot.data.PotCacheData
import me.siooraen.lootpot.data.WeightCategory
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Plugin
import taboolib.common.platform.Schedule
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.createLocal
import taboolib.platform.util.onlinePlayers
import java.lang.System.currentTimeMillis
import java.util.*

/**
 * @author Siooraen
 * @since 2022/3/8 20:28
 */
object LootPot : Plugin() {

    @Config(migrate = true)
    lateinit var conf: Configuration
        private set

    @ConfigNode("update-delay")
    var updateDelay: Long = 300L
        private set

    @ConfigNode("drop-offset.x")
    var xOffset: Double = 0.0
        private set

    @ConfigNode("drop-offset.y")
    var yOffset: Double = 0.0
        private set

    @ConfigNode("drop-offset.z")
    var zOffset: Double = 0.0
        private set

    @ConfigNode("sound.id")
    var sound: String = "ENTITY_EXPERIENCE_ORB_PICKUP"
        private set

    @ConfigNode("sound.volume")
    var volume: Float = 1f
        private set

    @ConfigNode("sound.pitch")
    var pitch: Float = 2f
        private set

    val data by lazy { createLocal("config/pot/data.yml") }
    val pots = ArrayList<PotCacheData>()
    val drops = ArrayList<WeightCategory<DropData>>()

    @Awake(LifeCycle.ACTIVE)
    fun import() {
        pots.clear()
        data.getKeys(false).forEach {
            pots.add(
                PotCacheData(
                    toLocation(it),
                    Material.valueOf(data.getString("$it.type")!!),
                    data.getLong("$it.time")
                )
            )
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun export() {
        data.getKeys(false).forEach { data[it] = null }
        pots.forEach { pot ->
            val location = fromLocation(pot.location)
            data["$location.type"] = pot.type.name
            data["$location.time"] = pot.time
        }
    }

    @Schedule(period = 40)
    fun e() {
        reset(false)
    }

    fun reset(force: Boolean) {
        pots.forEach { pot ->
            if (force || currentTimeMillis() - pot.time >= updateDelay * 1000) {
                val blockData = pot.type.createBlockData()
                onlinePlayers.forEach { player ->
                    player.sendBlockChange(pot.location, blockData)
                }
                pot.location.block.display()
                pots.remove(pot)
            }
        }
        export()
    }

    fun Block.display() {
        world.playEffect(location, Effect.STEP_SOUND, type)
    }

    fun toLocation(source: String): Location {
        return source.replace("__", ".").split(",").run {
            Location(
                Bukkit.getWorld(get(0)),
                getOrElse(1) { "0" }.toDouble(),
                getOrElse(2) { "0" }.toDouble(),
                getOrElse(3) { "0" }.toDouble()
            )
        }
    }

    fun fromLocation(location: Location): String {
        return "${location.world?.name},${location.x},${location.y},${location.z}".replace(".", "__")
    }

    fun <T> getWeightRandom(categories: List<WeightCategory<T>>): T? {
        var weightSum = 0
        categories.forEach { wc ->
            weightSum += wc.weight
        }
        if (weightSum <= 0) {
            return null
        }
        val n = Random().nextInt(weightSum)
        var m = 0
        categories.forEach { wc ->
            if (m <= n && n < m + wc.weight) {
                return wc.category
            }
            m += wc.weight
        }
        return null
    }
}