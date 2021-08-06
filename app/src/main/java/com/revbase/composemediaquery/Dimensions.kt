package com.revbase.composemediaquery

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class Dimensions {
    object Width: Dimensions()
    object Height: Dimensions()

    sealed class DimensionOperator {
        object LessThan: DimensionOperator()
        object GreaterThen: DimensionOperator()
        object LessOrEqualTo: DimensionOperator()
        object GreaterOrEqualTo: DimensionOperator()
        object EqualTo: DimensionOperator()
    }

    class DimensionComparator(
        val operator: DimensionOperator,
        private val dimension: Dimensions,
        val value: Dp
    ) {
        fun compare(screenWidth: Dp, screenHeight: Dp): Boolean {
            return if(dimension is Width) {
                when(operator) {
                    is DimensionOperator.LessThan -> screenWidth < value
                    is DimensionOperator.GreaterThen -> screenWidth > value
                    is DimensionOperator.LessOrEqualTo -> screenWidth <= value
                    is DimensionOperator.GreaterOrEqualTo -> screenWidth >= value
                    is DimensionOperator.EqualTo -> screenWidth == value
                }
            } else {
                when(operator) {
                    is DimensionOperator.LessThan -> screenHeight < value
                    is DimensionOperator.GreaterThen -> screenHeight > value
                    is DimensionOperator.LessOrEqualTo -> screenHeight <= value
                    is DimensionOperator.GreaterOrEqualTo -> screenHeight >= value
                    is DimensionOperator.EqualTo -> screenHeight == value
                }
            }
        }
    }
}

@Composable
fun MediaQuery(
    comparator: Dimensions.DimensionComparator,
    content: @Composable () -> Unit
) {
    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels.dp /
            LocalDensity.current.density
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels.dp /
            LocalDensity.current.density

    if(comparator.compare(screenWidth, screenHeight)) {
        content()
    }
}

infix fun Dimensions.lessThan(value: Dp): Dimensions.DimensionComparator {
    return Dimensions.DimensionComparator(
        operator = Dimensions.DimensionOperator.LessThan,
        dimension = this,
        value = value
    )
}

infix fun Dimensions.greaterThan(value: Dp): Dimensions.DimensionComparator {
    return Dimensions.DimensionComparator(
        operator = Dimensions.DimensionOperator.GreaterThen,
        dimension = this,
        value = value
    )
}

infix fun Dimensions.lessOrEqualTo(value: Dp): Dimensions.DimensionComparator {
    return Dimensions.DimensionComparator(
        operator = Dimensions.DimensionOperator.LessOrEqualTo,
        dimension = this,
        value = value
    )
}

infix fun Dimensions.greaterOrEqualTo(value: Dp): Dimensions.DimensionComparator {
    return Dimensions.DimensionComparator(
        operator = Dimensions.DimensionOperator.GreaterOrEqualTo,
        dimension = this,
        value = value
    )
}

infix fun Dimensions.equalTo(value: Dp): Dimensions.DimensionComparator {
    return Dimensions.DimensionComparator(
        operator = Dimensions.DimensionOperator.EqualTo,
        dimension = this,
        value = value
    )
}