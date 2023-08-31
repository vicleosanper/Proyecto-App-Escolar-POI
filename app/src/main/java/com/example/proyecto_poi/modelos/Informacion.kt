package com.example.proyecto_poi.modelos

class Alumno (val Id: Int, val nombre: String)
class Materia(val nombre: String, val Alumnos: List<Alumno>)
class Carreras (val Nombre: String, val Materias: List<Materia>)
class Universidad (val Nombre: String, val Carreras: List<Carreras>)