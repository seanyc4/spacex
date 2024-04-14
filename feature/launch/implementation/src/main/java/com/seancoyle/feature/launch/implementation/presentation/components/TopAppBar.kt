package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.seancoyle.feature.launch.implementation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 4.dp
    ) {
        TopAppBar(
            modifier = modifier,
            title = {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                actionIconContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.secondary
            ),
            actions = {
                IconButton(
                    onClick = { onClick() },
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.small_view_margins_8dp))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter_list),
                        contentDescription = stringResource(id = R.string.filter_btn_content_desc)
                    )
                }
            },
        )
    }
}