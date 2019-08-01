package com.example.ta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.ta.Model.UserInfo
import com.example.ta.utilss.UserSessionManager
import zendesk.core.AnonymousIdentity
import zendesk.core.Zendesk
import zendesk.support.Request
import zendesk.support.Support
import zendesk.support.request.RequestActivity

class FashionStore : AppCompatActivity() {

    lateinit var session: UserSessionManager
    lateinit var user: UserInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        HelpCrunch.initialize(
//            this,
//            "hendri",
//            2,
//            "sbLXAyZZYzvXxRqT4X9VZ4KylrfPJ8Xg6TCee87CV0TLWT1IkAqzFX3DPrSPWQGSYCB6uGx58ibRBFzlOEMnmA=="
//        )
//        HelpCrunch.showChatScreen(this)

        session = UserSessionManager(applicationContext)
        user = session.userDetails
        Zendesk.INSTANCE.init(this, "https://skripsi.zendesk.com", "Qfr7Oam", "AVTj4OGwOnKdpH1QsFc52F8bST3vIiq3z3EJ1CWRqV9ELHRIYu");

        var identity = AnonymousIdentity()
        Zendesk.INSTANCE.setIdentity(identity)

        Support.INSTANCE.init(Zendesk.INSTANCE)

        var inte = RequestActivity.builder()
            .withRequestSubject("testing")
            .withTags("android")
            .intent(this@FashionStore)

        startActivity(inte)

//        AcquireApp.getInstance().startSupportChat()

////        initWithOptions(application, "83fe3", true,true)
////        AcquireApp.getInstance().startSupportChat()
//        var json = JSONObject()
//        try {
//            json.put("success",user.name)
//        }catch (e:JSONException)
//        {
//            e.printStackTrace()
//        }
//        setVisitorDetail(user.name, user.email,user.phone,json)
    }
}
