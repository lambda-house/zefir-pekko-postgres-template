package $package$.api

import cats.effect.IO
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder, Json}
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import zefir.entity.{Engine, EntityId, ReplyError}
import $package$.domain.*

import java.time.Instant

/** HTTP API request/response models. */
object ApiModels:
  /** Request to create a $entity$. */
  final case class Create$entity;format="Camel"$Request(name: String, description: String)

  /** Request to update a $entity$. */
  final case class Update$entity;format="Camel"$Request(name: Option[String], description: Option[String])

  /** Response containing $entity$ data. */
  final case class $entity;format="Camel"$ResponseDto(
      id: String,
      name: String,
      description: String,
      createdAt: String,
      updatedAt: String
  )

  /** Error response. */
  final case class ErrorResponse(error: String)

  /** Deleted response. */
  final case class DeletedResponse(id: String, message: String)

  // Codecs
  given Decoder[Create$entity;format="Camel"$Request] = deriveDecoder
  given Decoder[Update$entity;format="Camel"$Request] = deriveDecoder
  given Encoder[$entity;format="Camel"$ResponseDto]   = deriveEncoder
  given Encoder[ErrorResponse]                        = deriveEncoder
  given Encoder[DeletedResponse]                      = deriveEncoder

  given EntityDecoder[IO, Create$entity;format="Camel"$Request] = jsonOf[IO, Create$entity;format="Camel"$Request]
  given EntityDecoder[IO, Update$entity;format="Camel"$Request] = jsonOf[IO, Update$entity;format="Camel"$Request]
  given EntityEncoder[IO, $entity;format="Camel"$ResponseDto]   = jsonEncoderOf[IO, $entity;format="Camel"$ResponseDto]
  given EntityEncoder[IO, ErrorResponse]                        = jsonEncoderOf[IO, ErrorResponse]
  given EntityEncoder[IO, DeletedResponse]                      = jsonEncoderOf[IO, DeletedResponse]

/** HTTP routes for the $entity;format="Camel"$ API. */
object ApiRoutes:
  import ApiModels.{*, given}

  def routes(engine: Engine[IO, $entity;format="Camel"$Command, $entity;format="Camel"$Reply, $entity;format="Camel"$State, $entity;format="Camel"$Event]): HttpRoutes[IO] =
    HttpRoutes.of[IO] {

      // POST /$entity$s/:id - Create a new $entity$
      case req @ POST -> Root / "$entity$s" / $entity$Id =>
        for
          body <- req.as[Create$entity;format="Camel"$Request]
          timestamp = Instant.now()
          command   = $entity;format="Camel"$Command.Create$entity;format="Camel"$(body.name, body.description, timestamp)
          result   <- engine.ask($entity;format="Camel"$State.Category, EntityId($entity$Id), command)
          response <- result match
            case Right(Some($entity;format="Camel"$Reply.$entity;format="Camel"$Response(id, name, desc, createdAt, updatedAt))) =>
              Created(
                $entity;format="Camel"$ResponseDto(
                  id        = id,
                  name      = name,
                  description = desc,
                  createdAt = createdAt.toString,
                  updatedAt = updatedAt.toString
                )
              )

            case Right(Some($entity;format="Camel"$Reply.$entity;format="Camel"$AlreadyExists(id))) =>
              Conflict(ErrorResponse(s"$entity;format="Camel"$ \$id already exists"))

            case Right(Some(_)) =>
              InternalServerError(ErrorResponse("Unexpected reply type"))

            case Right(None) =>
              Accepted(Json.obj("status" -> Json.fromString("processing")))

            case Left(ReplyError.Timeout) =>
              GatewayTimeout(ErrorResponse("Request timed out"))

            case Left(ReplyError.CategoryNotSupported) =>
              NotFound(ErrorResponse("$entity;format="Camel"$ category not supported"))

            case Left(ReplyError.GeneralReplyError(msg)) =>
              InternalServerError(ErrorResponse(msg))
        yield response

      // GET /$entity$s/:id - Get a $entity$
      case GET -> Root / "$entity$s" / $entity$Id =>
        for
          result   <- engine.ask($entity;format="Camel"$State.Category, EntityId($entity$Id), $entity;format="Camel"$Command.Get$entity;format="Camel"$)
          response <- result match
            case Right(Some($entity;format="Camel"$Reply.$entity;format="Camel"$Response(id, name, desc, createdAt, updatedAt))) =>
              Ok(
                $entity;format="Camel"$ResponseDto(
                  id        = id,
                  name      = name,
                  description = desc,
                  createdAt = createdAt.toString,
                  updatedAt = updatedAt.toString
                )
              )

            case Right(Some($entity;format="Camel"$Reply.$entity;format="Camel"$NotFound(id))) =>
              NotFound(ErrorResponse(s"$entity;format="Camel"$ \$id not found"))

            case Right(_) =>
              InternalServerError(ErrorResponse("Unexpected reply type"))

            case Left(error) =>
              val errorMsg = error match
                case ReplyError.Timeout                => "Request timed out"
                case ReplyError.CategoryNotSupported   => "Category not supported"
                case ReplyError.GeneralReplyError(msg) => msg
              InternalServerError(ErrorResponse(errorMsg))
        yield response

      // PUT /$entity$s/:id - Update a $entity$
      case req @ PUT -> Root / "$entity$s" / $entity$Id =>
        for
          body <- req.as[Update$entity;format="Camel"$Request]
          timestamp = Instant.now()
          command   = $entity;format="Camel"$Command.Update$entity;format="Camel"$(body.name, body.description, timestamp)
          result   <- engine.ask($entity;format="Camel"$State.Category, EntityId($entity$Id), command)
          response <- result match
            case Right(Some($entity;format="Camel"$Reply.$entity;format="Camel"$Response(id, name, desc, createdAt, updatedAt))) =>
              Ok(
                $entity;format="Camel"$ResponseDto(
                  id        = id,
                  name      = name,
                  description = desc,
                  createdAt = createdAt.toString,
                  updatedAt = updatedAt.toString
                )
              )

            case Right(Some($entity;format="Camel"$Reply.$entity;format="Camel"$NotFound(id))) =>
              NotFound(ErrorResponse(s"$entity;format="Camel"$ \$id not found"))

            case Right(Some(_)) =>
              InternalServerError(ErrorResponse("Unexpected reply type"))

            case Right(None) =>
              Accepted(Json.obj("status" -> Json.fromString("processing")))

            case Left(ReplyError.Timeout) =>
              GatewayTimeout(ErrorResponse("Request timed out"))

            case Left(ReplyError.CategoryNotSupported) =>
              NotFound(ErrorResponse("Category not supported"))

            case Left(ReplyError.GeneralReplyError(msg)) =>
              InternalServerError(ErrorResponse(msg))
        yield response

      // DELETE /$entity$s/:id - Delete a $entity$
      case DELETE -> Root / "$entity$s" / $entity$Id =>
        val timestamp = Instant.now()
        val command   = $entity;format="Camel"$Command.Delete$entity;format="Camel"$(timestamp)
        for
          result   <- engine.ask($entity;format="Camel"$State.Category, EntityId($entity$Id), command)
          response <- result match
            case Right(Some($entity;format="Camel"$Reply.$entity;format="Camel"$DeletedResponse(id))) =>
              Ok(DeletedResponse(id, s"$entity;format="Camel"$ \$id deleted"))

            case Right(Some($entity;format="Camel"$Reply.$entity;format="Camel"$NotFound(id))) =>
              NotFound(ErrorResponse(s"$entity;format="Camel"$ \$id not found"))

            case Right(Some(_)) =>
              InternalServerError(ErrorResponse("Unexpected reply type"))

            case Right(None) =>
              Accepted(Json.obj("status" -> Json.fromString("processing")))

            case Left(error) =>
              val errorMsg = error match
                case ReplyError.Timeout                => "Request timed out"
                case ReplyError.CategoryNotSupported   => "Category not supported"
                case ReplyError.GeneralReplyError(msg) => msg
              InternalServerError(ErrorResponse(errorMsg))
        yield response
    }
