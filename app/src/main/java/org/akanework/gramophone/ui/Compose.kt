package org.akanework.gramophone.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import org.akanework.gramophone.logic.enableEdgeToEdgeProperly
import org.akanework.gramophone.logic.getBooleanStrict
import org.akanework.gramophone.ui.theme.asPureDark
import org.akanework.gramophone.ui.theme.sunshinePalette
import org.akanework.gramophone.ui.theme.toColorScheme

abstract class BaseComposeActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    val pureDarkFlow by lazy {
        callbackFlow {
            val cb = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == "pureDark") {
                    trySendBlocking(prefs.getBooleanStrict("pureDark", false))
                }
            }
            prefs.registerOnSharedPreferenceChangeListener(cb)
            awaitClose {
                prefs.unregisterOnSharedPreferenceChangeListener(cb)
            }
        }.stateIn(
            lifecycleScope, WhileSubscribed(),
            prefs.getBooleanStrict("pureDark", false)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeProperly()
    }
}

@Composable
fun BaseComposeActivity.GramophoneTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val pureDark by pureDarkFlow.collectAsState()
    GramophoneTheme(useDarkTheme, pureDark, content)
}

@Composable
fun GramophoneTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    pureDark: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        // Sunshine Music: fixed palette, no Material You dynamic colour.
        colorScheme = sunshinePalette(useDarkTheme).toColorScheme(useDarkTheme).let {
            if (useDarkTheme && pureDark) it.asPureDark() else it
        }, content = {
            CompositionLocalProvider(
                LocalContentColor provides contentColorFor(MaterialTheme.colorScheme.surface),
            ) {
                content()
            }
        }
    )
}
