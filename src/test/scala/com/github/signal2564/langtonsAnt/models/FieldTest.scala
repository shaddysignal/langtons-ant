package com.github.signal2564.langtonsAnt.models

import org.specs2.Specification

class FieldTest extends Specification {

  def is =
    s2"""
  Field should return correct data for paint:
    when diff apply backward after tick, should return to starting state         $applyDiffsBackward
    when diff apply forward, should be as tick happen                            $applyDiffsForward
    when tick occur, correct diff is returned                                    $tickDiff
    when tick occur, ant should move and point change state                      $tickAntPosition
  """

  def applyDiffsBackward = {
    val settings = Settings("LR")
    val field = new Field(2, 2, settings)
    val maybeDiff = field.tick()
    val pointsToBecome =
      List((Point(0, 0), settings.startPointState), (Point(0, 1), settings.startPointState),
        (Point(1, 0), settings.startPointState), (Point(1, 1), settings.startPointState))

    if (maybeDiff.nonEmpty) {
      val diff = maybeDiff.get

      field.applyDiffsBackward(List(diff))
      val (points, ant) = field.collectForPaint

      ant.position.must_==(Point(1, 1)).and(points.must(containAllOf(pointsToBecome)))
    } else
      ko("diff after tick messed up")
  }

  def applyDiffsForward = {
    val settings = Settings("LR")
    val field = new Field(2, 2, settings)
    val pointsToBecome =
      List((Point(0, 0), settings.startPointState), (Point(0, 1), settings.startPointState),
        (Point(1, 0), settings.startPointState), (Point(1, 1), settings.nextValue(settings.startPointState)))

    field.applyDiffsForward(List(Diff(Direction.Left)))
    val (points, ant) = field.collectForPaint

    ant.position.must_==(Point(0, 1)).and(points.must(containAllOf(pointsToBecome)))
  }

  def tickDiff = {
    val field = new Field(2, 2, Settings("LR"))

    field.tick().must_==(Some(Diff(Direction.Left)))
  }

  def tickAntPosition = {
    val settings = Settings("LR")
    val field = new Field(2, 2, settings)
    val pointsToBecome =
      List((Point(0, 0), settings.startPointState), (Point(0, 1), settings.startPointState),
        (Point(1, 0), settings.startPointState), (Point(1, 1), settings.nextValue(settings.startPointState)))

    field.tick()
    val (points, ant) = field.collectForPaint

    ant.position.must_==(Point(0, 1)).and(points.must(containAllOf(pointsToBecome)))
  }
}
