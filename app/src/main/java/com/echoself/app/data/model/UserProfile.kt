package com.echoself.app.data.model

data class UserProfile(
    val name: String = "",
    val age: String = "",
    val biggestGoal: String = "",
    val currentStruggle: String = ""
) {
    val isComplete: Boolean
        get() = name.isNotBlank() && age.isNotBlank() && 
                biggestGoal.isNotBlank() && currentStruggle.isNotBlank()
}
