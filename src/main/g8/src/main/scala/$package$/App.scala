package $package$

import cats.data.{Kleisli, OptionT}
import cats.effect.{IO, Resource}
import cats.syntax.all.*
import com.comcast.ip4s.*
import com.typesafe.config.Config
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import zefir.entity.*
import zefir.entity.pekko.PekkoRuntime
import zefir.pekko.postgres.r2dbc.PostgresBase
import $package$.api.ApiRoutes
import $package$.codecs.$entity;format="Camel"$Codecs
import $package$.domain.*

import scala.concurrent.duration.*

/** Application configuration. */
final case class AppConfig(
    apiHost: Host,
    apiPort: Port
)

object AppConfig:
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  def fromTypesafeConfig(config: Config): AppConfig =
    val apiConfig = config.getConfig("$name;format="norm"$.api")
    AppConfig(
      apiHost = Host.fromString(apiConfig.getString("host")).get,
      apiPort = Port.fromInt(apiConfig.getInt("port")).get
    )

/** Main application context - what's available after full initialization. */
final case class AppContext(
    apiServer: Server
)

/** Application wiring - extends Zefir's BaseApp pattern.
  *
  * This demonstrates the complete Zefir application structure:
  *   - Base: PostgresBase with connection pool, health checks, and metrics
  *   - Manifests: $entity;format="Camel"$ behaviour and codecs
  *   - Engine: Pekko-based event-sourced entity engine
  *   - App: HTTP API server for CRUD operations
  */
object App
    extends BaseApp[
      IO,
      Config,
      Engine[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event],
      Manifests[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event],
      Unit,
      PostgresBase[IO],
      AppContext
    ]:

  /** Use PostgresBase for database-backed event sourcing. */
  override protected def base: Resource[IO, PostgresBase[IO]] =
    PostgresBase.default[IO]()

  /** Provide PostgresBase health checks. */
  override protected given hasHealthChecks: HasHealthChecks[IO, PostgresBase[IO]] =
    PostgresBase.hasHealthChecks[IO]

  /** No externals needed for this example. */
  override protected def externals(base: PostgresBase[IO]): Resource[IO, Unit] =
    Resource.pure(())

  /** Create manifests with the $entity;format="Camel"$ behaviour. */
  override protected def manifests(
      externals: Unit
  ): Manifests[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event] =
    Manifests(
      behaviours = Map(
        $entity;format="Camel"$State.Category -> $entity;format="Camel"$Behaviour()
      ),
      codecs = Some($entity;format="Camel"$Codecs)
    )

  /** Create the Pekko engine runtime. */
  override protected def engineRuntime(
      base: PostgresBase[IO],
      manifests: Manifests[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event],
      logEncoder: LogEncoder
  ): Resource[IO, EngineRuntime[IO, Config, Engine[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]]] =
    PekkoRuntime.create[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event](
      base       = base,
      manifests  = manifests,
      logEncoder = logEncoder
    )

  /** Start the HTTP API server after engine is ready. */
  @SuppressWarnings(Array("org.wartremover.warts.Deprecated"))
  override protected def finaliseApp(
      base: PostgresBase[IO],
      externals: Unit,
      runtime: EngineRuntime[IO, Config, Engine[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]]
  ): Resource[IO, AppContext] =
    val appConfig = AppConfig.fromTypesafeConfig(runtime.config)
    val apiRoutes = ApiRoutes.routes(runtime.entityEngine)

    for
      server <- EmberServerBuilder
        .default[IO]
        .withHost(appConfig.apiHost)
        .withPort(appConfig.apiPort)
        .withHttpApp(apiRoutes.orNotFound)
        .withShutdownTimeout(5.seconds)
        .build
      _ <- Resource.eval(IO.println(s"API server started on \${server.address}"))
    yield AppContext(server)
