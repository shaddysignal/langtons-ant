package com.github.signal2564.langtonsAnt.models

class Field(val width: Int, val height: Int, settings: Settings) {
  private val points = Array.tabulate(width, height) { (i, j) => (Point(i, j), settings.startPointState) }
  private var ant = Ant(Point(width / 2, height / 2), Direction.Up)

  def collectForPaint: (List[(Point, PointState)], Ant) = (points.flatten.toList, ant)

  def tick(): Option[Diff] = {
    val (position, state) = points(ant.position.x)(ant.position.y)
    points(position.x)(position.y) = (position, settings.nextValue(state))
    ant = ant.turnAndMove(state.turn)

    if ((0 until width).contains(ant.position.x) && (0 until height).contains(ant.position.y)) Some(Diff(ant.direction))
    else None
  }

  def applyDiffsForward(diffs: List[Diff]): Field = {
    def loop(diff: Diff, remaining: List[Diff]): Field = {
      val (position, state) = points(ant.position.x)(ant.position.y)
      points(position.x)(position.y) = (position, settings.nextValue(state))
      ant = Ant(position + diff.direction.moveBy, diff.direction)

      if (remaining.isEmpty) this
      else loop(remaining.head, remaining.tail)
    }

    loop(diffs.head, diffs.tail)
  }

  def applyDiffsBackward(diffs: List[Diff]): Field = {
    def loop(diff: Diff, remaining: List[Diff]): Field = {
      val position = ant.position - ant.direction.moveBy
      val (_, state) = points(position.x)(position.y)
      points(position.x)(position.y) = (position, settings.previousValue(state))
      ant = Ant(position, diff.direction)

      if (remaining.isEmpty) this
      else loop(remaining.head, remaining.tail)
    }

    loop(diffs.head, diffs.tail)
  }
}
