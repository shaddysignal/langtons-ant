package com.github.signal2564.langtonsAnt.events

import com.github.signal2564.langtonsAnt.models.Field

import scala.swing.event.Event

case class FieldRedraw(field: Field, step: Int) extends Event
