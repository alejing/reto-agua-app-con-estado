// Paquete de la app
package com.example.retoagua

// Importaciones necesarias para la actividad y elementos Compose
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.retoagua.ui.theme.RetoAguaTheme

// Actividad principal de la app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Permite que el contenido ocupe toda la pantalla
        setContent {
            // Se aplica el tema visual de la app
            RetoAguaTheme (
                darkTheme = false,
                dynamicColor = false
            ) {
                // Se llama a la función principal de la app
                RetoAguaApp()
            }
        }
    }
}

@Composable
fun RetoAguaApp() {

    // Estado para el peso ingresado por el usuario (texto)
    var peso by remember { mutableStateOf("") }

    // Estado para saber si hizo ejercicio (true/false)
    var hizoEjercicio by remember { mutableStateOf(false) }

    // Estado de la meta diaria de agua en mililitros (entero)
    var metaDiaria by remember { mutableIntStateOf(0) }

    // Estado que almacena cuántos vasos se han tomado
    var vasosTomados by remember { mutableIntStateOf(0) }

    // Constante que representa cuántos ml tiene un vaso
    val mlPorVaso = 250

    // Cálculo automático de ml totales consumidos
    val mlConsumidos = vasosTomados * mlPorVaso

    // Estructura visual de la app (layout principal)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Espaciado interior
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la app
        Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(48.dp)) // Espacio vertical

        // Campo de entrada para el peso del usuario
        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it }, // Actualiza el estado
            label = { Text("¿Cuál es tu peso? (kg)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fila con el texto y el switch de ejercicio
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Hiciste ejercicio hoy?")
            Switch(
                checked = hizoEjercicio,
                onCheckedChange = { hizoEjercicio = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para calcular la meta de agua diaria
        Button(
            onClick = {
                val pesoKg = peso.toIntOrNull() // Convierte el peso a entero
                if (pesoKg != null) {
                    // Fórmula: peso * 35 ml, más 500 si hizo ejercicio
                    metaDiaria = (pesoKg * 35) + if (hizoEjercicio) 500 else 0
                    vasosTomados = 0 // Reinicia contador
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular meta de agua")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Solo se muestra esta sección si ya se calculó la meta
        if (metaDiaria > 0) {
            // Muestra la meta diaria y lo que ha tomado
            Text(
                text = "🎯 Tu meta diaria es: $metaDiaria ml",
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "🥤 Has tomado: $mlConsumidos ml",
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para agregar 1 vaso (250 ml)
            Button(
                onClick = {
                    // Solo suma si aún no se cumple la meta
                    if (mlConsumidos < metaDiaria) vasosTomados++
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+1 vaso (250 ml)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Muestra si la meta fue alcanzada o cuánto falta
            if (mlConsumidos >= metaDiaria) {
                Text(
                    text = "✅ Meta alcanzada ✅",
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                val restante = metaDiaria - mlConsumidos
                Text(
                    text = "Te faltan $restante ml. ¡Sigue así! 💪"
                )
            }
        }
    }
}

// Vista previa del Composable (solo para diseño)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RetoAguaAppPreview() {
    RetoAguaTheme (
        darkTheme = false,
        dynamicColor = false
    ) {
        RetoAguaApp()
    }
}