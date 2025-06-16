package soltani.code.taskvine.helpers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit,
    color: Color,
    maxLines: Int = 2,

    ) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOverLimit by remember { mutableStateOf(false) }
    var lineCount by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        // Measure the text's line count but keep it invisible and non-intrusive
        Text(
            text = text,
            fontSize = fontSize,
            color = color,
            onTextLayout = { textLayoutResult ->
                lineCount = textLayoutResult.lineCount
                isOverLimit = lineCount > maxLines
            },
            maxLines = Int.MAX_VALUE,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .alpha(0f) // Make it invisible
                .height(0.dp) // Avoid taking up any space
        )

        // Display the actual visible text with limited lines
        Text(
            text = text,
            fontSize = fontSize,
            color = color,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )

        // Conditionally display "Read more" or "Show less" if the text exceeds max lines
        if (isOverLimit) {
            Text(
                text = if (isExpanded) "Show less" else "Read more",
                color = color,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
                    .clickable { isExpanded = !isExpanded }
            )
        }
    }
}
