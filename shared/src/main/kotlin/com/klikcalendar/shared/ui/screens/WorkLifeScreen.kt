//package com.klikcalendar.shared.ui.screens
//
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.Business
//import androidx.compose.material.icons.outlined.FilterAlt
//import androidx.compose.material.icons.outlined.Folder
//import androidx.compose.material.icons.outlined.Insights
//import androidx.compose.material.icons.outlined.Person
//import androidx.compose.material3.AssistChip
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.klikcalendar.shared.model.MetricSnapshot
//import com.klikcalendar.shared.model.StatusLevel
//import com.klikcalendar.shared.model.WorkLifeNode
//import com.klikcalendar.shared.model.WorkLifeNodeType
//import com.klikcalendar.shared.strings.AppStrings
//
//enum class GraphType(val title: String, val icon: ImageVector, val nodeType: WorkLifeNodeType) {
//    PROJECT("项目图谱", Icons.Outlined.Folder, WorkLifeNodeType.Project),
//    PERSON("人物图谱", Icons.Outlined.Person, WorkLifeNodeType.Person),
//    ORGANIZATION("组织图谱", Icons.Outlined.Business, WorkLifeNodeType.Team);
//
//    companion object {
//        val values = listOf(PROJECT, PERSON, ORGANIZATION)
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun WorkLifeScreen(
//    nodes: List<WorkLifeNode>,
//    strings: AppStrings,
//    modifier: Modifier = Modifier,
//) {
//    // Infinite loop pager implementation
//    // Use a reasonable page count that still provides infinite-like scrolling
//    // 3000 pages / 3 types = 1000 cycles in each direction
//    val pageCount = 3000
//    val initialPage = pageCount / 2
//    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { pageCount })
//    val coroutineScope = rememberCoroutineScope()
//
//    // Get the actual graph type based on current page (wrapping around the 3 types)
//    val currentGraphType by remember {
//        derivedStateOf {
//            GraphType.values[pagerState.currentPage % GraphType.values.size]
//        }
//    }
//
//    Column(modifier = modifier.fillMaxSize()) {
//        // Tab indicator showing current graph type
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.surface)
//                .padding(horizontal = 16.dp, vertical = 12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    imageVector = currentGraphType.icon,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.primary
//                )
//                Text(
//                    modifier = Modifier.padding(start = 8.dp),
//                    text = currentGraphType.title,
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.SemiBold,
//                )
//            }
//            AssistChip(
//                onClick = { /* filter hook */ },
//                label = { Text(strings.filterHeader) },
//                leadingIcon = { Icon(Icons.Outlined.FilterAlt, contentDescription = null) },
//            )
//        }
//
//        // Horizontal pager for graph types (infinite loop)
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier.fillMaxSize(),
//        ) { page ->
//            val graphType = GraphType.values[page % GraphType.values.size]
//            val filteredNodes = nodes.filter { it.type == graphType.nodeType }
//
//            GraphContent(
//                nodes = filteredNodes,
//                strings = strings,
//                graphType = graphType,
//            )
//        }
//    }
//}
//
//@Composable
//private fun GraphContent(
//    nodes: List<WorkLifeNode>,
//    strings: AppStrings,
//    graphType: GraphType,
//) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//    ) {
//        item {
//            Text(
//                text = "共 ${nodes.size} 个${graphType.title.substring(0, 2)}",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                modifier = Modifier.padding(vertical = 8.dp),
//            )
//        }
//
//        items(nodes, key = { it.id }) { node ->
//            WorkLifeCard(node = node, strings = strings)
//        }
//    }
//}
//
//@Composable
//private fun WorkLifeCard(
//    node: WorkLifeNode,
//    strings: AppStrings,
//) {
//    Card(
//        shape = RoundedCornerShape(24.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
//    ) {
//        Column(
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//            ) {
//                Column {
//                    Text(text = node.name, style = MaterialTheme.typography.titleMedium)
//                    Text(
//                        text = node.description,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    )
//                }
//                Text(
//                    text = node.type.name,
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.primary,
//                )
//            }
//
//            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                node.tags.forEach { tag ->
//                    Surface(
//                        shape = CircleShape,
//                        color = MaterialTheme.colorScheme.secondaryContainer,
//                    ) {
//                        Text(
//                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
//                            text = tag,
//                            style = MaterialTheme.typography.labelSmall,
//                        )
//                    }
//                }
//            }
//
//            HorizontalDivider()
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//            ) {
//                val widthFraction = 1f / node.metrics.size.coerceAtLeast(1)
//                node.metrics.forEach { metric ->
//                    MetricTile(metric = metric, modifier = Modifier.fillMaxWidth(widthFraction))
//                }
//            }
//
//            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//                Text(
//                    text = strings.insights,
//                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
//                )
//                node.linkedNodes.forEach { linked ->
//                    Text(
//                        text = "↪ $linked",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.outline,
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun MetricTile(metric: MetricSnapshot, modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .height(76.dp)
//            .clip(RoundedCornerShape(16.dp))
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(colorFor(metric.level).copy(alpha = 0.25f), Color.Transparent),
//                ),
//            ),
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 12.dp, vertical = 8.dp),
//            verticalArrangement = Arrangement.SpaceBetween,
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(Icons.Outlined.Insights, contentDescription = null, tint = colorFor(metric.level))
//                Text(
//                    modifier = Modifier.padding(start = 6.dp),
//                    text = metric.label,
//                    style = MaterialTheme.typography.labelMedium,
//                    color = colorFor(metric.level),
//                )
//            }
//            Text(
//                text = metric.value,
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//            )
//            Text(
//                text = metric.delta,
//                style = MaterialTheme.typography.bodySmall,
//                color = colorFor(metric.level),
//            )
//        }
//    }
//}
//
//@Composable
//private fun colorFor(level: StatusLevel): Color = when (level) {
//    StatusLevel.Normal -> MaterialTheme.colorScheme.primary
//    StatusLevel.Warning -> MaterialTheme.colorScheme.tertiary
//    StatusLevel.Critical -> MaterialTheme.colorScheme.error
//}
