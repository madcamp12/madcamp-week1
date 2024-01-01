package com.example.week1


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Environment.MEDIA_MOUNTED
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            default_layout(title = "MadCamp 1st Week")
        }
        requestContactsPermission()
        requestStoragePermission()
    }

    private val CONTACTS_PERMISSION_REQUEST_CODE = 101
    private val STORAGE_PERMISSION_REQUEST_CODE = 100
    private fun requestContactsPermission() {
        val permission = Manifest.permission.READ_CONTACTS
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 권한이 이미 허용된 경우 연락처 가져오기
                fetchContacts()
            }
            else -> {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    CONTACTS_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun requestStoragePermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
//                Toast.makeText(this, "저장공간 접근이 허용되었습니다", Toast.LENGTH_SHORT).show()
//                loadimages()
            }
            else -> {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchContacts()
            } else {
                finish()
            }
        }
    }

    @SuppressLint("Range")
    private fun fetchContacts() {

        lifecycleScope.launch(Dispatchers.IO) {
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
            )

            cursor?.use {
                while (it.moveToNext()) {
                    var name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    var number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    add_contact(name, number)
                }
            }
        }
    }
}

