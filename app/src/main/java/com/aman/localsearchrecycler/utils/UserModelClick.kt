package com.aman.localsearchrecycler.utils

import com.aman.localsearchrecycler.models.UserModel

interface UserModelClick {
    fun UserModelClick(position: Int, clickType: ClickType)
}