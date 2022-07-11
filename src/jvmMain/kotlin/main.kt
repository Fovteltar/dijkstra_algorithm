import application.Application
import logic.GraphFileReader
import mu.KotlinLogging
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.ConsoleAppender
import org.apache.log4j.PatternLayout

val logger = KotlinLogging.logger {}

fun main() {
    BasicConfigurator.configure(ConsoleAppender(PatternLayout("[%p] [%d{HH:mm:ss}] %n [%l] %n %m %n%n")))
    val application = Application()
    application.start()
}