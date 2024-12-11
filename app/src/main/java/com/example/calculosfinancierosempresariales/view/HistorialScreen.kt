package com.example.calculosfinancierosempresariales.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calculosfinancierosempresariales.viewmodel.CalculosViewModel

@Composable
fun HistorialScreen(viewModel: CalculosViewModel) {
    // Obtenemos el historial desde el ViewModel
    val historial = viewModel.historial.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título de la pantalla con botón para limpiar historial
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Historial de Cálculos",
                style = MaterialTheme.typography.titleLarge,
            )
            Button(onClick = { viewModel.limpiarHistorial() }) {
                Text("Limpiar")
            }
        }

        Divider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)

        // Mensaje si el historial está vacío
        if (historial.value.isEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No hay cálculos realizados aún.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Mostrar historial usando LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historial.value) { resultado ->
                    HistorialItem(resultado = resultado)
                }
            }
        }
    }
}

@Composable
fun HistorialItem(resultado: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        // Separación entre los elementos del historial
        Column(modifier = Modifier.padding(16.dp)) {
            val (categoria, detalle) = resultado.split(" - ").let {
                it.getOrNull(0) to it.getOrNull(1)
            }
            Text(
                text = "Categoría: ${categoria ?: "Desconocida"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Resultado: ${detalle ?: "No especificado"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

