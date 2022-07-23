package me.siooraen.lootpot

import ink.ptms.zaphkiel.ZaphkielAPI.getItemStack
import me.siooraen.lootpot.LootPot.display
import me.siooraen.lootpot.LootPot.drops
import me.siooraen.lootpot.LootPot.getWeightRandom
import me.siooraen.lootpot.LootPot.pitch
import me.siooraen.lootpot.LootPot.pots
import me.siooraen.lootpot.LootPot.sound
import me.siooraen.lootpot.LootPot.volume
import me.siooraen.lootpot.LootPot.xOffset
import me.siooraen.lootpot.LootPot.yOffset
import me.siooraen.lootpot.LootPot.zOffset
import me.siooraen.lootpot.data.PotCacheData
import org.bukkit.FluidCollisionMode
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerAnimationEvent
import org.bukkit.event.player.PlayerAnimationType
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.util.random

/**
 * @author Siooraen
 * @since 2022/3/8 20:36
 */
object LootPotEvent {

    @SubscribeEvent
    fun e(e: PlayerAnimationEvent) {
        if (e.animationType != PlayerAnimationType.ARM_SWING) {
            return
        }

        val player = e.player
        if (player.gameMode != GameMode.ADVENTURE) {
            return
        }

        val block = player.getTargetBlockExact(10, FluidCollisionMode.NEVER) ?: return
        if (block.state.type != Material.FLOWER_POT && !block.state.type.name.startsWith("POTTED")) {
            return
        }

        e.isCancelled = true

        val pot = PotCacheData(block.location, block.type, System.currentTimeMillis())
        pots.add(pot)

        block.display()
        val blockData = Material.AIR.createBlockData()
        player.sendBlockChange(block.location, blockData)

        // 假设这里是随机获取
        val drop = getWeightRandom(drops)!!
        val item = getItemStack(drop.id)!!

        if (item.type == Material.AIR) {
            return
        }

        val xRandom = random(-xOffset, xOffset)
        val yRandom = random(0.0, yOffset)
        val zRandom = random(-zOffset, zOffset)
        val location = block.location.add(xRandom, yRandom, zRandom)
        block.world.dropItemNaturally(location, item)

        player.playSound(player.location, Sound.valueOf(sound), volume, pitch)
    }
}