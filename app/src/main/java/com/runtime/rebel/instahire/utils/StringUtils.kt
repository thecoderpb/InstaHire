package com.runtime.rebel.instahire.utils

import androidx.core.text.HtmlCompat

/**
 * Extension function for String class
 */
fun String?.removeHtmlTags(): String {
    return if(this == null) ""
    else HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
}