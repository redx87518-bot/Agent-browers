package dev.agentbrowser.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddressBar(
    url: String,
    onUrlChange: (String) -> Unit,
    onUrlSubmit: (String) -> Unit,
    onTabClick: () -> Unit,
    tabCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (url.isNotBlank()) {
                    IconButton(onClick = { onUrlChange("") }, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            placeholder = { Text("Search or enter URL") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onSearch = {
                    if (url.isNotBlank()) {
                        onUrlSubmit(url)
                    }
                }
            )
        )

        IconButton(onClick = onTabClick, modifier = Modifier.size(40.dp)) {
            Text(
                text = tabCount.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
