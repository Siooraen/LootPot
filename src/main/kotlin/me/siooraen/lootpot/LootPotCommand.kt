package me.siooraen.lootpot

import me.siooraen.lootpot.LootPot.conf
import me.siooraen.lootpot.LootPot.reset
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.module.chat.colored

/**
 * @author Siooraen
 * @since 2022/3/8 20:41
 */
@CommandHeader(name = "LootPot", aliases = ["lp"], permission = "*")
object LootPotCommand {

    @CommandBody
    val main = mainCommand { createHelper() }

    @CommandBody
    val reset = subCommand {
        execute<CommandSender> { sender, _, _ ->
            reset(true)
            sender.sendMessage("&c[LootPot] &7重置成功.".colored())
        }
    }

    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            LootPotExcel.read()
            conf.reload()
            sender.sendMessage("&c[LootPot] &7重载成功.".colored())
        }
    }
}