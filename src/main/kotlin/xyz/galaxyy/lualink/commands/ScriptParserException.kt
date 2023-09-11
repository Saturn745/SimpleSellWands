package xyz.galaxyy.lualink.commands

import cloud.commandframework.captions.Caption
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.ParserException

class ScriptParserException(input: String, commandContext: CommandContext<*>) : ParserException(LoadedScriptParser::class.java, commandContext, Caption.of("Script already loaded or was not found."))