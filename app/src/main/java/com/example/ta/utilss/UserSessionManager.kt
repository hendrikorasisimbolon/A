package com.example.ta.utilss

import android.content.Intent
import android.content.Context
import android.content.SharedPreferences
import com.example.ta.LoginAct
import com.example.ta.Model.MCart
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.UserInfo

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UserSessionManager
    (// Context
    internal var _context: Context
) {
//     Shared Preferences reference
    internal var pref: SharedPreferences

    // Editor reference for Shared preferences
    internal var editor: SharedPreferences.Editor

    // Shared pref mode
    internal var PRIVATE_MODE = 0

    init {
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {

        // Sharedpref file name
        private val PREFER_NAME = "AndroidExamplePref"

        // All Shared Preferences Keys
        private val IS_USER_LOGIN = "IsUserLoggedIn"

        // User name (make variable public to access from outside)
        val KEY_ID = "id"
        val KEY_NAME = "name"
        val KEY_USERNAME = "username"
        val KEY_PASSWORD = "password"
        val KEY_EMAIL = "email"
        val KEY_PHONE = "phone"
        val KEY_PROVINSI ="provinsi"
        val KEY_TAGPROVINSI = "tagprovinsi"
        val KEY_KOTA = "kota"
        val KEY_TAGKOTA = "tagkota"
        val KEY_ADDRESS = "address"
        val KEY_UMUR = "umur"
        val KEY_LAHIR = "lahir"
        val KEY_PHOTO = "photo"
        val KEY_PHOTOTYPE = "phototype"
        val KEY_CREATED = "created"

    }
    //Create login session
    fun createUserLoginSession(userInfo: UserInfo) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true)

        editor.putInt(KEY_ID, userInfo.id )
        editor.putString(KEY_NAME, userInfo.name)
        editor.putString(KEY_USERNAME, userInfo.username)
        editor.putString(KEY_PASSWORD,userInfo.password)
        editor.putString(KEY_EMAIL, userInfo.email)
        editor.putString(KEY_PHONE, userInfo.phone)
        editor.putString(KEY_PROVINSI, userInfo.nama_provinsi)
        editor.putString(KEY_TAGPROVINSI, userInfo.id_provinsi)
        editor.putString(KEY_KOTA, userInfo.nama_kota)
        editor.putString(KEY_TAGKOTA, userInfo.id_kota)
        editor.putString(KEY_ADDRESS,userInfo.address)
        editor.putString(KEY_UMUR,userInfo.umur)
        editor.putString(KEY_LAHIR,userInfo.lahir)
        editor.putString(KEY_PHOTO,userInfo.photo)
        editor.putString(KEY_PHOTOTYPE,userInfo.photo_type)
        editor.putInt(KEY_CREATED,userInfo.created_on)

        // commit changes
        editor.commit()
    }

    val userDetails: UserInfo
        get() {
            return UserInfo(
                pref.getInt(KEY_ID,-1),
                pref.getString(KEY_NAME,null),
                pref.getString(KEY_USERNAME,null),
                pref.getString(KEY_PASSWORD,null),
                pref.getString(KEY_EMAIL,null),
                pref.getString(KEY_PHONE,null),
                pref.getString(KEY_PROVINSI,null),
                pref.getString(KEY_TAGPROVINSI,null),
                pref.getString(KEY_KOTA, null),
                pref.getString(KEY_TAGKOTA, null),
                pref.getString(KEY_ADDRESS,null),
                pref.getString(KEY_UMUR,null),
                pref.getString(KEY_LAHIR, null),
                pref.getString(KEY_PHOTO, null),
                pref.getString(KEY_PHOTOTYPE, null),
                pref.getInt(KEY_CREATED,-1)
            )
        }
    fun checkLogin(): Boolean {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            val i = Intent(_context, LoginAct::class.java)

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            // Staring Login Activity
            _context.startActivity(i)

            return true
        }
        return false
    }

    fun logoutUser() {
        MCart.user_id = "0"
        MTotalCart.total_harga = 0
        MTotalCart.total_cart = 0
        // Clearing all user data from Shared Preferences
//        editor.clear()
//        editor.commit()

        // After logout redirect user to Login Activity
        val i = Intent(_context, LoginAct::class.java)

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // Staring Login Activity
        _context.startActivity(i)
    }


    fun isUserLoggedIn(): Boolean {
        return pref.getBoolean(IS_USER_LOGIN, false)
    }


}

//class UserSessionManager// Constructor
//    (// Context
//    internal var _context: Context
//) {
//
//    // Shared Preferences reference
//    internal var pref: SharedPreferences
//
//    // Editor reference for Shared preferences
//    internal var editor: SharedPreferences.Editor
//
//    // Shared pref mode
//    internal var PRIVATE_MODE = 0
//
//
//    /**
//     * Get stored session data
//     */
//    //Use hashmap to store user credentials
//    // user name
//    // user email id
//    // return user
//    val userDetails: UserInfo
//        get() {
//            return UserInfo(
//                pref.getInt(KEY_ID,-1),
//                pref.getString(KEY_NAME,null),
//                pref.getString(KEY_USERNAME,null),
//                pref.getString(KEY_EMAIL,null),
//                pref.getString(KEY_PHONE,null),
//                pref.getString(KEY_PROVINSI,null),
//                pref.getString(KEY_KOTA, null),
//                pref.getString(KEY_ADDRESS,null),
//                pref.getInt(KEY_CREATED,-1)
//            )
//        }
//
//
//    // Check for login
//    private val isUserLoggedIn: Boolean
//        get() = pref.getBoolean(IS_USER_LOGIN, false)
//
//    init {
//        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE)
//        editor = pref.edit()
//    }
//
//    //Create login session
//    fun createUserLoginSession(userInfo: UserInfo) {
//        // Storing login value as TRUE
//        editor.putBoolean(IS_USER_LOGIN, true)
//
//        editor.putInt(KEY_ID, userInfo.id )
//        editor.putString(KEY_NAME, userInfo.name)
//        editor.putString(KEY_USERNAME, userInfo.username)
//        editor.putString(KEY_EMAIL, userInfo.email)
//        editor.putString(KEY_PHONE, userInfo.phone)
//        editor.putString(KEY_PROVINSI, userInfo.nama_provinsi)
//        editor.putString(KEY_KOTA, userInfo.nama_kota)
//        editor.putString(KEY_ADDRESS,userInfo.address)
//        editor.putInt(KEY_CREATED,userInfo.created_on)
//
//        // commit changes
//        editor.commit()
//    }
//
//    /**
//     * Check login method will check user login status
//     * If false it will redirect user to login page
//     * Else do anything
//     */
//    fun checkLogin(): Boolean {
//        // Check login status
//        if (!this.isUserLoggedIn) {
//
//            // user is not logged in redirect him to Login Activity
//            val i = Intent(_context, LoginAct::class.java)
//
//            // Closing all the Activities from stack
////            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//            // Add new Flag to start new Activity
//            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//
//            // Staring Login Activity
////            _context.startActivity(i)
//            _context.startActivity(i)
//
//            return true
//        }
//        return false
//    }
//
//    /**
//     * Clear session details
//     */
//    fun logoutUser() {
//
//        // Clearing all user data from Shared Preferences
//        editor.clear()
//        editor.commit()
//
//        // After logout redirect user to Login Activity
//        val i = Intent(_context, LoginAct::class.java)
//
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//        // Staring Login Activity
//        _context.startActivity(i)
//    }
//

//}