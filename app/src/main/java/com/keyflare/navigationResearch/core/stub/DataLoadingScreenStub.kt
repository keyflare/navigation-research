package com.keyflare.navigationResearch.core.stub

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DataLoadingScreenStub(
    color: Color,
    label: String,
    isLoading: Boolean,
    error: Boolean,
    onBack: () -> Unit,
    onForward: (() -> Unit)? = null,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = label) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                }
                if (error) {
                    Text(text = "ERROR!!!")
                }
            }
            if (onForward != null) {
                Button(
                    onClick = onForward,
                    enabled = !isLoading && !error,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Next screen >>>")
                }
            }
        }
    }
}

@Preview
@Composable
private fun DataLoadingScreenStubPreview() {
    DataLoadingScreenStub(
        color = Color.Red,
        label = "ScreenStub",
        isLoading = true,
        error = false,
        onBack = {},
        onForward = {},
    )
}
