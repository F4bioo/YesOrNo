package fbo.costa.yesorno.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnsApiEntity(
    @SerializedName("answer")
    @Expose
    val answer: String?,

    @SerializedName("forced")
    @Expose
    val forced: Boolean?,

    @SerializedName("image")
    @Expose
    val image: String?
)
