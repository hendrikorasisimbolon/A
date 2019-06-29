package com.example.ta.RulesExtra

import com.example.ta.RegisterAct
import com.wajahatkarim3.easyvalidation.core.rules.BaseRule

class NameValid : BaseRule {
    override fun getErrorMessage(): String {
        return "Name is already exist"
    }

    override fun validate(text: String): Boolean {
        return text.contains(RegisterAct.name_)
    }
}