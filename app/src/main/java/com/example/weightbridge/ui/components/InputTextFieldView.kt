package com.example.weightbridge.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val TITLE_DEFAULT_VALUE = "InputTextFieldView Title"
private const val ERROR_MESSAGE_DEFAULT = "Input is Error"

@Composable
@Preview
fun InputTextFieldView (
    modifier: Modifier = Modifier,
    title: String = TITLE_DEFAULT_VALUE,
    keyboardOptions: KeyboardOptions? = null,
    errorValidator: (input: String) -> Boolean = { false },
    errorMessage: String = ERROR_MESSAGE_DEFAULT,
    isError: Boolean = false,
    value: String = "",
    onValueChange: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var isFirstFocus by remember {
        mutableStateOf(true)
    }

    OutlinedTextField(
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            },
            onNext = {
                errorValidator(value).also {
                    if (!it) {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                }
            }
        ),
        modifier = modifier.onFocusChanged {
            if (!it.isFocused && !isFirstFocus) {
                errorValidator(value)
            } else {
                isFirstFocus = false
            }
        },
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = title) },
        isError = isError,
    )

    if (isError) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = errorMessage,
            color = androidx.compose.ui.graphics.Color.Red
        )
    }
}