package com.talentmarketplace.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talentmarketplace.view.theme.Primary
import com.talentmarketplace.view.theme.Secondary

@Composable
fun WideButtonComponent(
    onClick: () -> Unit,
    label: Int
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
       Box(
           modifier = Modifier
               .fillMaxWidth()
               .heightIn(48.dp)
               .background(
                   brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                   shape = RoundedCornerShape(50.dp)
               ),
           contentAlignment = Alignment.Center
       ) {
            Text(
                text = stringResource(label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold

            )
       }
    }
}
