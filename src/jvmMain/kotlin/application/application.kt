package application

import application.controller.Tools
import application.logic.Logic
import application.ui.UI

class Application(
    private val logic: Logic = Logic(),
    private val tools: Tools = Tools(logic),
    private val ui: UI = UI(tools)
) {
    init {
        tools.ui = ui
    }
    fun start() {
        ui.draw()
    }
}