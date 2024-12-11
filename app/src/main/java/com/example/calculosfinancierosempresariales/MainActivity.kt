package com.example.calculosfinancierosempresariales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.calculosfinancierosempresariales.view.MainScreen
import com.example.calculosfinancierosempresariales.viewmodel.CalculosViewModel
import com.example.calculosfinancierosempresariales.ui.theme.CalculosFinancierosEmpresarialesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crea una instancia del ViewModel
        val viewModel = CalculosViewModel()

        setContent {
            // Tema de la aplicación
            CalculosFinancierosEmpresarialesTheme {
                // Pantalla principal
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
