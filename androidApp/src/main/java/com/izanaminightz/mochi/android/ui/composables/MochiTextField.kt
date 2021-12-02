package com.izanaminightz.mochi.android.ui.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material.icons.sharp.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.izanaminightz.mochi.android.R


@Preview
@Composable
private fun MochiTextFieldPreview() {
    MochiTextField(onSearch = {})
}

@Composable
fun MochiTextField(
    onSearch: (String) -> Unit,
) {



    val textState = remember {
        mutableStateOf(TextFieldValue())
    }

    val focus = remember {
        FocusRequester()
    }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        focus.requestFocus()
    }


    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(4.dp))
            .padding(horizontal = 10.dp, vertical = 2.dp)

    ) {
        TextField(

            trailingIcon = {
                if (textState.value.text.isNotEmpty())
                    IconButton(onClick = { textState.value = TextFieldValue() }) {
                        Icon(Icons.Sharp.Clear, stringResource(id = R.string.clear_search), tint = colorResource(
                            id = R.color.softGrayText
                        ))
                    }
            },
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focus),
            leadingIcon =  { Icon(Icons.Sharp.Search, stringResource(id = R.string.search), tint = colorResource(
                id = R.color.softGrayText
            ) ) },
            placeholder = {
                Text(
                    text = stringResource(
                        id = R.string.search_phrase,

                        ),
                    color = colorResource(id = R.color.softGrayText)
                )

            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                },
                onSearch = {
                    if (textState.value.text.isNotBlank() && textState.value.text.isNotEmpty()) {
                        focusManager.clearFocus()
                        onSearch(textState.value.text)
                    }

                }
            )

            )


    }
}