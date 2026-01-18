package $package$.domain

import cats.effect.IO
import cats.syntax.all.*
import zefir.entity.*

/** Behaviour implementation for the $entity;format="Camel"$ entity.
  *
  * Demonstrates the Zefir event-sourcing pattern:
  *   - Commands are handled and produce events
  *   - Events update the state
  *   - State is derived from events
  */
class $entity;format="Camel"$Behaviour extends BaseBehaviour[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]:

  override def category: EntityCategoryId = $entity;format="Camel"$State.Category

  override def initial(id: EntityId): $entity;format="Camel"$State = $entity;format="Camel"$State.initial(id)

  /** Handle commands and produce effects. */
  override def handle(
      self: Self[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply],
      state: $entity;format="Camel"$State,
      command: $entity;format="Camel"$Command
  ): IO[EngineEffect[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]] =
    command match
      case $entity;format="Camel"$Command.Create$entity;format="Camel"$(name, description, timestamp) =>
        if state.createdAt.isDefined then
          val reply = $entity;format="Camel"$Reply.$entity;format="Camel"$AlreadyExists(state.id.value)
          IO.pure(
            EngineEffect
              .none[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]
              .thenReply(_ => reply)
          )
        else
          val event = $entity;format="Camel"$Event.$entity;format="Camel"$Created(state.id, name, description, timestamp)
          val reply = $entity;format="Camel"$Reply.$entity;format="Camel"$Response(
            $entity$Id    = state.id.value,
            name        = name,
            description = description,
            createdAt   = timestamp,
            updatedAt   = timestamp
          )
          IO.pure(
            EngineEffect
              .persist[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event](event)
              .thenRunEffects { newState =>
                self.log(
                  LogLevel.Info,
                  LogCategory("$entity$.created"),
                  s"$entity;format="Camel"$ \${state.id.value} created: \$name"
                )
              }
              .thenReply(_ => reply)
          )

      case $entity;format="Camel"$Command.Update$entity;format="Camel"$(nameOpt, descOpt, timestamp) =>
        if state.createdAt.isEmpty then
          val reply = $entity;format="Camel"$Reply.$entity;format="Camel"$NotFound(state.id.value)
          IO.pure(
            EngineEffect
              .none[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]
              .thenReply(_ => reply)
          )
        else if state.deleted then
          val reply = $entity;format="Camel"$Reply.$entity;format="Camel"$NotFound(state.id.value)
          IO.pure(
            EngineEffect
              .none[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]
              .thenReply(_ => reply)
          )
        else
          val event = $entity;format="Camel"$Event.$entity;format="Camel"$Updated(state.id, nameOpt, descOpt, timestamp)
          val newName = nameOpt.getOrElse(state.name)
          val newDesc = descOpt.getOrElse(state.description)
          val reply = $entity;format="Camel"$Reply.$entity;format="Camel"$Response(
            $entity$Id    = state.id.value,
            name        = newName,
            description = newDesc,
            createdAt   = state.createdAt.getOrElse(timestamp),
            updatedAt   = timestamp
          )
          IO.pure(
            EngineEffect
              .persist[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event](event)
              .thenRunEffects { newState =>
                self.log(
                  LogLevel.Info,
                  LogCategory("$entity$.updated"),
                  s"$entity;format="Camel"$ \${state.id.value} updated"
                )
              }
              .thenReply(_ => reply)
          )

      case $entity;format="Camel"$Command.Delete$entity;format="Camel"$(timestamp) =>
        if state.createdAt.isEmpty || state.deleted then
          val reply = $entity;format="Camel"$Reply.$entity;format="Camel"$NotFound(state.id.value)
          IO.pure(
            EngineEffect
              .none[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]
              .thenReply(_ => reply)
          )
        else
          val event = $entity;format="Camel"$Event.$entity;format="Camel"$Deleted(state.id, timestamp)
          val reply = $entity;format="Camel"$Reply.$entity;format="Camel"$DeletedResponse(state.id.value)
          IO.pure(
            EngineEffect
              .persist[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event](event)
              .thenRunEffects { newState =>
                self.log(
                  LogLevel.Info,
                  LogCategory("$entity$.deleted"),
                  s"$entity;format="Camel"$ \${state.id.value} deleted"
                )
              }
              .thenReply(_ => reply)
          )

      case $entity;format="Camel"$Command.Get$entity;format="Camel"$ =>
        val reply = state.createdAt match
          case Some(created) if !state.deleted =>
            $entity;format="Camel"$Reply.$entity;format="Camel"$Response(
              $entity$Id    = state.id.value,
              name        = state.name,
              description = state.description,
              createdAt   = created,
              updatedAt   = state.updatedAt.getOrElse(created)
            )
          case _ =>
            $entity;format="Camel"$Reply.$entity;format="Camel"$NotFound(state.id.value)
        IO.pure(
          EngineEffect
            .none[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]
            .thenReply(_ => reply)
        )

  override def eventsOnCommand(state: $entity;format="Camel"$State, command: $entity;format="Camel"$Command): List[$entity;format="Camel"$Event] =
    List.empty

  override def replyToCommand(state: $entity;format="Camel"$State, command: $entity;format="Camel"$Command): ResponseToAsk =
    IO.pure(None)

  override def effects(
      self: Self[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply],
      stateBefore: Option[$entity;format="Camel"$State],
      state: $entity;format="Camel"$State,
      command: $entity;format="Camel"$Command,
      reply: Option[$entity;format="Camel"$Reply],
      events: List[$entity;format="Camel"$Event]
  ): IO[Unit] = IO.unit

  override def update(state: $entity;format="Camel"$State, event: $entity;format="Camel"$Event): $entity;format="Camel"$State =
    event match
      case $entity;format="Camel"$Event.$entity;format="Camel"$Created(_, name, description, timestamp) =>
        state.copy(
          name        = name,
          description = description,
          createdAt   = Some(timestamp),
          updatedAt   = Some(timestamp)
        )

      case $entity;format="Camel"$Event.$entity;format="Camel"$Updated(_, nameOpt, descOpt, timestamp) =>
        state.copy(
          name        = nameOpt.getOrElse(state.name),
          description = descOpt.getOrElse(state.description),
          updatedAt   = Some(timestamp)
        )

      case $entity;format="Camel"$Event.$entity;format="Camel"$Deleted(_, timestamp) =>
        state.copy(
          deleted   = true,
          updatedAt = Some(timestamp)
        )

  override def afterRecovery(self: Self[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply], state: $entity;format="Camel"$State): IO[Unit] =
    if state.createdAt.isDefined then
      self.log(
        LogLevel.Info,
        LogCategory("$entity$.recovery"),
        s"$entity;format="Camel"$ \${state.id.value} recovered: \${state.name}"
      )
    else
      IO.unit

object $entity;format="Camel"$Behaviour:
  def apply(): $entity;format="Camel"$Behaviour = new $entity;format="Camel"$Behaviour()
