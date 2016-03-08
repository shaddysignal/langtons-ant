package com.github.signal2564.langtonsAnt.components

import java.awt.{Color, Dimension}

import com.github.signal2564.langtonsAnt.events.FieldRedraw
import com.github.signal2564.langtonsAnt.models._

import scala.swing.{Component, Graphics2D, Publisher}

class FieldCanvas(field: Field) extends Component {
  private val rectSize = 4
  private var diffs: List[Diff] = List()

  object tick extends Publisher

  preferredSize = new Dimension(field.width * rectSize, field.height * rectSize)

  def moveToNextState(): Boolean = {
    val maybeDiff = field.tick()

    if (maybeDiff.nonEmpty) {
      diffs = maybeDiff.get :: diffs

      tick.publish(new FieldRedraw(field, diffs.length))
      true
    } else false
  }

  override protected def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)

    val (points, ant) = field.collectForPaint
    points.foreach {
      case (p: Point, s: PointState) =>
        g.setColor(s.color)
        g.fillRect(p.x * rectSize, p.y * rectSize, rectSize, rectSize)
    }

    g.setColor(Color.RED)
    g.fillRect(ant.position.x * rectSize, ant.position.y * rectSize, rectSize, rectSize)
  }
}
