package $package$.domain

import zefir.entity.{EntityCategoryId, EntityId}

import java.time.Instant

/** Commands that can be sent to a $entity;format="Camel"$ entity. */
enum $entity;format="Camel"$Command:
  /** Create a new $entity$ with the given data. */
  case Create$entity;format="Camel"$(name: String, description: String, timestamp: Instant)

  /** Update an existing $entity$. */
  case Update$entity;format="Camel"$(name: Option[String], description: Option[String], timestamp: Instant)

  /** Delete the $entity$ (soft delete). */
  case Delete$entity;format="Camel"$(timestamp: Instant)

  /** Query the current $entity$ state. */
  case Get$entity;format="Camel"$

/** Events persisted by the $entity;format="Camel"$ entity. */
enum $entity;format="Camel"$Event:
  /** A $entity$ was created. */
  case $entity;format="Camel"$Created(
      id: EntityId,
      name: String,
      description: String,
      timestamp: Instant
  )

  /** A $entity$ was updated. */
  case $entity;format="Camel"$Updated(
      id: EntityId,
      name: Option[String],
      description: Option[String],
      timestamp: Instant
  )

  /** A $entity$ was deleted. */
  case $entity;format="Camel"$Deleted(
      id: EntityId,
      timestamp: Instant
  )

/** Replies returned from $entity;format="Camel"$ commands. */
enum $entity;format="Camel"$Reply:
  /** Response containing $entity$ details. */
  case $entity;format="Camel"$Response(
      $entity$Id: String,
      name: String,
      description: String,
      createdAt: Instant,
      updatedAt: Instant
  )

  /** Response when $entity$ is not found or deleted. */
  case $entity;format="Camel"$NotFound($entity$Id: String)

  /** Response confirming deletion. */
  case $entity;format="Camel"$DeletedResponse($entity$Id: String)

  /** Response when trying to create an already existing $entity$. */
  case $entity;format="Camel"$AlreadyExists($entity$Id: String)

/** State of a $entity;format="Camel"$ entity. */
final case class $entity;format="Camel"$State(
    id: EntityId,
    name: String,
    description: String,
    createdAt: Option[Instant],
    updatedAt: Option[Instant],
    deleted: Boolean
):
  def category: EntityCategoryId = $entity;format="Camel"$State.Category

object $entity;format="Camel"$State:
  val Category: EntityCategoryId = EntityCategoryId("$entity$")

  def initial(id: EntityId): $entity;format="Camel"$State = $entity;format="Camel"$State(
    id          = id,
    name        = "",
    description = "",
    createdAt   = None,
    updatedAt   = None,
    deleted     = false
  )
