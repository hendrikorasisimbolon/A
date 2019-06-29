package com.example.ta.RulesExtra

import com.example.ta.RegisterAct
import com.wajahatkarim3.easyvalidation.core.rules.BaseRule

class UsernameValid : BaseRule {

    override fun validate(text: String): Boolean {
        return text.contains(RegisterAct.username_)
    }

    override fun getErrorMessage(): String {
        return "Username is already exist"
    }

}