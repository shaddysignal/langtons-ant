package com.github.signal2564.langtonsAnt.models

import java.awt.Color

sealed abstract class Direction(val moveBy: Point)
object Direction {
  val values = List(Up, Right, Down, Left)

  def turnLeft(d: Direction): Direction = values((values.indexOf(d) - 1 + values.length) % values.length)
  def turnRight(d: Direction): Direction = values((values.indexOf(d) + 1) % values.length)
  def turnAround(d: Direction): Direction = values((values.indexOf(d) + 2) % values.length)

  case object Up extends Direction(Point(0, -1))
  case object Left extends Direction(Point(-1, 0))
  case object Down extends Direction(Point(0, 1))
  case object Right extends Direction(Point(1, 0))
}

sealed trait Turn
object Turn {
  def byIdentifier(id: Char): Option[Turn] = id match {
    case 'R' => Some(Right)
    case 'L' => Some(Left)
    case 'U' => Some(UTurn)
    case 'N' => Some(NoTurn)
    case _ => None
  }

  case object Left extends Turn
  case object Right extends Turn
  case object UTurn extends Turn
  case object NoTurn extends Turn
}

case class PointState(turn: Turn, color: Color)

case class Point(x: Int, y: Int) {
  def +(p: Point) = Point(x + p.x, y + p.y)
  def -(p: Point) = Point(x - p.x, y - p.y)
}

case class Ant(position: Point, direction: Direction) {
  def turnAndMove(turn: Turn): Ant = {
    val direction = turn match {
      case Turn.Left => Direction.turnLeft(this.direction)
      case Turn.Right => Direction.turnRight(this.direction)
      case Turn.UTurn => Direction.turnAround(this.direction)
      case Turn.NoTurn => this.direction
    }

    Ant(position + direction.moveBy, direction)
  }
}

case class Diff(direction: Direction)
