package com.github.signal2564.langtonsAnt

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.Timer

import com.github.signal2564.langtonsAnt.components.FieldCanvas
import com.github.signal2564.langtonsAnt.events.FieldRedraw
import com.github.signal2564.langtonsAnt.models.{Field, Settings}

import scala.swing._
import scala.swing.event.{EditDone, KeyTyped}

object App extends SimpleSwingApplication {
  override def top: Frame = new MainFrame {
    title = "Langton's Ant"

    val stepsLabel = new Label() {
      val start = "Steps pass:"

      text = s"$start 0"
      preferredSize = new Dimension(150, preferredSize.getHeight.toInt)
    }
    val widthField = newDigitField(200, 5)
    val heightField = newDigitField(200, 5)
    val settingsField = new TextField("RL", 10) {
      listenTo(keys)
      reactions += {
        case e: KeyTyped => if (!List('R', 'L', 'U', 'N').contains(e.char)) e.consume()
      }
    }
    val startButton = new Button("remake") {
      action = new Action("remake") {
        override def apply(): Unit = {
          timer.stop()
          boxPanel.deafTo(canvas.tick)

          canvas = createCanvas(settingsField.text, widthField.text.toInt, heightField.text.toInt)
          canvasFlowPanel.contents.clear()
          canvasFlowPanel.contents += canvas
          boxPanel.listenTo(canvas.tick)

          buttonPanel.contents.clear()
          buttonPanel.contents += pauseButton

          boxPanel.repaint()

          timer.restart()
        }
      }
    }
    val buttonPanel = new FlowPanel()
    val pauseButton: Button = new Button("pause") {
      preferredSize = new Dimension(100, preferredSize.getHeight.toInt)
      action = new Action("pause") {
        override def apply(): Unit = {
          timer.stop()

          buttonPanel.contents.clear()
          buttonPanel.contents += resumeButton
          buttonPanel.revalidate()
          buttonPanel.repaint()
        }
      }
    }
    val resumeButton: Button = new Button("continue") {
      preferredSize = new Dimension(100, preferredSize.getHeight.toInt)
      action = new Action("continue") {
        override def apply(): Unit = {
          timer.start()

          buttonPanel.contents.clear()
          buttonPanel.contents += pauseButton
          buttonPanel.revalidate()
          buttonPanel.repaint()
        }
      }
    }
    var canvas = createCanvas(settingsField.text, widthField.text.toInt, heightField.text.toInt)
    val canvasFlowPanel = new FlowPanel(canvas)
    val timer: Timer = new Timer(100, new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (!canvas.moveToNextState()) pauseButton.doClick()
      }
    })

    val boxPanel: Panel = new BoxPanel(Orientation.Vertical) {
      buttonPanel.contents += pauseButton

      contents ++= Seq(
        new FlowPanel(new Label("Width:"),
          widthField,
          new Label("Height:"),
          heightField,
          new Label("Settings:"),
          settingsField,
          startButton,
          buttonPanel,
          stepsLabel),
        canvasFlowPanel
      )
    }

    contents = boxPanel

    boxPanel.listenTo(canvas.tick)
    boxPanel.reactions += {
      case e @ FieldRedraw(field, step) =>
        stepsLabel.text = s"${stepsLabel.start} $step"

        repaint()
    }

    timer.start()
  }

  private def newDigitField(text: Int, columns: Int) = new TextField(text.toString, columns) {
    listenTo(keys, this)
    reactions += {
      case e @ KeyTyped(_, key, _, _) =>
        if (!key.isDigit) e.consume()
      case EditDone(source) =>
        if (source.text.isEmpty) source.text = "200"
    }
  }

  private def createCanvas(settings: String, width: Int, height: Int): FieldCanvas = {
    val field = new Field(width, height, Settings(settings))

    new FieldCanvas(field)
  }
}

