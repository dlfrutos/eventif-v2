package br.edu.ifsc.eventos.entities

data class Talk(val id: Long, val name: String, val eventId: Long, val day: String, val speaker: String, val time: String, val description: String)