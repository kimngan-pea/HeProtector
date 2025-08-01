package com.example.heprotector.ui.conponents

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heprotector.data.model.BloodSugar

import androidx.compose.foundation.Indication
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
// Nếu bạn dùng màu BlueGray từ theme
import com.example.heprotector.ui.theme.BlueGray
import com.example.heprotector.viewmodel.BloodSugarViewModel
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun <T> HistoryPopup(
    title: String,
    list: List<T>,
    onClose: () -> Unit,
    itemFormatter: (T) -> String
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val historyPopupMaxHeight = screenHeight * 0.6f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onClose() }
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .heightIn(max = historyPopupMaxHeight),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    //header
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = BlueGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = BlueGray
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                //list history
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = historyPopupMaxHeight - 200.dp)
                        .background(Color(0xFFF8F8F8), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    LazyColumn {
                        items(list) { entry ->
                            Text(
                                text = "• ${itemFormatter(entry)}",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = Color.DarkGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(containerColor = BlueGray)
                    ) {
                        Text("Close", color = Color.White, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}



fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}


