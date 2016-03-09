package com.github.signal2564.langtonsAnt.models

import java.awt.Color

import scala.util.Random._

class Settings(pointStates: List[PointState]) {
  val startPointState = pointStates.head

  def previousValue(before: PointState): PointState = pointStates((pointStates.indexOf(before) - 1 + pointStates.length) % pointStates.length)
  def nextValue(after: PointState): PointState = pointStates((pointStates.indexOf(after) + 1) % pointStates.length)
}
object Settings {
  def apply(pointStates: String): Settings = {
    new Settings(
      pointStates.flatMap(ch => Turn.byIdentifier(ch))
        .map(turn => PointState(turn, new Color(nextInt(255), nextInt(255), nextInt(255))))
        .toList
    )
  }
}
