package com.example.calculosfinancierosempresariales.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calculosfinancierosempresariales.viewmodel.CalculosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: CalculosViewModel) {
    var categoriaSeleccionada by remember { mutableStateOf("") }
    var calculoSeleccionado by remember { mutableStateOf("") }
    var precioBase by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var salarioBase by remember { mutableStateOf("") }
    var porcentaje by remember { mutableStateOf("") }
    val resultado by viewModel.resultado.collectAsState()
    val historial by viewModel.historial.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cálculos Financieros") }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Selección de categoría
                item {
                    Text(
                        text = "Seleccione una categoría:",
                        style = MaterialTheme.typography.titleMedium
                    )

                    DropdownMenuComponent(
                        label = "Categoría",
                        opciones = listOf("Cálculos de Productos", "Cálculos de Empleador", "Cálculos de Empleado"),
                        seleccion = categoriaSeleccionada,
                        onSeleccionChange = { categoriaSeleccionada = it; calculoSeleccionado = "" }
                    )
                }

                if (categoriaSeleccionada.isNotEmpty()) {
                    // Selección de cálculo
                    item {
                        Text(
                            text = "Seleccione un cálculo:",
                            style = MaterialTheme.typography.titleMedium
                        )

                        DropdownMenuComponent(
                            label = "Cálculo",
                            opciones = getCalculosPorCategoria(categoriaSeleccionada),
                            seleccion = calculoSeleccionado,
                            onSeleccionChange = { calculoSeleccionado = it }
                        )
                    }
                }

                // Mostrar campos según el cálculo seleccionado
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    mostrarCamposSegunCalculo(
                        categoriaSeleccionada,
                        calculoSeleccionado,
                        precioBase,
                        { precioBase = it },
                        costo,
                        { costo = it },
                        salarioBase,
                        { salarioBase = it },
                        porcentaje,
                        { porcentaje = it }
                    )
                }

                // Botón para calcular
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val inputs = mutableMapOf<String, Double>()
                            sanitizeInput(precioBase)?.let { inputs["precioBase"] = it }
                            sanitizeInput(costo)?.let { inputs["costo"] = it }
                            sanitizeInput(salarioBase)?.let { inputs["salarioBase"] = it }
                            sanitizeInput(porcentaje)?.let { inputs["porcentaje"] = it }

                            viewModel.realizarCalculo(calculoSeleccionado, inputs)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Calcular")
                    }
                }

                // Mostrar resultado
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Resultado: $resultado",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Mostrar historial de cálculos recientes
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Historial de Cálculos",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                items(historial) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Botón para limpiar historial
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.limpiarHistorial() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limpiar Historial")
                    }
                }
            }
        }
    )
}

@Composable
fun DropdownMenuComponent(
    label: String,
    opciones: List<String>,
    seleccion: String,
    onSeleccionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(seleccion.ifEmpty { "Seleccionar" })
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSeleccionChange(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun getCalculosPorCategoria(categoria: String): List<String> {
    return when (categoria) {
        "Cálculos de Productos" -> listOf("Precio con IVA", "Margen de Ganancia", "Punto de Equilibrio", "ROI del Producto")
        "Cálculos de Empleador" -> listOf("Costo Total de Nómina", "Provisiones Sociales", "Aportes Parafiscales", "Prestaciones Sociales")
        "Cálculos de Empleado" -> listOf("Salario Neto", "Deducciones de Nómina", "Horas Extras", "Bonificaciones")
        else -> emptyList()
    }
}

@Composable
fun mostrarCamposSegunCalculo(
    categoria: String,
    calculo: String,
    precioBase: String,
    onPrecioBaseChange: (String) -> Unit,
    costo: String,
    onCostoChange: (String) -> Unit,
    salarioBase: String,
    onSalarioBaseChange: (String) -> Unit,
    porcentaje: String,
    onPorcentajeChange: (String) -> Unit
) {
    when (categoria) {
        "Cálculos de Productos" -> when (calculo) {
            "Precio con IVA", "Margen de Ganancia" -> {
                OutlinedTextField(
                    value = precioBase,
                    onValueChange = onPrecioBaseChange,
                    label = { Text("Precio Base") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (calculo == "Margen de Ganancia") {
                    OutlinedTextField(
                        value = costo,
                        onValueChange = onCostoChange,
                        label = { Text("Costo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            "Punto de Equilibrio" -> {
                OutlinedTextField(
                    value = precioBase,
                    onValueChange = onPrecioBaseChange,
                    label = { Text("Precio de Venta Unitario") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = costo,
                    onValueChange = onCostoChange,
                    label = { Text("Costo Variable") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = salarioBase,
                    onValueChange = onSalarioBaseChange,
                    label = { Text("Costos Fijos") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            "ROI del Producto" -> {
                OutlinedTextField(
                    value = precioBase,
                    onValueChange = onPrecioBaseChange,
                    label = { Text("Ingresos") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = costo,
                    onValueChange = onCostoChange,
                    label = { Text("Inversión") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        "Cálculos de Empleador" -> {
            OutlinedTextField(
                value = salarioBase,
                onValueChange = onSalarioBaseChange,
                label = { Text("Salario Base") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        "Cálculos de Empleado" -> when (calculo) {
            "Salario Neto", "Deducciones de Nómina" -> {
                OutlinedTextField(
                    value = salarioBase,
                    onValueChange = onSalarioBaseChange,
                    label = { Text("Salario Base") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            "Horas Extras" -> {
                OutlinedTextField(
                    value = salarioBase,
                    onValueChange = onSalarioBaseChange,
                    label = { Text("Salario Base") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            "Bonificaciones" -> {
                OutlinedTextField(
                    value = salarioBase,
                    onValueChange = onSalarioBaseChange,
                    label = { Text("Base") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = porcentaje,
                    onValueChange = onPorcentajeChange,
                    label = { Text("Porcentaje") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

fun sanitizeInput(input: String): Double? {
    return input.replace(",", "").replace(" ", "").toDoubleOrNull()
}
