package me.ddivad.keeper.services

import me.jakejmattson.discordkt.api.annotations.Service
import me.ddivad.keeper.dataclasses.Configuration
import net.dv8tion.jda.api.entities.Member

enum class Permission {
    BOT_OWNER,
    GUILD_OWNER,
    USER,
    NONE
}

val DEFAULT_REQUIRED_PERMISSION = Permission.USER

@Service
class PermissionsService(private val configuration: Configuration) {
    fun hasClearance(member: Member, requiredPermissionLevel: Permission) = member.getPermissionLevel().ordinal <= requiredPermissionLevel.ordinal

    private fun Member.getPermissionLevel() =
            when {
                isBotOwner() -> Permission.BOT_OWNER
                isGuildOwner() -> Permission.GUILD_OWNER
                isUser() -> Permission.USER
                else -> Permission.NONE
            }

    private fun Member.isBotOwner() = user.idLong == configuration.botOwner
    private fun Member.isGuildOwner() = isOwner
    private fun Member.isUser() = configuration[guild.idLong]?.getLiveRole(guild.jda) in roles
}