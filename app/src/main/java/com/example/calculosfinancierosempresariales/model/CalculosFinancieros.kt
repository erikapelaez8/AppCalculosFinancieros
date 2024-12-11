package com.example.calculosfinancierosempresariales.model

object CalculosFinancieros {

    // Constantes
    private const val HORAS_LABORALES = 240 // Jornada estándar mensual
    private const val IVA = 1.19 // IVA del 19%
    private const val PARA_FISCALES_PORCENTAJE = 0.09 // SENA 2%, ICBF 3%, Caja de Compensación 4%
    private const val PRESTACIONES_SOCIALES_PORCENTAJE = 0.2183 // Prima, Cesantías, etc.
    private const val DEDUCCIONES_PORCENTAJE = 0.08 // Salud y pensión 8%

    // Cálculos de Productos
    fun calcularPrecioConIVA(precioBase: Double): Double {
        require(precioBase >= 0) { "El precio base no puede ser negativo" }
        return precioBase * IVA
    }

    fun calcularMargenGanancia(precioVenta: Double, costo: Double): Double {
        require(precioVenta >= 0 && costo >= 0) { "Los valores no pueden ser negativos" }
        return if (precioVenta != 0.0) {
            ((precioVenta - costo) / precioVenta) * 100
        } else {
            0.0
        }
    }

    fun calcularPuntoEquilibrio(costosFijos: Double, precioVentaUnitario: Double, costoVariable: Double): Double {
        require(costosFijos >= 0 && precioVentaUnitario >= 0 && costoVariable >= 0) { "Los valores no pueden ser negativos" }
        return if ((precioVentaUnitario - costoVariable) > 0) {
            costosFijos / (precioVentaUnitario - costoVariable)
        } else {
            0.0
        }
    }

    fun calcularROI(ingresos: Double, inversion: Double): Double {
        require(ingresos >= 0 && inversion > 0) { "Los ingresos deben ser positivos y la inversión mayor que 0" }
        return ((ingresos - inversion) / inversion) * 100
    }

    // Cálculos de Empleador
    fun calcularCostoTotalNomina(salarioBase: Double): Double {
        val parafiscales = calcularAportesParafiscales(salarioBase)
        val prestaciones = calcularPrestacionesSociales(salarioBase)
        return salarioBase + parafiscales + prestaciones
    }

    fun calcularAportesParafiscales(salarioBase: Double): Double {
        require(salarioBase >= 0) { "El salario base no puede ser negativo" }
        return salarioBase * PARA_FISCALES_PORCENTAJE
    }

    fun calcularPrestacionesSociales(salarioBase: Double): Double {
        require(salarioBase >= 0) { "El salario base no puede ser negativo" }
        return salarioBase * PRESTACIONES_SOCIALES_PORCENTAJE
    }

    fun calcularProvisionesSociales(salarioBase: Double): Double {
        require(salarioBase >= 0) { "El salario base no puede ser negativo" }
        return salarioBase * 0.3 // Estimado general
    }

    // Cálculos de Empleado
    fun calcularSalarioNeto(salarioBase: Double): Double {
        val deducciones = calcularDeducciones(salarioBase)
        return salarioBase - deducciones
    }

    fun calcularDeducciones(salarioBase: Double): Double {
        require(salarioBase >= 0) { "El salario base no puede ser negativo" }
        return salarioBase * DEDUCCIONES_PORCENTAJE
    }

    fun calcularHoraExtraDiurna(salarioBase: Double): Double {
        require(salarioBase >= 0) { "El salario base no puede ser negativo" }
        return (salarioBase / HORAS_LABORALES) * 1.25
    }

    fun calcularHoraExtraNocturna(salarioBase: Double): Double {
        require(salarioBase >= 0) { "El salario base no puede ser negativo" }
        return (salarioBase / HORAS_LABORALES) * 1.75
    }

    fun calcularHoraFestivaDominical(salarioBase: Double): Double {
        require(salarioBase >= 0) { "El salario base no puede ser negativo" }
        return (salarioBase / HORAS_LABORALES) * 2.0
    }

    fun calcularBonificaciones(base: Double, porcentaje: Double): Double {
        require(base >= 0 && porcentaje >= 0) { "Los valores no pueden ser negativos" }
        return base * (porcentaje / 100)
    }
}
