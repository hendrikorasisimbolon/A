package com.example.ta.RulesExtra

import com.example.ta.RegisterAct
import com.wajahatkarim3.easyvalidation.core.rules.BaseRule


class EmailValid : BaseRule {
    override fun getErrorMessage(): String {
        return "Email is already exist"
    }

    override fun validate(text: String): Boolean {
        return text.contains(RegisterAct.email_)
    }
}
