package com.runtime.rebel.instahire.model

import com.google.gson.annotations.SerializedName


data class GptPdfRequest(
    @SerializedName("model") val model: String = "gpt-4o-mini",
    @SerializedName("messages") val messages: List<Message>,
)

data class Message(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String,
    @SerializedName("file_url") val fileUrl: String? = null
)

data class GptResponse(
    @SerializedName("choices") val choices: List<Choice>
)

data class Choice(
    @SerializedName("message") val message: MessageContent
)

data class MessageContent(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)
