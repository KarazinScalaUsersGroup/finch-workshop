package karazinscalausersgroup.workshop

import java.util.UUID

import karazinscalausersgroup.workshop.model.Ticket

/**
  * Simple in-memory storage
  */
object storage {
  private val storage = collection.mutable.HashMap[UUID, Ticket]()
  def get(uuid: UUID): Option[Ticket] = storage.get(uuid)
  def put(ticket: Ticket): Option[Ticket] = storage.put(ticket.uuid, ticket)
  def remove(uuid: UUID): Option[Ticket] = storage.remove(uuid)
}
