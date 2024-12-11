package com.example.calculosfinancierosempresariales.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.calculosfinancierosempresariales.model.CalculosFinancieros

class CalculosViewModel : ViewModel() {
    private val _resultado = MutableStateFlow("")
    val resultado: StateFlow<String> = _resultado.asStateFlow()

    private val _historial = MutableStateFlow<List<String>>(emptyList())
    val historial: StateFlow<List<String>> = _historial.asStateFlow()

    fun realizarCalculo(categoria: String, inputs: Map<String, Double>) {
        // Validar entradas antes de procesar el cálculo
        val resultadoCalculo = try {
            when (categoria) {
                // Cálculos de Productos
                "Precio con IVA" -> CalculosFinancieros.calcularPrecioConIVA(getInputValue(inputs, "precioBase"))
                "Margen de Ganancia" -> CalculosFinancieros.calcularMargenGanancia(
                    precioVenta = getInputValue(inputs, "precioBase"),
                    costo = getInputValue(inputs, "costo")
                )
                "Punto de Equilibrio" -> CalculosFinancieros.calcularPuntoEquilibrio(
                    costosFijos = getInputValue(inputs, "salarioBase"),
                    precioVentaUnitario = getInputValue(inputs, "precioBase"),
                    costoVariable = getInputValue(inputs, "costo")
                )
                "ROI del Producto" -> CalculosFinancieros.calcularROI(
                    ingresos = getInputValue(inputs, "precioBase"),
                    inversion = getInputValue(inputs, "costo")
                )

                // Cálculos de Empleador
                "Costo Total de Nómina" -> CalculosFinancieros.calcularCostoTotalNomina(getInputValue(inputs, "salarioBase"))
                "Provisiones Sociales" -> CalculosFinancieros.calcularProvisionesSociales(getInputValue(inputs, "salarioBase"))
                "Aportes Parafiscales" -> CalculosFinancieros.calcularAportesParafiscales(getInputValue(inputs, "salarioBase"))
                "Prestaciones Sociales" -> CalculosFinancieros.calcularPrestacionesSociales(getInputValue(inputs, "salarioBase"))

                // Cálculos de Empleado
                "Salario Neto" -> CalculosFinancieros.calcularSalarioNeto(getInputValue(inputs, "salarioBase"))
                "Deducciones de Nómina" -> CalculosFinancieros.calcularDeducciones(getInputValue(inputs, "salarioBase"))
                "Horas Extras" -> CalculosFinancieros.calcularHoraExtraDiurna(getInputValue(inputs, "salarioBase"))
                "Bonificaciones" -> CalculosFinancieros.calcularBonificaciones(
                    base = getInputValue(inputs, "salarioBase"),
                    porcentaje = getInputValue(inputs, "porcentaje")
                )
                else -> "Cálculo no definido"
            }
        } catch (e: IllegalArgumentException) {
            "Error: ${e.message}" // Mostrar el mensaje de error al usuario
        }

        _resultado.value = resultadoCalculo.toString()
        if (resultadoCalculo !is String || !resultadoCalculo.startsWith("Error")) {
            agregarAlHistorial("Categoría: $categoria - Resultado: $resultadoCalculo")
        }
    }

    private fun agregarAlHistorial(item: String) {
        val maxHistorial = 10 // Número máximo de cálculos en el historial
        _historial.value = (_historial.value + item).takeLast(maxHistorial)
    }

    fun limpiarHistorial() {
        _historial.value = emptyList()
    }

    // Función auxiliar para obtener valores de entradas
    private fun getInputValue(inputs: Map<String, Double>, key: String): Double {
        return inputs[key] ?: throw IllegalArgumentException("El valor para '$key' es requerido.")
    }
}
