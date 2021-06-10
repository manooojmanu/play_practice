import com.google.inject.AbstractModule
import com.manoj.actors.{ DivisionActor, SimpleActor }
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.concurrent.AkkaGuiceSupport
import services.{ ApplicationTimer, AtomicCounter, Counter }
import java.time.Clock

import akka.routing.RoundRobinPool

import scala.swing.event.Key.Props

/** This class is a Guice module that tells Guice how to bind several
  * different types. This Guice module is created when the Play
  * application starts.
  *
  * Play will automatically use any class called `Module` that is in
  * the root package. You can create modules in other locations by
  * adding `play.modules.enabled` settings to the `application.conf`
  * configuration file.
  */
class Module extends AbstractModule with AkkaGuiceSupport with LazyLogging {

  def javaDetails(): Unit = {

    logger.info(s"##################### Java Version Details #####################")
    logger.info(s"Java Version = ${System.getProperty("java.version")}")
    logger.info(s"Java Version Date = ${System.getProperty("java.version.date")}")
    logger.info(s"Java Vendor = ${System.getProperty("java.vendor")}")
    logger.info(s"Java Vendor Version = ${System.getProperty("java.vendor.version")}")
    logger.info(s"Java Vendor URL = ${System.getProperty("java.vendor.url")}")
    logger.info(s"Java Vendor URL Bug = ${System.getProperty("java.vendor.url.bug")}")

    logger.info(s"Java Specification Name = ${System.getProperty("java.specification.name")}")
    logger.info(s"Java Specification Vendor = ${System.getProperty("java.specification.vendor")}")
    logger.info(s"Java Specification Version = ${System.getProperty("java.specification.version")}")

    logger.info(s"Java VM Name = ${System.getProperty("java.vm.name")}")
    logger.info(s"Java VM Vendor = ${System.getProperty("java.vm.vendor")}")
    logger.info(s"Java VM Version = ${System.getProperty("java.vm.version")}")
    logger.info(s"Java VM Info = ${System.getProperty("java.vm.info")}")
    logger.info(s"Java VM Specification Name = ${System.getProperty("java.vm.specification.name")}")
    logger.info(s"Java VM Specification Vendor = ${System.getProperty("java.vm.specification.vendor")}")
    logger.info(s"Java VM Specification Version = ${System.getProperty("java.vm.specification.version")}")

    logger.info(s"Java Runtime Name = ${System.getProperty("java.runtime.name")}")

    logger.info(s"Java Runtime Version = ${System.getProperty("java.runtime.version")}")

    logger.info(s"Java Class Version = ${System.getProperty("java.class.version")}")

    logger.info(s"JDK Debug = ${System.getProperty("jdk.debug")}")

    logger.info(s"Sun Java Launcher = ${System.getProperty("sun.java.launcher")}")

    logger.info(s"Sun Management Compiler = ${System.getProperty("sun.management.compiler")}")

    val mb = 1024 * 1024
    // Getting the runtime reference from system
    val runtime = Runtime.getRuntime
    logger.info(s"##################### Heap utilization statistics [MB] #####################")

    // Print used memory
    logger.info(s"Used Memory: ${(runtime.totalMemory() - runtime.freeMemory()) / mb}")

    // Print free memory
    logger.info(s"Free Memory: ${runtime.freeMemory() / mb}")

    // Print total available memory
    logger.info(s"Total Memory: ${runtime.totalMemory() / mb}")

    // Print Maximum available memory
    logger.info(s"Max Memory: ${runtime.maxMemory() / mb}")

  }

  override def configure() = {
    javaDetails()
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind(classOf[ApplicationTimer]).asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])

    //Actors
    bindActor[SimpleActor]("simple_actor", _.withRouter(RoundRobinPool(10)))
    bindActor[DivisionActor]("division_actor", _.withRouter(RoundRobinPool(5)))

  }

}
