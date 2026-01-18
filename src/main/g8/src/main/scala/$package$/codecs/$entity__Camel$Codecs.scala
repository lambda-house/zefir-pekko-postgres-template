package $package$.codecs

import io.circe.generic.semiauto.deriveCodec
import io.circe.syntax.*
import io.circe.{Codec, Json}
import zefir.entity.{CannotDecode, CannotEncode, CodecError, Codecs, EntityId, UnknownManifest}
import $package$.domain.*

/** Circe-based codecs for $entity;format="Camel"$ domain types.
  *
  * Implements Zefir's Codecs trait for serialization/deserialization of commands, events, state, and replies.
  */
object $entity;format="Camel"$Codecs extends Codecs:

  // Entity ID codec
  private given Codec[EntityId] = Codec.from(
    io.circe.Decoder.decodeString.map(EntityId.apply),
    io.circe.Encoder.encodeString.contramap(_.value)
  )

  // Domain type codecs
  private given Codec[$entity;format="Camel"$Command] = deriveCodec
  private given Codec[$entity;format="Camel"$Event]   = deriveCodec
  private given Codec[$entity;format="Camel"$Reply]   = deriveCodec
  private given Codec[$entity;format="Camel"$State]   = deriveCodec

  // Manifest constants
  private val CommandManifest = "$entity;format="Camel"$Command"
  private val EventManifest   = "$entity;format="Camel"$Event"
  private val ReplyManifest   = "$entity;format="Camel"$Reply"
  private val StateManifest   = "$entity;format="Camel"$State"

  override def maybeManifest(o: AnyRef): Either[CodecError, String] = o match
    case _: $entity;format="Camel"$Command => Right(CommandManifest)
    case _: $entity;format="Camel"$Event   => Right(EventManifest)
    case _: $entity;format="Camel"$Reply   => Right(ReplyManifest)
    case _: $entity;format="Camel"$State   => Right(StateManifest)
    case v                                 => Left(UnknownManifest(s"No manifest for \${v.getClass.getName}"))

  override def maybeToJson(o: AnyRef): Either[CodecError, Json] = o match
    case c: $entity;format="Camel"$Command => Right(c.asJson)
    case e: $entity;format="Camel"$Event   => Right(e.asJson)
    case r: $entity;format="Camel"$Reply   => Right(r.asJson)
    case s: $entity;format="Camel"$State   => Right(s.asJson)
    case v                                 => Left(CannotEncode(s"Cannot encode \${v.getClass.getName}"))

  override def maybeFromJson(manifest: String, json: Json): Either[CodecError, AnyRef] =
    manifest match
      case CommandManifest =>
        summon[Codec[$entity;format="Camel"$Command]].decodeJson(json).left.map(e => CannotDecode(e.message))
      case EventManifest =>
        summon[Codec[$entity;format="Camel"$Event]].decodeJson(json).left.map(e => CannotDecode(e.message))
      case ReplyManifest =>
        summon[Codec[$entity;format="Camel"$Reply]].decodeJson(json).left.map(e => CannotDecode(e.message))
      case StateManifest =>
        summon[Codec[$entity;format="Camel"$State]].decodeJson(json).left.map(e => CannotDecode(e.message))
      case _ =>
        Left(CannotDecode(s"Unknown manifest: \$manifest"))
