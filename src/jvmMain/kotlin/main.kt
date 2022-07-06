import application.Application
import mu.KotlinLogging
import org.apache.log4j.BasicConfigurator

val logger = KotlinLogging.logger {}

fun main() {
    BasicConfigurator.configure()
    val application = Application()
    application.start()
}