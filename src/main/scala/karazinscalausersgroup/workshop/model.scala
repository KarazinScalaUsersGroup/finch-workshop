package karazinscalausersgroup.workshop

import java.util.UUID

/**
  * Simple bug reporting entity
  */
object model {
  case class Ticket(uuid: UUID, reporter: String, assignee: String, description: String)
}
