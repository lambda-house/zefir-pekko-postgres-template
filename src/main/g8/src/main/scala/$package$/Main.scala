package $package$

import cats.effect.unsafe.VirtualThreadsIOApp
import cats.effect.{IO, IOApp}

/** Entry point for $name$ service.
  *
  * This application uses Zefir event-sourcing with:
  *   - PostgreSQL R2DBC journal for event persistence
  *   - Pekko cluster for entity management
  *   - HTTP API for $entity$ management
  *   - Prometheus metrics integration
  *
  * Run with: sbt run
  *
  * Endpoints:
  *   - POST /$entity$s/:id - Create a new $entity$
  *   - GET /$entity$s/:id - Get a $entity$
  *   - PUT /$entity$s/:id - Update a $entity$
  *   - DELETE /$entity$s/:id - Delete a $entity$
  *   - GET /ready - Readiness probe (port 9095)
  *   - GET /alive - Liveness probe (port 9095)
  *   - GET /metrics - Prometheus metrics (port 9095)
  */
object Main extends IOApp.Simple with VirtualThreadsIOApp:

  override val run: IO[Unit] = App.runLifecycle
