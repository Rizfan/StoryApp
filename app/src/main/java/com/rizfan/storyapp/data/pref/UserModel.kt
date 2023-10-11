package com.rizfan.storyapp.data.pref

data class UserModel(

    val token: String,

    val name: String,

    val userId: String,

    val isLogin: Boolean = false
)