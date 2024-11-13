package ru.evgeniykim.mychat.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.absoluteValue

class PhoneMask(private val mask: String): VisualTransformation {

    private val specialSymbs = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var index = 0
        text.forEach { char ->
            while (specialSymbs.contains(index)) {
                out += mask[index]
                index++
            }
            out += char
            index++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator() = object : OffsetMapping{
        override fun originalToTransformed(offset: Int): Int {
            val offseValue = offset.absoluteValue
            if (offseValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offseValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }

    }
}